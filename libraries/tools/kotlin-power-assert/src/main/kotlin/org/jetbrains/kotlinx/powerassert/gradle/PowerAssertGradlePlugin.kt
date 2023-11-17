/*
 * Copyright (C) 2020-2023 Brian Norman
 * Copyright 2023 JetBrains s.r.o. and Kotlin Programming Language contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the license/LICENSE.txt file.
 */

package org.jetbrains.kotlinx.powerassert.gradle

import org.gradle.api.Project
import org.gradle.api.provider.Provider
import org.jetbrains.kotlin.gradle.plugin.KotlinCompilation
import org.jetbrains.kotlin.gradle.plugin.KotlinCompilerPluginSupportPlugin
import org.jetbrains.kotlin.gradle.plugin.SubpluginArtifact
import org.jetbrains.kotlin.gradle.plugin.SubpluginOption

class PowerAssertGradlePlugin : KotlinCompilerPluginSupportPlugin {
    override fun apply(target: Project): Unit = with(target) {
        extensions.create("kotlinPowerAssert", PowerAssertGradleExtension::class.java)
    }

    override fun isApplicable(kotlinCompilation: KotlinCompilation<*>): Boolean {
        val project = kotlinCompilation.target.project
        val extension = project.extensions.getByType(PowerAssertGradleExtension::class.java)
        return extension.excludedSourceSets.none { it == kotlinCompilation.defaultSourceSet.name }
    }

    override fun getCompilerPluginId(): String = "com.bnorm.kotlin-power-assert"

    override fun getPluginArtifact(): SubpluginArtifact = SubpluginArtifact(
        groupId = BuildConfig.PLUGIN_GROUP_ID,
        artifactId = BuildConfig.PLUGIN_ARTIFACT_ID,
        version = BuildConfig.PLUGIN_VERSION,
    )

    override fun applyToCompilation(
        kotlinCompilation: KotlinCompilation<*>,
    ): Provider<List<SubpluginOption>> {
        val project = kotlinCompilation.target.project
        val extension = project.extensions.getByType(PowerAssertGradleExtension::class.java)
        return project.provider {
            extension.functions.map {
                SubpluginOption(key = "function", value = it)
            }
        }
    }
}
