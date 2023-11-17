/*
 * Copyright 2010-2020 JetBrains s.r.o. and Kotlin Programming Language contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the license/LICENSE.txt file.
 */

package org.jetbrains.kotlin.analysis.api.components

import org.jetbrains.kotlin.analysis.api.lifetime.withValidityAssertion
import org.jetbrains.kotlin.analysis.api.symbols.KtCallableSymbol
import org.jetbrains.kotlin.analysis.api.symbols.KtDeclarationSymbol
import org.jetbrains.kotlin.analysis.api.symbols.KtFileSymbol
import org.jetbrains.kotlin.analysis.api.symbols.KtSymbol
import org.jetbrains.kotlin.analysis.project.structure.KtModule
import org.jetbrains.kotlin.resolve.jvm.JvmClassName

public abstract class KtSymbolContainingDeclarationProvider : KtAnalysisSessionComponent() {
    public abstract fun getContainingDeclaration(symbol: KtSymbol): KtDeclarationSymbol?

    public abstract fun getContainingFile(symbol: KtSymbol): KtFileSymbol?

    public abstract fun getContainingJvmClassName(symbol: KtCallableSymbol): JvmClassName?

    public abstract fun getContainingModule(symbol: KtSymbol): KtModule
}

public interface KtSymbolContainingDeclarationProviderMixIn : KtAnalysisSessionMixIn {
    /**
     * Returns containing declaration for symbol:
     *   for top-level declarations returns null
     *   for class members returns containing class
     *   for local declaration returns declaration it was declared it
     */
    public fun KtSymbol.getContainingSymbol(): KtDeclarationSymbol? =
        withValidityAssertion { analysisSession.containingDeclarationProvider.getContainingDeclaration(this) }

    public fun KtSymbol.getContainingFile(): KtFileSymbol? =
        withValidityAssertion { analysisSession.containingDeclarationProvider.getContainingFile(this) }

    /**
     * Returns containing class's [JvmClassName]
     *
     *   even for deserialized callables!
     *   for regular, non-local callables from source, it is a mere conversion of [ClassId] inside [CallableId]
     */
    public fun KtCallableSymbol.getContainingJvmClassName(): JvmClassName? =
        withValidityAssertion { analysisSession.containingDeclarationProvider.getContainingJvmClassName(this) }

    public fun KtSymbol.getContainingModule(): KtModule =
        withValidityAssertion { analysisSession.containingDeclarationProvider.getContainingModule(this) }
}