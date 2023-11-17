/*
 * Copyright 2010-2023 JetBrains s.r.o. and Kotlin Programming Language contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the license/LICENSE.txt file.
 */

package org.jetbrains.kotlinx.powerassert

import com.google.auto.service.AutoService
import org.jetbrains.kotlin.backend.common.extensions.IrGenerationExtension
import org.jetbrains.kotlin.cli.common.CLIConfigurationKeys
import org.jetbrains.kotlin.cli.common.messages.MessageCollector
import org.jetbrains.kotlin.compiler.plugin.CompilerPluginRegistrar
import org.jetbrains.kotlin.config.CompilerConfiguration
import org.jetbrains.kotlin.config.CompilerConfigurationKey
import org.jetbrains.kotlin.name.FqName

val KEY_FUNCTIONS = CompilerConfigurationKey<List<String>>("fully-qualified function names")

@AutoService(CompilerPluginRegistrar::class)
class PowerAssertCompilerPluginRegistrar(
    private val functions: Set<FqName>,
) : CompilerPluginRegistrar() {
    @Suppress("unused")
    constructor() : this(emptySet()) // Used by service loader

    override val supportsK2: Boolean = true

    override fun ExtensionStorage.registerExtensions(configuration: CompilerConfiguration) {
        val functions = configuration[KEY_FUNCTIONS]?.map { FqName(it) } ?: functions
        if (functions.isEmpty()) return

        val messageCollector = configuration.get(CLIConfigurationKeys.MESSAGE_COLLECTOR_KEY, MessageCollector.NONE)
        IrGenerationExtension.registerExtension(PowerAssertIrGenerationExtension(messageCollector, functions.toSet()))
    }
}
