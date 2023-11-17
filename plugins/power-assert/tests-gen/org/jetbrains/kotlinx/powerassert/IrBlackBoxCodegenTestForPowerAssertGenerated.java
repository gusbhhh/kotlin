/*
 * Copyright 2010-2023 JetBrains s.r.o. and Kotlin Programming Language contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the license/LICENSE.txt file.
 */

package org.jetbrains.kotlinx.powerassert;

import com.intellij.testFramework.TestDataPath;
import org.jetbrains.kotlin.test.util.KtTestUtil;
import org.jetbrains.kotlin.test.TargetBackend;
import org.jetbrains.kotlin.test.TestMetadata;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.regex.Pattern;

/** This class is generated by {@link org.jetbrains.kotlin.generators.tests.GenerateTestsKt}. DO NOT MODIFY MANUALLY */
@SuppressWarnings("all")
@TestMetadata("plugins/power-assert/testData/codegen")
@TestDataPath("$PROJECT_ROOT")
public class IrBlackBoxCodegenTestForPowerAssertGenerated extends AbstractIrBlackBoxCodegenTestForPowerAssert {
    @Test
    public void testAllFilesPresentInCodegen() throws Exception {
        KtTestUtil.assertAllTestsPresentByMetadataWithExcluded(this.getClass(), new File("plugins/power-assert/testData/codegen"), Pattern.compile("^(.+)\\.kt$"), Pattern.compile("^(.+)\\.fir\\.kts?$"), TargetBackend.JVM_IR, true);
    }

    @Test
    @TestMetadata("CheckCustomMessage.kt")
    public void testCheckCustomMessage() throws Exception {
        runTest("plugins/power-assert/testData/codegen/CheckCustomMessage.kt");
    }

    @Test
    @TestMetadata("Constants.kt")
    public void testConstants() throws Exception {
        runTest("plugins/power-assert/testData/codegen/Constants.kt");
    }

    @Test
    @TestMetadata("CustomLocalVariableMessage.kt")
    public void testCustomLocalVariableMessage() throws Exception {
        runTest("plugins/power-assert/testData/codegen/CustomLocalVariableMessage.kt");
    }

    @Test
    @TestMetadata("CustomMessage.kt")
    public void testCustomMessage() throws Exception {
        runTest("plugins/power-assert/testData/codegen/CustomMessage.kt");
    }

    @Test
    @TestMetadata("MemberFunctions.kt")
    public void testMemberFunctions() throws Exception {
        runTest("plugins/power-assert/testData/codegen/MemberFunctions.kt");
    }

    @Test
    @TestMetadata("Multiline.kt")
    public void testMultiline() throws Exception {
        runTest("plugins/power-assert/testData/codegen/Multiline.kt");
    }

    @Test
    @TestMetadata("RequireCustomMessage.kt")
    public void testRequireCustomMessage() throws Exception {
        runTest("plugins/power-assert/testData/codegen/RequireCustomMessage.kt");
    }

    @Test
    @TestMetadata("Transformations.kt")
    public void testTransformations() throws Exception {
        runTest("plugins/power-assert/testData/codegen/Transformations.kt");
    }

    @Nested
    @TestMetadata("plugins/power-assert/testData/codegen/arithmetic")
    @TestDataPath("$PROJECT_ROOT")
    public class Arithmetic {
        @Test
        public void testAllFilesPresentInArithmetic() throws Exception {
            KtTestUtil.assertAllTestsPresentByMetadataWithExcluded(this.getClass(), new File("plugins/power-assert/testData/codegen/arithmetic"), Pattern.compile("^(.+)\\.kt$"), Pattern.compile("^(.+)\\.fir\\.kts?$"), TargetBackend.JVM_IR, true);
        }

        @Test
        @TestMetadata("InlineAddition.kt")
        public void testInlineAddition() throws Exception {
            runTest("plugins/power-assert/testData/codegen/arithmetic/InlineAddition.kt");
        }

        @Test
        @TestMetadata("InlineDivision.kt")
        public void testInlineDivision() throws Exception {
            runTest("plugins/power-assert/testData/codegen/arithmetic/InlineDivision.kt");
        }

        @Test
        @TestMetadata("InlineMultiplication.kt")
        public void testInlineMultiplication() throws Exception {
            runTest("plugins/power-assert/testData/codegen/arithmetic/InlineMultiplication.kt");
        }

        @Test
        @TestMetadata("InlinePostfixDecrement.kt")
        public void testInlinePostfixDecrement() throws Exception {
            runTest("plugins/power-assert/testData/codegen/arithmetic/InlinePostfixDecrement.kt");
        }

        @Test
        @TestMetadata("InlinePostfixIncrement.kt")
        public void testInlinePostfixIncrement() throws Exception {
            runTest("plugins/power-assert/testData/codegen/arithmetic/InlinePostfixIncrement.kt");
        }

        @Test
        @TestMetadata("InlinePrefixDecrement.kt")
        public void testInlinePrefixDecrement() throws Exception {
            runTest("plugins/power-assert/testData/codegen/arithmetic/InlinePrefixDecrement.kt");
        }

        @Test
        @TestMetadata("InlinePrefixIncrement.kt")
        public void testInlinePrefixIncrement() throws Exception {
            runTest("plugins/power-assert/testData/codegen/arithmetic/InlinePrefixIncrement.kt");
        }

        @Test
        @TestMetadata("InlineSubtraction.kt")
        public void testInlineSubtraction() throws Exception {
            runTest("plugins/power-assert/testData/codegen/arithmetic/InlineSubtraction.kt");
        }
    }

    @Nested
    @TestMetadata("plugins/power-assert/testData/codegen/boolean")
    @TestDataPath("$PROJECT_ROOT")
    public class Boolean {
        @Test
        public void testAllFilesPresentInBoolean() throws Exception {
            KtTestUtil.assertAllTestsPresentByMetadataWithExcluded(this.getClass(), new File("plugins/power-assert/testData/codegen/boolean"), Pattern.compile("^(.+)\\.kt$"), Pattern.compile("^(.+)\\.fir\\.kts?$"), TargetBackend.JVM_IR, true);
        }

        @Test
        @TestMetadata("BooleanAnd.kt")
        public void testBooleanAnd() throws Exception {
            runTest("plugins/power-assert/testData/codegen/boolean/BooleanAnd.kt");
        }

        @Test
        @TestMetadata("BooleanMixWithAndFirst.kt")
        public void testBooleanMixWithAndFirst() throws Exception {
            runTest("plugins/power-assert/testData/codegen/boolean/BooleanMixWithAndFirst.kt");
        }

        @Test
        @TestMetadata("BooleanMixWithAndLast.kt")
        public void testBooleanMixWithAndLast() throws Exception {
            runTest("plugins/power-assert/testData/codegen/boolean/BooleanMixWithAndLast.kt");
        }

        @Test
        @TestMetadata("BooleanMixWithOrFirst.kt")
        public void testBooleanMixWithOrFirst() throws Exception {
            runTest("plugins/power-assert/testData/codegen/boolean/BooleanMixWithOrFirst.kt");
        }

        @Test
        @TestMetadata("BooleanMixWithOrLast.kt")
        public void testBooleanMixWithOrLast() throws Exception {
            runTest("plugins/power-assert/testData/codegen/boolean/BooleanMixWithOrLast.kt");
        }

        @Test
        @TestMetadata("BooleanOr.kt")
        public void testBooleanOr() throws Exception {
            runTest("plugins/power-assert/testData/codegen/boolean/BooleanOr.kt");
        }

        @Test
        @TestMetadata("BooleanShortCircuit.kt")
        public void testBooleanShortCircuit() throws Exception {
            runTest("plugins/power-assert/testData/codegen/boolean/BooleanShortCircuit.kt");
        }
    }

    @Nested
    @TestMetadata("plugins/power-assert/testData/codegen/cast")
    @TestDataPath("$PROJECT_ROOT")
    public class Cast {
        @Test
        public void testAllFilesPresentInCast() throws Exception {
            KtTestUtil.assertAllTestsPresentByMetadataWithExcluded(this.getClass(), new File("plugins/power-assert/testData/codegen/cast"), Pattern.compile("^(.+)\\.kt$"), Pattern.compile("^(.+)\\.fir\\.kts?$"), TargetBackend.JVM_IR, true);
        }

        @Test
        @TestMetadata("InstanceEquals.kt")
        public void testInstanceEquals() throws Exception {
            runTest("plugins/power-assert/testData/codegen/cast/InstanceEquals.kt");
        }

        @Test
        @TestMetadata("InstanceNotEquals.kt")
        public void testInstanceNotEquals() throws Exception {
            runTest("plugins/power-assert/testData/codegen/cast/InstanceNotEquals.kt");
        }

        @Test
        @TestMetadata("SmartCast.kt")
        public void testSmartCast() throws Exception {
            runTest("plugins/power-assert/testData/codegen/cast/SmartCast.kt");
        }
    }

    @Nested
    @TestMetadata("plugins/power-assert/testData/codegen/dbg")
    @TestDataPath("$PROJECT_ROOT")
    public class Dbg {
        @Test
        public void testAllFilesPresentInDbg() throws Exception {
            KtTestUtil.assertAllTestsPresentByMetadataWithExcluded(this.getClass(), new File("plugins/power-assert/testData/codegen/dbg"), Pattern.compile("^(.+)\\.kt$"), Pattern.compile("^(.+)\\.fir\\.kts?$"), TargetBackend.JVM_IR, true);
        }

        @Test
        @TestMetadata("DebugEntryFunction.kt")
        public void testDebugEntryFunction() throws Exception {
            runTest("plugins/power-assert/testData/codegen/dbg/DebugEntryFunction.kt");
        }

        @Test
        @TestMetadata("DebugEntryFunctionComplexBooleans.kt")
        public void testDebugEntryFunctionComplexBooleans() throws Exception {
            runTest("plugins/power-assert/testData/codegen/dbg/DebugEntryFunctionComplexBooleans.kt");
        }

        @Test
        @TestMetadata("DebugEntryFunctionMessage.kt")
        public void testDebugEntryFunctionMessage() throws Exception {
            runTest("plugins/power-assert/testData/codegen/dbg/DebugEntryFunctionMessage.kt");
        }

        @Test
        @TestMetadata("DebugEntryFunctionMessageComplexBooleans.kt")
        public void testDebugEntryFunctionMessageComplexBooleans() throws Exception {
            runTest("plugins/power-assert/testData/codegen/dbg/DebugEntryFunctionMessageComplexBooleans.kt");
        }

        @Test
        @TestMetadata("DebugFunction.kt")
        public void testDebugFunction() throws Exception {
            runTest("plugins/power-assert/testData/codegen/dbg/DebugFunction.kt");
        }

        @Test
        @TestMetadata("DebugFunctionMessage.kt")
        public void testDebugFunctionMessage() throws Exception {
            runTest("plugins/power-assert/testData/codegen/dbg/DebugFunctionMessage.kt");
        }
    }

    @Nested
    @TestMetadata("plugins/power-assert/testData/codegen/infix")
    @TestDataPath("$PROJECT_ROOT")
    public class Infix {
        @Test
        public void testAllFilesPresentInInfix() throws Exception {
            KtTestUtil.assertAllTestsPresentByMetadataWithExcluded(this.getClass(), new File("plugins/power-assert/testData/codegen/infix"), Pattern.compile("^(.+)\\.kt$"), Pattern.compile("^(.+)\\.fir\\.kts?$"), TargetBackend.JVM_IR, true);
        }

        @Test
        @TestMetadata("DispatchInfix.kt")
        public void testDispatchInfix() throws Exception {
            runTest("plugins/power-assert/testData/codegen/infix/DispatchInfix.kt");
        }

        @Test
        @TestMetadata("DispatchInfixConstantParameter.kt")
        public void testDispatchInfixConstantParameter() throws Exception {
            runTest("plugins/power-assert/testData/codegen/infix/DispatchInfixConstantParameter.kt");
        }

        @Test
        @TestMetadata("DispatchInfixConstantReceiver.kt")
        public void testDispatchInfixConstantReceiver() throws Exception {
            runTest("plugins/power-assert/testData/codegen/infix/DispatchInfixConstantReceiver.kt");
        }

        @Test
        @TestMetadata("DispatchInfixOnlyConstants.kt")
        public void testDispatchInfixOnlyConstants() throws Exception {
            runTest("plugins/power-assert/testData/codegen/infix/DispatchInfixOnlyConstants.kt");
        }

        @Test
        @TestMetadata("DispatchNonInfix.kt")
        public void testDispatchNonInfix() throws Exception {
            runTest("plugins/power-assert/testData/codegen/infix/DispatchNonInfix.kt");
        }

        @Test
        @TestMetadata("DispatchNonInfixConstantParameter.kt")
        public void testDispatchNonInfixConstantParameter() throws Exception {
            runTest("plugins/power-assert/testData/codegen/infix/DispatchNonInfixConstantParameter.kt");
        }

        @Test
        @TestMetadata("DispatchNonInfixConstantReceiver.kt")
        public void testDispatchNonInfixConstantReceiver() throws Exception {
            runTest("plugins/power-assert/testData/codegen/infix/DispatchNonInfixConstantReceiver.kt");
        }

        @Test
        @TestMetadata("DispatchNonInfixOnlyConstants.kt")
        public void testDispatchNonInfixOnlyConstants() throws Exception {
            runTest("plugins/power-assert/testData/codegen/infix/DispatchNonInfixOnlyConstants.kt");
        }

        @Test
        @TestMetadata("ExtensionInfix.kt")
        public void testExtensionInfix() throws Exception {
            runTest("plugins/power-assert/testData/codegen/infix/ExtensionInfix.kt");
        }

        @Test
        @TestMetadata("ExtensionInfixConstantParameter.kt")
        public void testExtensionInfixConstantParameter() throws Exception {
            runTest("plugins/power-assert/testData/codegen/infix/ExtensionInfixConstantParameter.kt");
        }

        @Test
        @TestMetadata("ExtensionInfixConstantReceiver.kt")
        public void testExtensionInfixConstantReceiver() throws Exception {
            runTest("plugins/power-assert/testData/codegen/infix/ExtensionInfixConstantReceiver.kt");
        }

        @Test
        @TestMetadata("ExtensionInfixOnlyConstants.kt")
        public void testExtensionInfixOnlyConstants() throws Exception {
            runTest("plugins/power-assert/testData/codegen/infix/ExtensionInfixOnlyConstants.kt");
        }

        @Test
        @TestMetadata("ExtensionNonInfix.kt")
        public void testExtensionNonInfix() throws Exception {
            runTest("plugins/power-assert/testData/codegen/infix/ExtensionNonInfix.kt");
        }

        @Test
        @TestMetadata("ExtensionNonInfixConstantParameter.kt")
        public void testExtensionNonInfixConstantParameter() throws Exception {
            runTest("plugins/power-assert/testData/codegen/infix/ExtensionNonInfixConstantParameter.kt");
        }

        @Test
        @TestMetadata("ExtensionNonInfixConstantReceiver.kt")
        public void testExtensionNonInfixConstantReceiver() throws Exception {
            runTest("plugins/power-assert/testData/codegen/infix/ExtensionNonInfixConstantReceiver.kt");
        }

        @Test
        @TestMetadata("ExtensionNonInfixOnlyConstants.kt")
        public void testExtensionNonInfixOnlyConstants() throws Exception {
            runTest("plugins/power-assert/testData/codegen/infix/ExtensionNonInfixOnlyConstants.kt");
        }

        @Test
        @TestMetadata("RegexFunction.kt")
        public void testRegexFunction() throws Exception {
            runTest("plugins/power-assert/testData/codegen/infix/RegexFunction.kt");
        }

        @Test
        @TestMetadata("RegexInfix.kt")
        public void testRegexInfix() throws Exception {
            runTest("plugins/power-assert/testData/codegen/infix/RegexInfix.kt");
        }
    }

    @Nested
    @TestMetadata("plugins/power-assert/testData/codegen/junit5")
    @TestDataPath("$PROJECT_ROOT")
    public class Junit5 {
        @Test
        public void testAllFilesPresentInJunit5() throws Exception {
            KtTestUtil.assertAllTestsPresentByMetadataWithExcluded(this.getClass(), new File("plugins/power-assert/testData/codegen/junit5"), Pattern.compile("^(.+)\\.kt$"), Pattern.compile("^(.+)\\.fir\\.kts?$"), TargetBackend.JVM_IR, true);
        }

        @Test
        @TestMetadata("JunitAssertTrue.kt")
        public void testJunitAssertTrue() throws Exception {
            runTest("plugins/power-assert/testData/codegen/junit5/JunitAssertTrue.kt");
        }

        @Test
        @TestMetadata("JunitAssertTrueMessage.kt")
        public void testJunitAssertTrueMessage() throws Exception {
            runTest("plugins/power-assert/testData/codegen/junit5/JunitAssertTrueMessage.kt");
        }

        @Test
        @TestMetadata("JunitAssertTrueMessageSupplier.kt")
        public void testJunitAssertTrueMessageSupplier() throws Exception {
            runTest("plugins/power-assert/testData/codegen/junit5/JunitAssertTrueMessageSupplier.kt");
        }

        @Test
        @TestMetadata("JunitAssertTrueMessageSupplierVariable.kt")
        public void testJunitAssertTrueMessageSupplierVariable() throws Exception {
            runTest("plugins/power-assert/testData/codegen/junit5/JunitAssertTrueMessageSupplierVariable.kt");
        }

        @Test
        @TestMetadata("JunitAssertTrueMessageVariable.kt")
        public void testJunitAssertTrueMessageVariable() throws Exception {
            runTest("plugins/power-assert/testData/codegen/junit5/JunitAssertTrueMessageVariable.kt");
        }
    }

    @Nested
    @TestMetadata("plugins/power-assert/testData/codegen/kotlin-test")
    @TestDataPath("$PROJECT_ROOT")
    public class Kotlin_test {
        @Test
        public void testAllFilesPresentInKotlin_test() throws Exception {
            KtTestUtil.assertAllTestsPresentByMetadataWithExcluded(this.getClass(), new File("plugins/power-assert/testData/codegen/kotlin-test"), Pattern.compile("^(.+)\\.kt$"), Pattern.compile("^(.+)\\.fir\\.kts?$"), TargetBackend.JVM_IR, true);
        }

        @Test
        @TestMetadata("AssertEquals.kt")
        public void testAssertEquals() throws Exception {
            runTest("plugins/power-assert/testData/codegen/kotlin-test/AssertEquals.kt");
        }

        @Test
        @TestMetadata("AssertEqualsMessage.kt")
        public void testAssertEqualsMessage() throws Exception {
            runTest("plugins/power-assert/testData/codegen/kotlin-test/AssertEqualsMessage.kt");
        }

        @Test
        @TestMetadata("AssertEqualsMessageVariable.kt")
        public void testAssertEqualsMessageVariable() throws Exception {
            runTest("plugins/power-assert/testData/codegen/kotlin-test/AssertEqualsMessageVariable.kt");
        }

        @Test
        @TestMetadata("AssertFalse.kt")
        public void testAssertFalse() throws Exception {
            runTest("plugins/power-assert/testData/codegen/kotlin-test/AssertFalse.kt");
        }

        @Test
        @TestMetadata("AssertFalseMessage.kt")
        public void testAssertFalseMessage() throws Exception {
            runTest("plugins/power-assert/testData/codegen/kotlin-test/AssertFalseMessage.kt");
        }

        @Test
        @TestMetadata("AssertFalseMessageVariable.kt")
        public void testAssertFalseMessageVariable() throws Exception {
            runTest("plugins/power-assert/testData/codegen/kotlin-test/AssertFalseMessageVariable.kt");
        }

        @Test
        @TestMetadata("AssertNotNull.kt")
        public void testAssertNotNull() throws Exception {
            runTest("plugins/power-assert/testData/codegen/kotlin-test/AssertNotNull.kt");
        }

        @Test
        @TestMetadata("AssertNotNullMessage.kt")
        public void testAssertNotNullMessage() throws Exception {
            runTest("plugins/power-assert/testData/codegen/kotlin-test/AssertNotNullMessage.kt");
        }

        @Test
        @TestMetadata("AssertNotNullMessageVariable.kt")
        public void testAssertNotNullMessageVariable() throws Exception {
            runTest("plugins/power-assert/testData/codegen/kotlin-test/AssertNotNullMessageVariable.kt");
        }

        @Test
        @TestMetadata("AssertTrue.kt")
        public void testAssertTrue() throws Exception {
            runTest("plugins/power-assert/testData/codegen/kotlin-test/AssertTrue.kt");
        }

        @Test
        @TestMetadata("AssertTrueMessage.kt")
        public void testAssertTrueMessage() throws Exception {
            runTest("plugins/power-assert/testData/codegen/kotlin-test/AssertTrueMessage.kt");
        }

        @Test
        @TestMetadata("AssertTrueMessageVariable.kt")
        public void testAssertTrueMessageVariable() throws Exception {
            runTest("plugins/power-assert/testData/codegen/kotlin-test/AssertTrueMessageVariable.kt");
        }
    }

    @Nested
    @TestMetadata("plugins/power-assert/testData/codegen/lambda")
    @TestDataPath("$PROJECT_ROOT")
    public class Lambda {
        @Test
        public void testAllFilesPresentInLambda() throws Exception {
            KtTestUtil.assertAllTestsPresentByMetadataWithExcluded(this.getClass(), new File("plugins/power-assert/testData/codegen/lambda"), Pattern.compile("^(.+)\\.kt$"), Pattern.compile("^(.+)\\.fir\\.kts?$"), TargetBackend.JVM_IR, true);
        }

        @Test
        @TestMetadata("AnonymousObject.kt")
        public void testAnonymousObject() throws Exception {
            runTest("plugins/power-assert/testData/codegen/lambda/AnonymousObject.kt");
        }

        @Test
        @TestMetadata("ListOperationsAssert.kt")
        public void testListOperationsAssert() throws Exception {
            runTest("plugins/power-assert/testData/codegen/lambda/ListOperationsAssert.kt");
        }

        @Test
        @TestMetadata("ListOperationsRequire.kt")
        public void testListOperationsRequire() throws Exception {
            runTest("plugins/power-assert/testData/codegen/lambda/ListOperationsRequire.kt");
        }
    }

    @Nested
    @TestMetadata("plugins/power-assert/testData/codegen/nullsafe")
    @TestDataPath("$PROJECT_ROOT")
    public class Nullsafe {
        @Test
        public void testAllFilesPresentInNullsafe() throws Exception {
            KtTestUtil.assertAllTestsPresentByMetadataWithExcluded(this.getClass(), new File("plugins/power-assert/testData/codegen/nullsafe"), Pattern.compile("^(.+)\\.kt$"), Pattern.compile("^(.+)\\.fir\\.kts?$"), TargetBackend.JVM_IR, true);
        }

        @Test
        @TestMetadata("ConditionalAccess.kt")
        public void testConditionalAccess() throws Exception {
            runTest("plugins/power-assert/testData/codegen/nullsafe/ConditionalAccess.kt");
        }
    }

    @Nested
    @TestMetadata("plugins/power-assert/testData/codegen/operator")
    @TestDataPath("$PROJECT_ROOT")
    public class Operator {
        @Test
        public void testAllFilesPresentInOperator() throws Exception {
            KtTestUtil.assertAllTestsPresentByMetadataWithExcluded(this.getClass(), new File("plugins/power-assert/testData/codegen/operator"), Pattern.compile("^(.+)\\.kt$"), Pattern.compile("^(.+)\\.fir\\.kts?$"), TargetBackend.JVM_IR, true);
        }

        @Test
        @TestMetadata("ContainsFunction.kt")
        public void testContainsFunction() throws Exception {
            runTest("plugins/power-assert/testData/codegen/operator/ContainsFunction.kt");
        }

        @Test
        @TestMetadata("ContainsOperator.kt")
        public void testContainsOperator() throws Exception {
            runTest("plugins/power-assert/testData/codegen/operator/ContainsOperator.kt");
        }

        @Test
        @TestMetadata("NegativeContainsFunction.kt")
        public void testNegativeContainsFunction() throws Exception {
            runTest("plugins/power-assert/testData/codegen/operator/NegativeContainsFunction.kt");
        }

        @Test
        @TestMetadata("NegativeContainsOperator.kt")
        public void testNegativeContainsOperator() throws Exception {
            runTest("plugins/power-assert/testData/codegen/operator/NegativeContainsOperator.kt");
        }
    }

    @Nested
    @TestMetadata("plugins/power-assert/testData/codegen/parameters")
    @TestDataPath("$PROJECT_ROOT")
    public class Parameters {
        @Test
        public void testAllFilesPresentInParameters() throws Exception {
            KtTestUtil.assertAllTestsPresentByMetadataWithExcluded(this.getClass(), new File("plugins/power-assert/testData/codegen/parameters"), Pattern.compile("^(.+)\\.kt$"), Pattern.compile("^(.+)\\.fir\\.kts?$"), TargetBackend.JVM_IR, true);
        }

        @Test
        @TestMetadata("VarargParameter.kt")
        public void testVarargParameter() throws Exception {
            runTest("plugins/power-assert/testData/codegen/parameters/VarargParameter.kt");
        }
    }
}
