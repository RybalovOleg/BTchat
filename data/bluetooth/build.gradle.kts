plugins {
    id("android-library-convention")
    id("koin-convention")
}

android {
    namespace = "io.salir.btchat.data.bluetooth"
}

dependencies {
    implementation(project(":core:common"))
    implementation(project(":core:model"))
    implementation(project(":core:interfaces"))
}