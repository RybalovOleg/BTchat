plugins {
    id("kotlin-application-convention")
}

application {
    mainClass = "io.salir.btchat.cli_impl.MainKt"
}

dependencies {
    implementation(project(":domain"))
    implementation(project(":core:model"))
}