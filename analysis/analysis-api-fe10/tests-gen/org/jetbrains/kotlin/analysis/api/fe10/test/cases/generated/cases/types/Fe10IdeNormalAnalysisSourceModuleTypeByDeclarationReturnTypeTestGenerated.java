/*
 * Copyright 2010-2023 JetBrains s.r.o. and Kotlin Programming Language contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the license/LICENSE.txt file.
 */

package org.jetbrains.kotlin.analysis.api.fe10.test.cases.generated.cases.types;

import com.intellij.testFramework.TestDataPath;
import org.jetbrains.kotlin.test.util.KtTestUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.kotlin.analysis.api.fe10.test.configurator.AnalysisApiFe10TestConfiguratorFactory;
import org.jetbrains.kotlin.analysis.test.framework.test.configurators.AnalysisApiTestConfiguratorFactoryData;
import org.jetbrains.kotlin.analysis.test.framework.test.configurators.AnalysisApiTestConfigurator;
import org.jetbrains.kotlin.analysis.test.framework.test.configurators.TestModuleKind;
import org.jetbrains.kotlin.analysis.test.framework.test.configurators.FrontendKind;
import org.jetbrains.kotlin.analysis.test.framework.test.configurators.AnalysisSessionMode;
import org.jetbrains.kotlin.analysis.test.framework.test.configurators.AnalysisApiMode;
import org.jetbrains.kotlin.analysis.api.impl.base.test.cases.types.AbstractTypeByDeclarationReturnTypeTest;
import org.jetbrains.kotlin.test.TestMetadata;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.regex.Pattern;

/** This class is generated by {@link org.jetbrains.kotlin.generators.tests.analysis.api.GenerateAnalysisApiTestsKt}. DO NOT MODIFY MANUALLY */
@SuppressWarnings("all")
@TestMetadata("analysis/analysis-api/testData/types/byDeclarationReturnType")
@TestDataPath("$PROJECT_ROOT")
public class Fe10IdeNormalAnalysisSourceModuleTypeByDeclarationReturnTypeTestGenerated extends AbstractTypeByDeclarationReturnTypeTest {
    @NotNull
    @Override
    public AnalysisApiTestConfigurator getConfigurator() {
        return AnalysisApiFe10TestConfiguratorFactory.INSTANCE.createConfigurator(
            new AnalysisApiTestConfiguratorFactoryData(
                FrontendKind.Fe10,
                TestModuleKind.Source,
                AnalysisSessionMode.Normal,
                AnalysisApiMode.Ide
            )
        );
    }

    @Test
    public void testAllFilesPresentInByDeclarationReturnType() throws Exception {
        KtTestUtil.assertAllTestsPresentByMetadataWithExcluded(this.getClass(), new File("analysis/analysis-api/testData/types/byDeclarationReturnType"), Pattern.compile("^(.+)\\.kt$"), null, true);
    }

    @Test
    @TestMetadata("localClassType.kt")
    public void testLocalClassType() throws Exception {
        runTest("analysis/analysis-api/testData/types/byDeclarationReturnType/localClassType.kt");
    }

    @Test
    @TestMetadata("localClassWithTypeArgumentsType.kt")
    public void testLocalClassWithTypeArgumentsType() throws Exception {
        runTest("analysis/analysis-api/testData/types/byDeclarationReturnType/localClassWithTypeArgumentsType.kt");
    }

    @Test
    @TestMetadata("localNestedClassType.kt")
    public void testLocalNestedClassType() throws Exception {
        runTest("analysis/analysis-api/testData/types/byDeclarationReturnType/localNestedClassType.kt");
    }

    @Test
    @TestMetadata("typeAliasForFunctionalType1.kt")
    public void testTypeAliasForFunctionalType1() throws Exception {
        runTest("analysis/analysis-api/testData/types/byDeclarationReturnType/typeAliasForFunctionalType1.kt");
    }
}
