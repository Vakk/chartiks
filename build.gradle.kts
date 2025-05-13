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
        extensions.findByType(com.android.build.gradle.BaseExtension::class)?.apply {
            defaultConfig {
                versionName = versionProps["versionName"].toString()
                versionCode = versionProps["versionCode"].toString().toInt()
            }
        }
    }
}