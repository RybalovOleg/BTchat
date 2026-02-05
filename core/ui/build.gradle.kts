plugins {
    id("android-library-convention")
    id("compose-convention")
}

android {
    namespace = "io.salir.ui"
}

dependencies {
    implementation(project(":core:common"))
}