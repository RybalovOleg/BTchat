plugins {
    id("android-application-convention")
}

android {
    namespace = "io.salir.btchat"
    defaultConfig {
        applicationId = "io.salir.btchat"
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("debug")
            project.extensions.extraProperties["android.enableAppCompileTimeRClass"] = true
        }

        debug {
            isMinifyEnabled = false
            applicationIdSuffix = ".debug"
            versionNameSuffix = "-DEBUG"
        }

        flavorDimensions += "version"

        productFlavors {
            create("dev") {
                dimension = "version"
                versionNameSuffix = "-dev"
            }
            create("prod") {
                dimension = "version"
            }
        }
    }
}

dependencies {
    implementation(project(":feature:welcome-screen"))
    implementation(project(":feature:host-new-chat-screen"))
    implementation(project(":feature:chat"))

    implementation(project(":data:bluetooth"))

    implementation(project(":domain"))
    implementation(project(":core:interfaces"))
    implementation(project(":core:common"))
}