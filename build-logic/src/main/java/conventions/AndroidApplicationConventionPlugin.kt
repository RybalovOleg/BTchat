package conventions

import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.LibraryExtension
import com.android.build.gradle.internal.dsl.BaseAppModuleExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.internal.impldep.org.apache.http.client.methods.Configurable
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType
import org.jetbrains.kotlin.gradle.dsl.KotlinAndroidProjectExtension

class AndroidApplicationConventionPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")

            with(pluginManager) {
                apply(libs.findPlugin("android.application").get().get().pluginId)
                apply(libs.findPlugin("kotlin.android").get().get().pluginId)
                apply("koin-convention")
                apply("compose-convention")
                apply("navigation-convention")
            }

            extensions.configure<ApplicationExtension> {
                compileSdk {
                    version = release(Config.compileSdk)
                }

                defaultConfig {
                    minSdk = Config.minSdk
                    targetSdk = Config.targetSdk

                    versionCode = Config.version.code
                    versionName = Config.version.name

                }

                compileOptions {
                    sourceCompatibility = Config.sourceCompatibility
                    targetCompatibility = Config.targetCompatibility
                }

                buildFeatures {
                    compose = true
                    buildConfig = true
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
                add("implementation", libs.findLibrary("androidx.lifecycle.runtime.ktx").get())

                add("implementation", libs.findLibrary("androidx.activity.compose").get())
            }
        }
    }
}