plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.google.gms.google.services) apply false
}

buildscript {
    repositories {
        google()
        mavenCentral()
    }
    buildscript {
        repositories {
            google()
            mavenCentral()
            maven { url = uri("https://jitpack.io") }
        }
        dependencies {
            classpath(libs.gradle.v800)
            classpath(libs.kotlin.gradle.plugin)
            classpath(libs.androidx.navigation.safe.args.gradle.plugin.v253)
            classpath(libs.hilt.android.gradle.plugin)

        }
    }
}
