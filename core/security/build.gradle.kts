plugins {
    id("android-library-convention")
    id("koin-convention")
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "io.salir.btchat.core.security"
}

dependencies {
    implementation(project(":core:model"))

    implementation(libs.androidx.datastore.preferences)
    implementation(libs.kotlinx.serialization.json)
}