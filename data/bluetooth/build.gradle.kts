plugins {
    id("android-library-convention")
    id("koin-convention")
}

android {
    namespace = "io.salir.btchat.data.bluetooth"
}

dependencies {
    implementation(project(":core:model"))
    implementation(project(":core:interfaces"))
    implementation(project(":core:security"))
    implementation(project(":core:util"))

    implementation(libs.kotlinx.serialization.json)
}