plugins {
    id("kotlin-library-convention")
    alias(libs.plugins.kotlin.serialization)
}

dependencies {
    implementation(project(":core:common"))
    implementation(libs.kotlinx.serialization.core)
}