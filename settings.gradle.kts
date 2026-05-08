pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version("0.8.0")
}

includeBuild("build-logic")

rootProject.name = "BTchat"
include(":app")
include(":feature:chat")
include(":feature:welcome-screen")
include(":domain")
include(":core:ui")
include(":feature:host-new-chat-screen")
include(":core:model")
include(":data:bluetooth")
include(":core:interfaces")
include(":core:local-storage")
include(":cli_impl")
include(":core:security")
include(":core:util")
