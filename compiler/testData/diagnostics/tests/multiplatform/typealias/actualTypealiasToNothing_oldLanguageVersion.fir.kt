// LANGUAGE: -MultiplatformRestrictions
// MODULE: m1-common
// FILE: common.kt

<!EXPECT_ACTUAL_INCOMPATIBILITY{JVM}, NO_ACTUAL_FOR_EXPECT{JVM}, NO_ACTUAL_FOR_EXPECT{JVM}, NO_ACTUAL_FOR_EXPECT{JVM}!>expect class E01<!>
<!EXPECT_ACTUAL_INCOMPATIBILITY{JVM}, NO_ACTUAL_FOR_EXPECT{JVM}, NO_ACTUAL_FOR_EXPECT{JVM}, NO_ACTUAL_FOR_EXPECT{JVM}!>expect class E02<!>

// MODULE: m1-jvm()()(m1-common)
// FILE: jvm.kt

typealias MyNothing = Nothing

actual typealias E01 = Nothing
<!ACTUAL_TYPE_ALIAS_NOT_TO_CLASS!>actual typealias E02 = MyNothing<!>
