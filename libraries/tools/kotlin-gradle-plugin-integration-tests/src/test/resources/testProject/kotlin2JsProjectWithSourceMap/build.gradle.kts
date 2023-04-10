import org.jetbrains.kotlin.gradle.dsl.KotlinJsCompile
import org.jetbrains.kotlin.gradle.plugin.KotlinJsCompilerType
import org.jetbrains.kotlin.gradle.targets.js.ir.KotlinJsIrLink
import kotlin.text.toBoolean

buildscript {
    repositories {
        mavenLocal()
        mavenCentral()
    }
    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:${property("kotlin_version")}")
    }
}

repositories {
    mavenLocal()
    mavenCentral()
}


val useIrBackend = (findProperty("kotlin.js.useIrBackend") as? String?)?.toBoolean() ?: false

val backend = if (useIrBackend) {
    KotlinJsCompilerType.IR
} else {
    KotlinJsCompilerType.LEGACY
}
