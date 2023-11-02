/*
 * Copyright 2010-2023 JetBrains s.r.o. and Kotlin Programming Language contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the license/LICENSE.txt file.
 */
package kotlin.js

// There was a problem with per-module compilation (KT-55758) when the top-level state (globalInterfaceId) was reinitialized during stdlib module initialization
// As a result we miss already incremented globalInterfaceId and had the same interfaceIds in two different modules
// So, to keep the state consistent it was moved into the variable without initializer and function
@Suppress("MUST_BE_INITIALIZED")
private var globalInterfaceId: dynamic

private fun generateInterfaceId(): Int {
    if (globalInterfaceId === VOID) {
        globalInterfaceId = 0
    }
    globalInterfaceId = globalInterfaceId.unsafeCast<Int>() + 1
    return globalInterfaceId.unsafeCast<Int>()
}

internal fun setMetadataFor(
    ctor: Ctor,
    name: String?,
    defaultConstructor: dynamic,
    parent: Ctor?,
    interfaces: Array<dynamic>?,
    addInterfaceId: Boolean?,
    suspendArity: Array<Int>?,
    associatedObjectKey: Number?,
    associatedObjects: dynamic
) {
    if (parent != null) {
        js("""
          ctor.prototype = Object.create(parent.prototype)
          ctor.prototype.constructor = ctor;
        """)
    }

    val interfaceId = if (addInterfaceId == true) generateInterfaceId() else VOID
    val metadata = createMetadata(name, defaultConstructor, associatedObjectKey, associatedObjects, suspendArity, interfaceId)
    ctor.`$metadata$` = metadata

    if (interfaces != null) {
        val receiver = if (metadata.interfaceId != VOID) ctor else ctor.prototype
        receiver.`$imask$` = implement(interfaces)
    }
}

internal fun setMetadataForLambda(ctor: Ctor, parent: Ctor?, interfaces: Array<dynamic>?, suspendArity: Array<Int>?) {
    setMetadataFor(ctor, "Lambda", VOID, parent, interfaces, false, suspendArity, VOID, VOID)
}

internal fun setMetadataForFunctionReference(ctor: Ctor, parent: Ctor?, interfaces: Array<dynamic>?, suspendArity: Array<Int>?) {
    setMetadataFor(ctor, "FunctionReference", VOID, parent, interfaces, false, suspendArity, VOID, VOID)
}

internal fun setMetadataForCoroutine(ctor: Ctor, parent: Ctor?, interfaces: Array<dynamic>?, suspendArity: Array<Int>?) {
    setMetadataFor(ctor, "Coroutine", VOID, parent, interfaces, false, suspendArity, VOID, VOID)
}

internal fun setMetadataForCompanion(ctor: Ctor, parent: Ctor?, interfaces: Array<dynamic>?, suspendArity: Array<Int>?) {
    setMetadataFor(ctor, "Companion", VOID, parent, interfaces, false, suspendArity, VOID, VOID)
}

// Seems like we need to disable this check if variables are used inside js annotation
@Suppress("UNUSED_PARAMETER", "UNUSED_VARIABLE")
internal fun createMetadata(
    name: String?,
    defaultConstructor: dynamic,
    associatedObjectKey: Number?,
    associatedObjects: dynamic,
    suspendArity: Array<Int>?,
    interfaceId: Int?
): Metadata {
    val undef = VOID
    return js("""({
    simpleName: name,
    associatedObjectKey: associatedObjectKey,
    associatedObjects: associatedObjects,
    suspendArity: suspendArity,
    ${'$'}kClass$: undef,
    defaultConstructor: defaultConstructor,
    interfaceId: interfaceId
})""")
}

internal external interface Metadata {
    // This field gives fast access to the prototype of metadata owner (Object.getPrototypeOf())
    // Can be pre-initialized or lazy initialized and then should be immutable
    val simpleName: String?
    val associatedObjectKey: Number?
    val associatedObjects: dynamic
    val suspendArity: Array<Int>?
    val interfaceId: Int?

    var `$kClass$`: dynamic
    val defaultConstructor: dynamic

    var errorInfo: Int? // Bits set for overridden properties: "message" => 0x1, "cause" => 0x2
}
