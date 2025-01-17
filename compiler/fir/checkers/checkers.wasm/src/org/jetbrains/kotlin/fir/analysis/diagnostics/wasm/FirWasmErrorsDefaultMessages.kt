/*
 * Copyright 2010-2023 JetBrains s.r.o. and Kotlin Programming Language contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the license/LICENSE.txt file.
 */

package org.jetbrains.kotlin.fir.analysis.diagnostics.wasm

import org.jetbrains.kotlin.diagnostics.KtDiagnosticFactoryToRendererMap
import org.jetbrains.kotlin.diagnostics.KtDiagnosticRenderers
import org.jetbrains.kotlin.diagnostics.KtDiagnosticRenderers.TO_STRING
import org.jetbrains.kotlin.diagnostics.rendering.BaseDiagnosticRendererFactory
import org.jetbrains.kotlin.diagnostics.rendering.CommonRenderers
import org.jetbrains.kotlin.fir.analysis.diagnostics.FirDiagnosticRenderers
import org.jetbrains.kotlin.fir.analysis.diagnostics.wasm.FirWasmErrors.CALL_TO_DEFINED_EXTERNALLY_FROM_NON_EXTERNAL_DECLARATION
import org.jetbrains.kotlin.fir.analysis.diagnostics.wasm.FirWasmErrors.EXTERNAL_TYPE_EXTENDS_NON_EXTERNAL_TYPE
import org.jetbrains.kotlin.fir.analysis.diagnostics.wasm.FirWasmErrors.JS_AND_WASM_EXPORTS_ON_SAME_DECLARATION
import org.jetbrains.kotlin.fir.analysis.diagnostics.wasm.FirWasmErrors.NESTED_WASM_EXPORT
import org.jetbrains.kotlin.fir.analysis.diagnostics.wasm.FirWasmErrors.NESTED_WASM_IMPORT
import org.jetbrains.kotlin.fir.analysis.diagnostics.wasm.FirWasmErrors.NON_EXTERNAL_TYPE_EXTENDS_EXTERNAL_TYPE
import org.jetbrains.kotlin.fir.analysis.diagnostics.wasm.FirWasmErrors.WASM_EXPORT_ON_EXTERNAL_DECLARATION
import org.jetbrains.kotlin.fir.analysis.diagnostics.wasm.FirWasmErrors.WASM_IMPORT_EXPORT_PARAMETER_DEFAULT_VALUE
import org.jetbrains.kotlin.fir.analysis.diagnostics.wasm.FirWasmErrors.WASM_IMPORT_EXPORT_UNSUPPORTED_PARAMETER_TYPE
import org.jetbrains.kotlin.fir.analysis.diagnostics.wasm.FirWasmErrors.WASM_IMPORT_EXPORT_UNSUPPORTED_RETURN_TYPE
import org.jetbrains.kotlin.fir.analysis.diagnostics.wasm.FirWasmErrors.WASM_IMPORT_EXPORT_VARARG_PARAMETER
import org.jetbrains.kotlin.fir.analysis.diagnostics.wasm.FirWasmErrors.WASM_IMPORT_ON_NON_EXTERNAL_DECLARATION
import org.jetbrains.kotlin.fir.analysis.diagnostics.wasm.FirWasmErrors.WRONG_JS_INTEROP_TYPE

@Suppress("unused")
object FirWasmErrorsDefaultMessages : BaseDiagnosticRendererFactory() {
    override val MAP = KtDiagnosticFactoryToRendererMap("FIR").also { map ->
        map.put(
            NON_EXTERNAL_TYPE_EXTENDS_EXTERNAL_TYPE,
            "Non-external type extends external type ''{0}''",
            FirDiagnosticRenderers.RENDER_TYPE
        )
        map.put(
            EXTERNAL_TYPE_EXTENDS_NON_EXTERNAL_TYPE,
            "External type extends non-external type ''{0}''",
            FirDiagnosticRenderers.RENDER_TYPE
        )
        map.put(CALL_TO_DEFINED_EXTERNALLY_FROM_NON_EXTERNAL_DECLARATION, "This property can only be used from external declarations.")
        map.put(
            WRONG_JS_INTEROP_TYPE,
            "Type ''{0}'' cannot be used in {1}. Only external, primitive, string and function types are supported in Kotlin/Wasm JS interop.",
            TO_STRING, FirDiagnosticRenderers.RENDER_TYPE,
        )

        map.put(NESTED_WASM_EXPORT, "Only top-level functions can be exported with '@WasmExport'.")
        map.put(WASM_EXPORT_ON_EXTERNAL_DECLARATION, "Functions annotated with '@WasmExport' must not be external.")
        map.put(JS_AND_WASM_EXPORTS_ON_SAME_DECLARATION, "Cannot use '@WasmExport' and '@JsExport' for same function.")
        map.put(NESTED_WASM_IMPORT, "Only top-level functions can be imported with '@WasmImport'.")
        map.put(WASM_IMPORT_ON_NON_EXTERNAL_DECLARATION, "Functions annotated with '@WasmImport' must be external.")
        map.put(WASM_IMPORT_EXPORT_PARAMETER_DEFAULT_VALUE, "Default parameter values are not supported with '@WasmImport' and '@WasmExport'.")
        map.put(WASM_IMPORT_EXPORT_VARARG_PARAMETER, "Vararg parameters are not supported with '@WasmImport' and '@WasmExport'.")
        map.put(
            WASM_IMPORT_EXPORT_UNSUPPORTED_PARAMETER_TYPE,
            "Unsupported ''@WasmImport'' and ''@WasmExport'' parameter type ''{0}''.",
            FirDiagnosticRenderers.RENDER_TYPE
        )
        map.put(
            WASM_IMPORT_EXPORT_UNSUPPORTED_RETURN_TYPE,
            "Unsupported ''@WasmImport'' and ''@WasmExport'' return type ''{0}''.",
            FirDiagnosticRenderers.RENDER_TYPE
        )
    }
}
