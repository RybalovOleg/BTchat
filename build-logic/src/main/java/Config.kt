import org.gradle.api.JavaVersion
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

object Config {
    val version = Version(0, 0, 1)

    val compileSdk = 36

    val minSdk = 26
    val targetSdk = 36

    val sourceCompatibility = JavaVersion.VERSION_17
    val targetCompatibility = JavaVersion.VERSION_17

    val jvmTarget = JvmTarget.JVM_17

    val jvmToolchain = 17
}