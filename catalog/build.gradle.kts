@file:OptIn(com.github.takahirom.roborazzi.ExperimentalRoborazziApi::class)

import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.android.application)
    alias(libs.plugins.compose.multiplatform)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.roborazzi)
}

kotlin {
    androidTarget {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_17)
        }
    }

    jvm("desktop") {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_17)
        }
    }

    listOf(iosArm64(), iosSimulatorArm64()).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "CatalogApp"
            isStatic = true
        }
    }

    sourceSets {
        commonMain.dependencies {
            implementation(project(":jenga"))
            @Suppress("DEPRECATION")
            implementation(compose.runtime)
            @Suppress("DEPRECATION")
            implementation(compose.foundation)
            @Suppress("DEPRECATION")
            implementation(compose.ui)
            @Suppress("DEPRECATION")
            implementation(compose.material3)
        }

        androidMain.dependencies {
            implementation(libs.androidx.activity.compose)
            implementation(libs.androidx.core.ktx)
            implementation(libs.androidx.lifecycle.runtime.ktx)
            implementation(project.dependencies.platform(libs.androidx.compose.bom))
            implementation(libs.androidx.compose.ui.tooling.preview)
            implementation(libs.androidx.compose.ui.tooling)
        }

        val desktopMain by getting {
            dependencies {
                @Suppress("DEPRECATION")
                implementation(compose.desktop.currentOs)
            }
        }

        val androidUnitTest by getting {
            dependencies {
                implementation(project.dependencies.platform(libs.androidx.compose.bom))
                implementation(libs.junit)
                implementation(libs.robolectric)
                implementation(libs.roborazzi)
                implementation(libs.roborazzi.compose)
                implementation(libs.roborazzi.junit.rule)
                implementation(libs.roborazzi.compose.preview.scanner.support)
                implementation(libs.composable.preview.scanner)
                implementation(libs.androidx.compose.ui.test.junit4)
                implementation(libs.androidx.compose.ui.tooling)
            }
        }
    }
}

android {
    namespace = "io.github.joelkanyi.jenga.catalog"
    compileSdk = 36

    defaultConfig {
        applicationId = "io.github.joelkanyi.jenga.catalog"
        minSdk = 23
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    testOptions {
        unitTests {
            isIncludeAndroidResources = true
            all {
                it.systemProperties["robolectric.pixelCopyRenderMode"] = "hardware"
            }
        }
    }
}

// Desktop entry point + packaging.
compose.desktop {
    application {
        mainClass = "io.github.joelkanyi.jenga.catalog.MainKt"
        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "JengaCatalog"
            packageVersion = "1.0.0"
        }
    }
}

// Screenshot-test the catalog itself (Airbnb/Showkase practice), Android target only.
roborazzi {
    outputDir.set(layout.projectDirectory.dir("src/androidUnitTest/screenshots"))
    generateComposePreviewRobolectricTests {
        enable = true
        packages = listOf("io.github.joelkanyi.jenga.catalog")
        testerQualifiedClassName = "io.github.joelkanyi.jenga.catalog.CatalogPreviewTester"
    }
}
