import com.android.build.gradle.LibraryExtension
import com.android.build.gradle.internal.api.BaseVariantOutputImpl
import org.gradle.kotlin.dsl.support.kotlinCompilerOptions
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmProjectExtension
import java.util.Properties

// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.jetbrains.kotlin.jvm) apply false
    alias(libs.plugins.android.library) apply false
}

val versionProps = Properties().apply {
    load(File("${rootProject.projectDir}/version.properties").inputStream())
}

subprojects {
    afterEvaluate {
        //Java Library JVM config.
        extensions.findByType(JavaPluginExtension::class)?.apply {
            sourceCompatibility = JavaVersion.VERSION_21
            targetCompatibility = JavaVersion.VERSION_21
        }

        extensions.findByType(KotlinJvmProjectExtension::class)?.apply {
            jvmToolchain(21)
        }

        extensions.findByType(com.android.build.gradle.BaseExtension::class)?.apply {
            setCompileSdkVersion(35)

            //Android Library JVM config.
            compileOptions {
                sourceCompatibility = JavaVersion.VERSION_21
                targetCompatibility = JavaVersion.VERSION_21
            }

            defaultConfig {
                minSdk = 21

                versionName = versionProps["versionName"].toString()
                versionCode = versionProps["versionCode"].toString().toInt()
            }

            composeOptions {
                kotlinCompilerExtensionVersion = "1.5.1"
            }
        }
    }
}