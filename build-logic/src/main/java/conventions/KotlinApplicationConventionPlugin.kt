package conventions

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmProjectExtension

class KotlinApplicationConventionPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")

            with(pluginManager) {
                apply("application")
                apply(libs.findPlugin("jetbrains.kotlin.jvm").get().get().pluginId)
            }

            extensions.configure<JavaPluginExtension> {
                sourceCompatibility = Config.sourceCompatibility
                targetCompatibility = Config.targetCompatibility
            }

            extensions.configure<KotlinJvmProjectExtension> {
                jvmToolchain(Config.jvmToolchain)

                compilerOptions {
                    jvmTarget.set(Config.jvmTarget)
                }
            }

            dependencies {
                add("implementation", libs.findLibrary("kotlinx.coroutines.core").get())
            }
        }
    }
}