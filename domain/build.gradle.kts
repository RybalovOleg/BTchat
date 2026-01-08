plugins {
    id("kotlin-library-convention")
}

dependencies {
    implementation(project(":core:common"))
    implementation(project(":core:model"))
    implementation(project(":core:interfaces"))
}
