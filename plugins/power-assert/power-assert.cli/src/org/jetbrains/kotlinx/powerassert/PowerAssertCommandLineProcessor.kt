/*
 * Copyright 2010-2023 JetBrains s.r.o. and Kotlin Programming Language contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the license/LICENSE.txt file.
 */

package org.jetbrains.kotlinx.powerassert

import org.jetbrains.kotlin.compiler.plugin.AbstractCliOption
import org.jetbrains.kotlin.compiler.plugin.CliOption
import org.jetbrains.kotlin.compiler.plugin.CommandLineProcessor
import org.jetbrains.kotlin.config.CompilerConfiguration

class PowerAssertCommandLineProcessor : CommandLineProcessor {
    override val pluginId: String = "com.bnorm.kotlin-power-assert"

    override val pluginOptions: Collection<CliOption> = listOf(
        CliOption(
            optionName = "function",
            valueDescription = "function full-qualified name",
            description = "fully qualified path of function to intercept",
            required = false, // TODO required for Kotlin/JS
            allowMultipleOccurrences = true,
        ),
    )

    override fun processOption(
        option: AbstractCliOption,
        value: String,
        configuration: CompilerConfiguration,
    ) {
        return when (option.optionName) {
            "function" -> configuration.add(KEY_FUNCTIONS, value)
            else -> error("Unexpected config option ${option.optionName}")
        }
    }
}
