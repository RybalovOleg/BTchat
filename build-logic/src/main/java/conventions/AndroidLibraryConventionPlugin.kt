package conventions

import com.android.build.api.dsl.LibraryExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType
import org.jetbrains.kotlin.gradle.dsl.KotlinAndroidProjectExtension

class AndroidLibraryConventionPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")

            with(pluginManager) {
                apply(libs.findPlugin("android.library").get().get().pluginId)
                apply(libs.findPlugin("kotlin.android").get().get().pluginId)
            }

            extensions.configure<LibraryExtension> {
                compileSdk {
                    version = release(Config.compileSdk)
                }

                defaultConfig {
                    minSdk = Config.minSdk
                }

                compileOptions {
                    sourceCompatibility = Config.sourceCompatibility
                    targetCompatibility = Config.targetCompatibility
                }
            }

            extensions.configure<KotlinAndroidProjectExtension> {
                jvmToolchain(Config.jvmToolchain)

                compilerOptions {
                    jvmTarget.set(Config.jvmTarget)
                }
            }

            dependencies {
                add("implementation", libs.findLibrary("kotlinx.coroutines.android").get())
                add("implementation", libs.findLibrary("androidx.core.ktx").get())
            }
        }
    }
}