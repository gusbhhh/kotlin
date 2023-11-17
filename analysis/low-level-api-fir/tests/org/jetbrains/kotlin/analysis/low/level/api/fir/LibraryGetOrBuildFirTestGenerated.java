/*
 * Copyright 2010-2023 JetBrains s.r.o. and Kotlin Programming Language contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the license/LICENSE.txt file.
 */

package org.jetbrains.kotlin.analysis.low.level.api.fir;

import com.intellij.testFramework.TestDataPath;
import org.jetbrains.kotlin.test.util.KtTestUtil;
import org.jetbrains.kotlin.test.TestMetadata;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.regex.Pattern;

/** This class is generated by {@link org.jetbrains.kotlin.generators.tests.analysis.api.GenerateAnalysisApiTestsKt}. DO NOT MODIFY MANUALLY */
@SuppressWarnings("all")
@TestMetadata("analysis/low-level-api-fir/testData/getOrBuildFirBinary")
@TestDataPath("$PROJECT_ROOT")
public class LibraryGetOrBuildFirTestGenerated extends AbstractLibraryGetOrBuildFirTest {
    @Test
    public void testAllFilesPresentInGetOrBuildFirBinary() throws Exception {
        KtTestUtil.assertAllTestsPresentByMetadataWithExcluded(this.getClass(), new File("analysis/low-level-api-fir/testData/getOrBuildFirBinary"), Pattern.compile("^(.+)\\.kt$"), null, true);
    }

    @Test
    @TestMetadata("enumEntry.kt")
    public void testEnumEntry() throws Exception {
        runTest("analysis/low-level-api-fir/testData/getOrBuildFirBinary/enumEntry.kt");
    }

    @Test
    @TestMetadata("functionWithDefinitelyNotNullParameter.kt")
    public void testFunctionWithDefinitelyNotNullParameter() throws Exception {
        runTest("analysis/low-level-api-fir/testData/getOrBuildFirBinary/functionWithDefinitelyNotNullParameter.kt");
    }

    @Test
    @TestMetadata("parameter.kt")
    public void testParameter() throws Exception {
        runTest("analysis/low-level-api-fir/testData/getOrBuildFirBinary/parameter.kt");
    }

    @Test
    @TestMetadata("secondaryConstructor.kt")
    public void testSecondaryConstructor() throws Exception {
        runTest("analysis/low-level-api-fir/testData/getOrBuildFirBinary/secondaryConstructor.kt");
    }

    @Test
    @TestMetadata("simpleClass.kt")
    public void testSimpleClass() throws Exception {
        runTest("analysis/low-level-api-fir/testData/getOrBuildFirBinary/simpleClass.kt");
    }

    @Test
    @TestMetadata("simpleConstructor.kt")
    public void testSimpleConstructor() throws Exception {
        runTest("analysis/low-level-api-fir/testData/getOrBuildFirBinary/simpleConstructor.kt");
    }

    @Test
    @TestMetadata("simpleFunction.kt")
    public void testSimpleFunction() throws Exception {
        runTest("analysis/low-level-api-fir/testData/getOrBuildFirBinary/simpleFunction.kt");
    }

    @Test
    @TestMetadata("simpleProperty.kt")
    public void testSimpleProperty() throws Exception {
        runTest("analysis/low-level-api-fir/testData/getOrBuildFirBinary/simpleProperty.kt");
    }

    @Test
    @TestMetadata("topLevelFunction.kt")
    public void testTopLevelFunction() throws Exception {
        runTest("analysis/low-level-api-fir/testData/getOrBuildFirBinary/topLevelFunction.kt");
    }

    @Test
    @TestMetadata("typeParameter.kt")
    public void testTypeParameter() throws Exception {
        runTest("analysis/low-level-api-fir/testData/getOrBuildFirBinary/typeParameter.kt");
    }

    @Nested
    @TestMetadata("analysis/low-level-api-fir/testData/getOrBuildFirBinary/js")
    @TestDataPath("$PROJECT_ROOT")
    public class Js {
        @Test
        public void testAllFilesPresentInJs() throws Exception {
            KtTestUtil.assertAllTestsPresentByMetadataWithExcluded(this.getClass(), new File("analysis/low-level-api-fir/testData/getOrBuildFirBinary/js"), Pattern.compile("^(.+)\\.kt$"), null, true);
        }

        @Test
        @TestMetadata("classAnnotation.kt")
        public void testClassAnnotation() throws Exception {
            runTest("analysis/low-level-api-fir/testData/getOrBuildFirBinary/js/classAnnotation.kt");
        }

        @Test
        @TestMetadata("constructorAnnotation.kt")
        public void testConstructorAnnotation() throws Exception {
            runTest("analysis/low-level-api-fir/testData/getOrBuildFirBinary/js/constructorAnnotation.kt");
        }

        @Test
        @TestMetadata("dynamic.kt")
        public void testDynamic() throws Exception {
            runTest("analysis/low-level-api-fir/testData/getOrBuildFirBinary/js/dynamic.kt");
        }

        @Test
        @TestMetadata("enumAnnotation.kt")
        public void testEnumAnnotation() throws Exception {
            runTest("analysis/low-level-api-fir/testData/getOrBuildFirBinary/js/enumAnnotation.kt");
        }

        @Test
        @TestMetadata("fileJsModule.kt")
        public void testFileJsModule() throws Exception {
            runTest("analysis/low-level-api-fir/testData/getOrBuildFirBinary/js/fileJsModule.kt");
        }

        @Test
        @TestMetadata("functionAnnotation.kt")
        public void testFunctionAnnotation() throws Exception {
            runTest("analysis/low-level-api-fir/testData/getOrBuildFirBinary/js/functionAnnotation.kt");
        }

        @Test
        @TestMetadata("jQueryExample.kt")
        public void testJQueryExample() throws Exception {
            runTest("analysis/low-level-api-fir/testData/getOrBuildFirBinary/js/jQueryExample.kt");
        }

        @Test
        @TestMetadata("parameterAnnotation.kt")
        public void testParameterAnnotation() throws Exception {
            runTest("analysis/low-level-api-fir/testData/getOrBuildFirBinary/js/parameterAnnotation.kt");
        }

        @Test
        @TestMetadata("propertyAnnotation.kt")
        public void testPropertyAnnotation() throws Exception {
            runTest("analysis/low-level-api-fir/testData/getOrBuildFirBinary/js/propertyAnnotation.kt");
        }

        @Test
        @TestMetadata("typeAnnotation.kt")
        public void testTypeAnnotation() throws Exception {
            runTest("analysis/low-level-api-fir/testData/getOrBuildFirBinary/js/typeAnnotation.kt");
        }

        @Test
        @TestMetadata("typeParameterAnnotation.kt")
        public void testTypeParameterAnnotation() throws Exception {
            runTest("analysis/low-level-api-fir/testData/getOrBuildFirBinary/js/typeParameterAnnotation.kt");
        }
    }

    @Nested
    @TestMetadata("analysis/low-level-api-fir/testData/getOrBuildFirBinary/publishedApi")
    @TestDataPath("$PROJECT_ROOT")
    public class PublishedApi {
        @Test
        public void testAllFilesPresentInPublishedApi() throws Exception {
            KtTestUtil.assertAllTestsPresentByMetadataWithExcluded(this.getClass(), new File("analysis/low-level-api-fir/testData/getOrBuildFirBinary/publishedApi"), Pattern.compile("^(.+)\\.kt$"), null, true);
        }

        @Test
        @TestMetadata("publishedApiClass.kt")
        public void testPublishedApiClass() throws Exception {
            runTest("analysis/low-level-api-fir/testData/getOrBuildFirBinary/publishedApi/publishedApiClass.kt");
        }

        @Test
        @TestMetadata("publishedApiConstructor.kt")
        public void testPublishedApiConstructor() throws Exception {
            runTest("analysis/low-level-api-fir/testData/getOrBuildFirBinary/publishedApi/publishedApiConstructor.kt");
        }

        @Test
        @TestMetadata("publishedApiFunction.kt")
        public void testPublishedApiFunction() throws Exception {
            runTest("analysis/low-level-api-fir/testData/getOrBuildFirBinary/publishedApi/publishedApiFunction.kt");
        }

        @Test
        @TestMetadata("publishedApiPrimaryConstructor.kt")
        public void testPublishedApiPrimaryConstructor() throws Exception {
            runTest("analysis/low-level-api-fir/testData/getOrBuildFirBinary/publishedApi/publishedApiPrimaryConstructor.kt");
        }

        @Test
        @TestMetadata("publishedApiProperty.kt")
        public void testPublishedApiProperty() throws Exception {
            runTest("analysis/low-level-api-fir/testData/getOrBuildFirBinary/publishedApi/publishedApiProperty.kt");
        }

        @Test
        @TestMetadata("publishedApiPropertyGetter.kt")
        public void testPublishedApiPropertyGetter() throws Exception {
            runTest("analysis/low-level-api-fir/testData/getOrBuildFirBinary/publishedApi/publishedApiPropertyGetter.kt");
        }

        @Test
        @TestMetadata("publishedApiPropertySetter.kt")
        public void testPublishedApiPropertySetter() throws Exception {
            runTest("analysis/low-level-api-fir/testData/getOrBuildFirBinary/publishedApi/publishedApiPropertySetter.kt");
        }
    }
}
