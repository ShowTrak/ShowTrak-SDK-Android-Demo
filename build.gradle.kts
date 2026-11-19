// Top-level build file. Plugin versions are declared here and applied in modules.
plugins {
    id("com.android.application") version "8.13.2" apply false
    id("com.android.library") version "8.13.2" apply false
    id("org.jetbrains.kotlin.android") version "1.9.24" apply false
    // Formatting and Kotlin style: ./gradlew spotlessCheck (CI) / spotlessApply.
    // ktlint reads its style from .editorconfig.
    id("com.diffplug.spotless") version "8.11.0"
}

spotless {
    kotlin {
        target("**/*.kt")
        targetExclude("**/build/**")
        ktlint("1.8.0")
    }
    kotlinGradle {
        target("**/*.gradle.kts")
        targetExclude("**/build/**")
        ktlint("1.8.0")
    }
}
