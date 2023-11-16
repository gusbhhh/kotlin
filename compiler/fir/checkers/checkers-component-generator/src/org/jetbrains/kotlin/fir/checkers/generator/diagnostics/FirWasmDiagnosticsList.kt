/*
 * Copyright 2010-2023 JetBrains s.r.o. and Kotlin Programming Language contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the license/LICENSE.txt file.
 */

package org.jetbrains.kotlin.fir.checkers.generator.diagnostics

import com.intellij.psi.PsiElement
import org.jetbrains.kotlin.fir.checkers.generator.diagnostics.model.DiagnosticList
import org.jetbrains.kotlin.fir.checkers.generator.diagnostics.model.PositioningStrategy
import org.jetbrains.kotlin.fir.types.ConeKotlinType
import org.jetbrains.kotlin.psi.*
import org.jetbrains.kotlin.util.PrivateForInline

@Suppress("ClassName", "unused")
@OptIn(PrivateForInline::class)
object WASM_DIAGNOSTICS_LIST : DiagnosticList("FirWasmErrors") {
    val EXTERNALS by object : DiagnosticGroup("Externals") {
        val NON_EXTERNAL_TYPE_EXTENDS_EXTERNAL_TYPE by error<KtElement>(PositioningStrategy.DECLARATION_SIGNATURE_OR_DEFAULT) {
            parameter<ConeKotlinType>("superType")
        }
        val EXTERNAL_TYPE_EXTENDS_NON_EXTERNAL_TYPE by error<KtElement>(PositioningStrategy.DECLARATION_SIGNATURE_OR_DEFAULT) {
            parameter<ConeKotlinType>("superType")
        }
        val CALL_TO_DEFINED_EXTERNALLY_FROM_NON_EXTERNAL_DECLARATION by error<PsiElement>()
        val WRONG_JS_INTEROP_TYPE by error<KtElement>(PositioningStrategy.DECLARATION_SIGNATURE_OR_DEFAULT) {
            parameter<String>("place")
            parameter<ConeKotlinType>("type")
        }
    }

    val JSCODE by object : DiagnosticGroup("JsCode") {
        val JSCODE_WRONG_CONTEXT by error<KtElement>(PositioningStrategy.NAME_IDENTIFIER)
        val JSCODE_UNSUPPORTED_FUNCTION_KIND by error<KtElement>(PositioningStrategy.NAME_IDENTIFIER) {
            parameter<String>("place")
        }
        val JSCODE_INVALID_PARAMETER_NAME by error<KtElement>()
    }

    val WASM_INTEROP by object : DiagnosticGroup("Wasm interop") {
        val NESTED_WASM_EXPORT by error<KtElement>()
        val WASM_EXPORT_ON_EXTERNAL_DECLARATION by error<KtElement>()
        val JS_AND_WASM_EXPORTS_ON_SAME_DECLARATION by error<KtElement>()
        val NESTED_WASM_IMPORT by error<KtElement>()
        val WASM_IMPORT_ON_NON_EXTERNAL_DECLARATION by error<KtElement>()
        val WASM_IMPORT_EXPORT_PARAMETER_DEFAULT_VALUE by error<KtElement>()
        val WASM_IMPORT_EXPORT_VARARG_PARAMETER by error<KtElement>()
        val WASM_IMPORT_EXPORT_UNSUPPORTED_PARAMETER_TYPE by error<KtElement>(PositioningStrategy.DECLARATION_SIGNATURE_OR_DEFAULT) {
            parameter<ConeKotlinType>("type")
        }
        val WASM_IMPORT_EXPORT_UNSUPPORTED_RETURN_TYPE by error<KtElement>(PositioningStrategy.DECLARATION_SIGNATURE_OR_DEFAULT) {
            parameter<ConeKotlinType>("type")
        }
    }
}
