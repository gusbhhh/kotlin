plugins {
    kotlin("jvm")
    id("jps-compatible")
}

dependencies {
    api(project(":compiler:util"))
    api(project(":compiler:backend-common"))
    api(project(":compiler:ir.tree"))
    api(project(":compiler:ir.backend.common"))
    compileOnly(intellijCore())
}

optInToUnsafeDuringIrConstructionAPI()

sourceSets {
    "main" { projectDefault() }
    "test" {}
}

