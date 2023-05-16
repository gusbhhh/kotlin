/*
 * Copyright 2010-2023 JetBrains s.r.o. and Kotlin Programming Language contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the license/LICENSE.txt file.
 */

package org.jetbrains.kotlin.analysis.low.level.api.fir.transformers

import org.jetbrains.kotlin.analysis.low.level.api.fir.api.targets.LLFirResolveTarget
import org.jetbrains.kotlin.analysis.low.level.api.fir.api.throwUnexpectedFirElementError
import org.jetbrains.kotlin.analysis.low.level.api.fir.file.builder.LLFirLockProvider
import org.jetbrains.kotlin.analysis.low.level.api.fir.lazy.resolve.LLFirPhaseUpdater
import org.jetbrains.kotlin.analysis.low.level.api.fir.util.checkDelegatedConstructorIsResolved
import org.jetbrains.kotlin.analysis.low.level.api.fir.util.*
import org.jetbrains.kotlin.analysis.utils.errors.buildErrorWithAttachment
import org.jetbrains.kotlin.analysis.utils.errors.checkWithAttachmentBuilder
import org.jetbrains.kotlin.fir.FirElementWithResolveState
import org.jetbrains.kotlin.fir.FirFileAnnotationsContainer
import org.jetbrains.kotlin.fir.FirSession
import org.jetbrains.kotlin.fir.declarations.*
import org.jetbrains.kotlin.fir.declarations.utils.getExplicitBackingField
import org.jetbrains.kotlin.fir.expressions.*
import org.jetbrains.kotlin.fir.expressions.builder.buildLazyDelegatedConstructorCall
import org.jetbrains.kotlin.fir.expressions.impl.FirLazyDelegatedConstructorCall
import org.jetbrains.kotlin.fir.references.FirSuperReference
import org.jetbrains.kotlin.fir.references.FirThisReference
import org.jetbrains.kotlin.fir.references.builder.buildExplicitSuperReference
import org.jetbrains.kotlin.fir.references.builder.buildExplicitThisReference
import org.jetbrains.kotlin.fir.resolve.ScopeSession
import org.jetbrains.kotlin.fir.resolve.dfa.FirControlFlowGraphReferenceImpl
import org.jetbrains.kotlin.fir.resolve.transformers.body.resolve.FirBodyResolveTransformer
import org.jetbrains.kotlin.fir.resolve.transformers.body.resolve.FirTowerDataContextCollector
import org.jetbrains.kotlin.fir.symbols.lazyResolveToPhase
import org.jetbrains.kotlin.fir.types.FirResolvedTypeRef

internal object LLFirBodyLazyResolver : LLFirLazyResolver(FirResolvePhase.BODY_RESOLVE) {
    override fun resolve(
        target: LLFirResolveTarget,
        lockProvider: LLFirLockProvider,
        session: FirSession,
        scopeSession: ScopeSession,
        towerDataContextCollector: FirTowerDataContextCollector?,
    ) {
        val resolver = LLFirBodyTargetResolver(target, lockProvider, session, scopeSession, towerDataContextCollector)
        resolver.resolveDesignation()
    }

    override fun updatePhaseForDeclarationInternals(target: FirElementWithResolveState) {
        LLFirPhaseUpdater.updateDeclarationInternalsPhase(target, resolverPhase, updateForLocalDeclarations = true)
    }

    override fun checkIsResolved(target: FirElementWithResolveState) {
        target.checkPhase(resolverPhase)
        when (target) {
            is FirValueParameter -> checkDefaultValueIsResolved(target)
            is FirVariable -> checkInitializerIsResolved(target)
            is FirConstructor -> {
                checkDelegatedConstructorIsResolved(target)
                checkBodyIsResolved(target)
            }
            is FirFunction -> checkBodyIsResolved(target)
        }

        checkNestedDeclarationsAreResolved(target)
    }
}

private class LLFirBodyTargetResolver(
    target: LLFirResolveTarget,
    lockProvider: LLFirLockProvider,
    session: FirSession,
    scopeSession: ScopeSession,
    towerDataContextCollector: FirTowerDataContextCollector?,
) : LLFirAbstractBodyTargetResolver(
    target,
    lockProvider,
    scopeSession,
    FirResolvePhase.BODY_RESOLVE,
) {
    override val transformer = object : FirBodyResolveTransformer(
        session,
        phase = resolverPhase,
        implicitTypeOnly = false,
        scopeSession = scopeSession,
        returnTypeCalculator = createReturnTypeCalculator(towerDataContextCollector = towerDataContextCollector),
        firTowerDataContextCollector = towerDataContextCollector,
    ) {
        override val preserveCFGForClasses: Boolean get() = false
    }

    override fun doResolveWithoutLock(target: FirElementWithResolveState): Boolean {
        when (target) {
            is FirRegularClass -> {
                if (target.resolvePhase >= resolverPhase) return true

                // resolve class CFG graph here, to do this we need to have property & init blocks resoled
                resolveMembersForControlFlowGraph(target)
                performCustomResolveUnderLock(target) {
                    calculateControlFlowGraph(target)
                }

                return true
            }
        }

        return false
    }

    private fun calculateControlFlowGraph(target: FirRegularClass) {
        checkWithAttachmentBuilder(
            target.controlFlowGraphReference == null,
            { "'controlFlowGraphReference' should be 'null' if the class phase < $resolverPhase)" },
        ) {
            withFirEntry("firClass", target)
        }

        val dataFlowAnalyzer = transformer.declarationsTransformer.dataFlowAnalyzer
        dataFlowAnalyzer.enterClass(target, buildGraph = true)
        val controlFlowGraph = dataFlowAnalyzer.exitClass()
            ?: buildErrorWithAttachment("CFG should not be 'null' as 'buildGraph' is specified") {
                withFirEntry("firClass", target)
            }

        target.replaceControlFlowGraphReference(FirControlFlowGraphReferenceImpl(controlFlowGraph))
    }

    private fun resolveMembersForControlFlowGraph(target: FirRegularClass) {
        withRegularClass(target) {
            transformer.firTowerDataContextCollector?.addDeclarationContext(target, transformer.context.towerDataContext)

            for (member in target.declarations) {
                if (member is FirCallableDeclaration || member is FirAnonymousInitializer) {
                    // TODO: Ideally, only properties and init blocks should be resolved here.
                    // However, dues to changes in the compiler resolution, we temporarily have to resolve all callable members.
                    // Such additional work might affect incremental analysis performance.
                    member.lazyResolveToPhase(resolverPhase.previous)
                    performResolve(member)
                }
            }
        }
    }

    override fun doLazyResolveUnderLock(target: FirElementWithResolveState) {
        val contextCollector = transformer.firTowerDataContextCollector
        if (contextCollector != null && target is FirDeclaration) {
            val bodyResolveContext = transformer.context
            if (target is FirFunction) {
                bodyResolveContext.forFunctionBody(target, transformer.components) {
                    contextCollector.addDeclarationContext(target, bodyResolveContext.towerDataContext)
                }
            } else {
                contextCollector.addDeclarationContext(target, bodyResolveContext.towerDataContext)
            }
        }

        when (target) {
            is FirRegularClass -> error("Should have been resolved in ${::doResolveWithoutLock.name}")
            is FirSimpleFunction -> resolve(target, BodyStateKeepers.FUNCTION)
            is FirConstructor -> resolve(target, BodyStateKeepers.CONSTRUCTOR)
            is FirProperty -> resolve(target, BodyStateKeepers.PROPERTY)
            is FirPropertyAccessor -> resolve(target.propertySymbol.fir, BodyStateKeepers.PROPERTY)
            is FirVariable -> resolve(target, BodyStateKeepers.VARIABLE)
            is FirAnonymousInitializer -> resolve(target, BodyStateKeepers.ANONYMOUS_INITIALIZER)
            is FirScript -> resolve(target, BodyStateKeepers.SCRIPT)
            is FirDanglingModifierList,
            is FirFileAnnotationsContainer,
            is FirTypeAlias,
            is FirCallableDeclaration -> {
                // No bodies here
            }
            else -> throwUnexpectedFirElementError(target)
        }
    }
}

internal object BodyStateKeepers {
    val SCRIPT: StateKeeper<FirScript> = stateKeeper {
        // TODO Lazy body is not supported for scripts yet
    }

    val ANONYMOUS_INITIALIZER: StateKeeper<FirAnonymousInitializer> = stateKeeper {
        add(FirAnonymousInitializer::body, FirAnonymousInitializer::replaceBody, ::blockGuard)
    }

    val FUNCTION: StateKeeper<FirFunction> = stateKeeper { function ->
        if (function.isCertainlyResolved) {
            return@stateKeeper
        }

        add(FirFunction::returnTypeRef, FirFunction::replaceReturnTypeRef)

        if (!isCallableWithSpecialBody(function)) {
            add(FirFunction::bodyIfUnresolved, FirFunction::replaceBody, ::blockGuard)

            entityList(function.valueParameters) { valueParameter ->
                if (valueParameter.defaultValue != null) {
                    add(FirValueParameter::defaultValue, FirValueParameter::replaceDefaultValue, ::expressionGuard)
                }
            }
        }

        add(FirFunction::controlFlowGraphReference, FirFunction::replaceControlFlowGraphReference)
    }

    val CONSTRUCTOR: StateKeeper<FirConstructor> = stateKeeper {
        add(FUNCTION)
        add(FirConstructor::delegatedConstructor, FirConstructor::replaceDelegatedConstructor, ::delegatedConstructorCallGuard)
    }

    val VARIABLE: StateKeeper<FirVariable> = stateKeeper { variable ->
        add(FirVariable::returnTypeRef, FirVariable::replaceReturnTypeRef)

        if (!isCallableWithSpecialBody(variable)) {
            add(FirVariable::initializerIfUnresolved, FirVariable::replaceInitializer, ::expressionGuard)
        }
    }

    val PROPERTY: StateKeeper<FirProperty> = stateKeeper { property ->
        if (property.bodyResolveState >= FirPropertyBodyResolveState.EVERYTHING_RESOLVED) {
            return@stateKeeper
        }

        add(VARIABLE)

        add(FirProperty::bodyResolveState, FirProperty::replaceBodyResolveState)
        add(FirProperty::returnTypeRef, FirProperty::replaceReturnTypeRef)

        entity(property.getterIfUnresolved, FUNCTION)
        entity(property.setterIfUnresolved, FUNCTION)

        entity(property.backingFieldIfUnresolved) {
            add(VARIABLE)
        }

        entity(property.delegateIfUnresolved) {
            add(FirWrappedDelegateExpression::expression, FirWrappedDelegateExpression::replaceExpression, ::expressionGuard)
            add(FirWrappedDelegateExpression::delegateProvider, FirWrappedDelegateExpression::replaceDelegateProvider, ::expressionGuard)
        }
    }
}

private val FirFunction.isCertainlyResolved: Boolean
    get() {
        val body = this.body ?: return false // Not completely sure
        return body !is FirLazyBlock && body.typeRef is FirResolvedTypeRef
    }

private val FirFunction.bodyIfUnresolved: FirBlock?
    get() = if (!isCertainlyResolved) body else null

private val FirVariable.initializerIfUnresolved: FirExpression?
    get() = when (this) {
        is FirProperty -> if (bodyResolveState < FirPropertyBodyResolveState.INITIALIZER_RESOLVED) initializer else null
        else -> initializer
    }

private val FirProperty.backingFieldIfUnresolved: FirBackingField?
    get() = if (bodyResolveState < FirPropertyBodyResolveState.INITIALIZER_RESOLVED) getExplicitBackingField() else null

private val FirProperty.getterIfUnresolved: FirPropertyAccessor?
    get() = if (bodyResolveState < FirPropertyBodyResolveState.INITIALIZER_AND_GETTER_RESOLVED) getter else null

private val FirProperty.setterIfUnresolved: FirPropertyAccessor?
    get() = if (bodyResolveState < FirPropertyBodyResolveState.EVERYTHING_RESOLVED) setter else null

private val FirProperty.delegateIfUnresolved: FirWrappedDelegateExpression?
    get() = if (bodyResolveState < FirPropertyBodyResolveState.EVERYTHING_RESOLVED) delegate as? FirWrappedDelegateExpression else null

private fun delegatedConstructorCallGuard(fir: FirDelegatedConstructorCall): FirDelegatedConstructorCall {
    if (fir is FirLazyDelegatedConstructorCall) {
        return fir
    }

    return buildLazyDelegatedConstructorCall {
        constructedTypeRef = fir.constructedTypeRef
        when (val originalCalleeReference = fir.calleeReference) {
            is FirThisReference -> {
                isThis = true
                calleeReference = buildExplicitThisReference {
                    source = null
                }
            }
            is FirSuperReference -> {
                isThis = false
                calleeReference = buildExplicitSuperReference {
                    source = null
                    superTypeRef = originalCalleeReference.superTypeRef
                }
            }
        }
    }
}