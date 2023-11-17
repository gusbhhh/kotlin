/*
 * Copyright (C) 2020-2023 Brian Norman
 * Copyright 2023 JetBrains s.r.o. and Kotlin Programming Language contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the license/LICENSE.txt file.
 */

package org.jetbrains.kotlinx.powerassert.gradle

open class PowerAssertGradleExtension {
    var functions: List<String> = listOf("kotlin.assert")
    var excludedSourceSets: List<String> = listOf()
}
