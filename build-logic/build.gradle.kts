plugins {
    `kotlin-dsl`
}

repositories {
    google()
    mavenCentral()
    gradlePluginPortal()
}

dependencies {
    compileOnly(libs.android.gradlePlugin)
    compileOnly(libs.kotlin.gradlePlugin)
}

gradlePlugin {
    plugins {
        register("androidApplicationConvention") {
            id = "android-application-convention"
            implementationClass = "conventions.AndroidApplicationConventionPlugin"
        }

        register("androidLibraryConvention") {
            id = "android-library-convention"
            implementationClass = "conventions.AndroidLibraryConventionPlugin"
        }

        register("kotlinApplicationConvention") {
            id = "kotlin-application-convention"
            implementationClass = "conventions.KotlinApplicationConventionPlugin"
        }

        register("kotlinLibraryConvention") {
            id = "kotlin-library-convention"
            implementationClass = "conventions.KotlinLibraryConventionPlugin"
        }

        register("FeatureModuleConventionPlugin") {
            id = "feature-module-convention"
            implementationClass = "conventions.FeatureModuleConventionPlugin"
        }

        register("NavigationConventionPlugin") {
            id = "navigation-convention"
            implementationClass = "conventions.NavigationConventionPlugin"
        }

        register("KoinConventionPlugin") {
            id = "koin-convention"
            implementationClass = "conventions.KoinConventionPlugin"
        }

        register("ComposeConventionPlugin") {
            id = "compose-convention"
            implementationClass = "conventions.ComposeConventionPlugin"
        }
    }
}