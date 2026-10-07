@file:OptIn(
    com.github.takahirom.roborazzi.ExperimentalRoborazziApi::class,
    org.jetbrains.kotlin.gradle.ExperimentalWasmDsl::class,
)

import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.android.library)
    alias(libs.plugins.compose.multiplatform)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.roborazzi)
    alias(libs.plugins.dokka)
    alias(libs.plugins.maven.publish)
    alias(libs.plugins.poko)
}

group = "io.github.joelkanyi"
version = "0.4.2-SNAPSHOT"

kotlin {
    // Public API of a published library must be explicit: every public/protected
    // declaration needs an explicit visibility modifier and return type.
    explicitApi()

    androidTarget {
        publishLibraryVariants("release")
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_17)
        }
    }

    jvm("desktop") {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_17)
        }
    }

    // iosArm64 = physical devices, iosSimulatorArm64 = Apple-Silicon simulator.
    // iosX64 (Intel-Mac simulator) is legacy and no longer published by Compose
    // Multiplatform, so it is intentionally omitted.
    iosArm64()
    iosSimulatorArm64()

    // Web target. Kotlin/Wasm is the modern Compose Multiplatform web backend;
    // browser() wires the wasm runtime + Compose Resources loading for the browser.
    wasmJs {
        browser()
    }

    sourceSets {
        commonMain.dependencies {
            // Exposed as `api` because Jenga's public API surfaces Compose types
            // (Color, Modifier, Dp, TextStyle, @Composable, RowScope, BorderStroke …),
            // so consumers must receive these transitively.
            @Suppress("DEPRECATION")
            api(compose.runtime)
            @Suppress("DEPRECATION")
            api(compose.foundation)
            @Suppress("DEPRECATION")
            api(compose.ui)
            @Suppress("DEPRECATION")
            api(compose.uiUtil)
            // Material 3 is an internal implementation detail (the bridge for ripple,
            // text selection and reused M3 primitives). Not part of Jenga's API.
            @Suppress("DEPRECATION")
            implementation(compose.material3)
            // Compose Resources: fonts + vector-drawable icons bundled with Jenga.
            @Suppress("DEPRECATION")
            implementation(compose.components.resources)
        }

        androidMain.dependencies {
            // Preview tooling is Android-only; the previews + Roborazzi goldens live
            // in androidMain/androidUnitTest.
            implementation(project.dependencies.platform(libs.androidx.compose.bom))
            implementation(libs.androidx.compose.ui.tooling.preview)
            implementation(libs.androidx.compose.ui.tooling)
            // Coil3 async image loading powers JengaImage's actual on Android (the
            // network engine comes from the consuming app). Desktop/iOS fall back to
            // the placeholder until their Coil toolchain lines up.
            implementation(libs.coil.compose)
            implementation(libs.coil.network.ktor3)
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
    namespace = "io.github.joelkanyi.jenga"
    compileSdk = 36

    defaultConfig {
        minSdk = 23
        consumerProguardFiles("consumer-rules.pro")
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    lint {
        warningsAsErrors = false
        // Conflicts with our setup: previews must be `internal` (not private) for
        // ComposablePreviewScanner, and a design system legitimately defines
        // CompositionLocals for its theme.
        disable += setOf(
            "ComposePreviewPublic",
            "ComposePreviewNaming",
            "ComposeCompositionLocalUsage",
        )
    }

    testOptions {
        unitTests {
            isIncludeAndroidResources = true
            all {
                // Improves Roborazzi screenshot fidelity (Robolectric 4.12.2+).
                it.systemProperties["robolectric.pixelCopyRenderMode"] = "hardware"
            }
        }
    }
}

// Compose Resources: generate an internal `Res` accessor for the bundled fonts
// and icons in commonMain/composeResources.
compose.resources {
    publicResClass = false
    packageOfResClass = "io.github.joelkanyi.jenga.resources"
    generateResClass = auto
}

// Machine-enforced Compose API conventions (param order, modifier rules, naming):
// Slack's compose-lints, run on the Android target via `./gradlew :jenga:lint`.
dependencies {
    lintChecks(libs.compose.lint.checks)
}

// ---- API docs (Dokka) ------------------------------------------------------
// Generate browsable HTML API docs from the KDoc with `./gradlew :jenga:dokkaGenerateHtml`
// (output under jenga/build/dokka/html). Module + package overviews come from Module.md.
dokka {
    moduleName.set("Jenga")
    dokkaSourceSets.configureEach {
        includes.from("Module.md")
        sourceLink {
            localDirectory.set(projectDir.resolve("src"))
            remoteUrl("https://github.com/joelkanyi/jenga/tree/main/jenga/src")
            remoteLineSuffix.set("#L")
        }
    }
    pluginsConfiguration.html {
        footerMessage.set("Jenga: a Kotlin Multiplatform + Compose Multiplatform design system")
    }
}

// ---- Maven Central publishing ----------------------------------------------
// Publishes all targets (Android, Desktop, iOS, Web) plus a sources and javadoc jar to
// Maven Central via the Central Portal. Credentials and the signing key are read
// from ~/.gradle/gradle.properties or ORG_GRADLE_PROJECT_* env vars, never from a
// committed file. See RELEASING.md.
mavenPublishing {
    publishToMavenCentral()
    // Sign only when a key is present, so local builds and `publishToMavenLocal`
    // work without one. Releases supply `signingInMemoryKey` (see RELEASING.md).
    if (providers.gradleProperty("signingInMemoryKey").isPresent) {
        signAllPublications()
    }

    coordinates(group.toString(), "jenga", version.toString())

    pom {
        name.set("Jenga")
        description.set(
            "Jenga is a Kotlin Multiplatform + Compose Multiplatform design system: " +
                "brandable tokens and ready-made blocks for Android, Desktop, iOS and Web.",
        )
        inceptionYear.set("2026")
        url.set("https://github.com/joelkanyi/jenga")
        licenses {
            license {
                name.set("The Apache License, Version 2.0")
                url.set("https://www.apache.org/licenses/LICENSE-2.0.txt")
                distribution.set("https://www.apache.org/licenses/LICENSE-2.0.txt")
            }
        }
        developers {
            developer {
                id.set("joelkanyi")
                name.set("Joel Kanyi")
                url.set("https://github.com/joelkanyi")
            }
        }
        scm {
            url.set("https://github.com/joelkanyi/jenga")
            connection.set("scm:git:git://github.com/joelkanyi/jenga.git")
            developerConnection.set("scm:git:ssh://git@github.com/joelkanyi/jenga.git")
        }
    }
}

// Auto-generate Roborazzi screenshot tests from every @Preview in the library
// (Android target only). Goldens are committed under src/androidUnitTest/screenshots.
roborazzi {
    outputDir.set(layout.projectDirectory.dir("src/androidUnitTest/screenshots"))
    generateComposePreviewRobolectricTests {
        enable = true
        packages = listOf("io.github.joelkanyi.jenga")
        // Cross-OS-tolerant screenshot comparison (macOS dev vs Linux CI).
        testerQualifiedClassName = "io.github.joelkanyi.jenga.JengaPreviewTester"
    }
}

// ---- Design-token pipeline -------------------------------------------------
// `tokens/primitives.json` is the single source of truth (a Figma -> JSON export
// stand-in). This task regenerates the primitive layer (JengaPalette.kt) from it.
//   ./gradlew :jenga:generateJengaTokens
tasks.register("generateJengaTokens") {
    group = "jenga"
    description = "Regenerates JengaPalette.kt from tokens/primitives.json."
    val tokensFile = layout.projectDirectory.file("tokens/primitives.json")
    val outFile = layout.projectDirectory.file(
        "src/commonMain/kotlin/io/github/joelkanyi/jenga/foundation/color/JengaPalette.kt",
    )
    inputs.file(tokensFile)
    outputs.file(outFile)
    doLast {
        @Suppress("UNCHECKED_CAST")
        val root = groovy.json.JsonSlurper().parse(tokensFile.asFile) as Map<String, Any?>

        @Suppress("UNCHECKED_CAST")
        val colors = root["color"] as Map<String, String>
        val out = buildString {
            appendLine("package io.github.joelkanyi.jenga.foundation.color")
            appendLine()
            appendLine("import androidx.compose.ui.graphics.Color")
            appendLine()
            appendLine("/**")
            appendLine(" * GENERATED FILE: DO NOT EDIT BY HAND.")
            appendLine(" *")
            appendLine(" * Regenerate with `./gradlew :jenga:generateJengaTokens` after editing")
            appendLine(" * `tokens/primitives.json` (the design-token source of truth). This is the")
            appendLine(" * **primitive** color layer: raw values with no meaning; the semantic layer")
            appendLine(" * [JengaColors] maps these onto roles.")
            appendLine(" */")
            appendLine("internal object JengaPalette {")
            colors.forEach { (name, hex) ->
                val argb = "0xFF" + hex.removePrefix("#").uppercase()
                appendLine("    val $name: Color = Color($argb)")
            }
            appendLine("}")
        }
        outFile.asFile.writeText(out)
        logger.lifecycle("generateJengaTokens: wrote ${colors.size} primitives to ${outFile.asFile.name}")
    }
}

// ---- Token-usage enforcement -----------------------------------------------
// Fails the build if component/pattern code hardcodes raw `Color(0x…)` literals
// instead of reading JengaTheme.colors tokens.
tasks.register("checkJengaTokenUsage") {
    group = "verification"
    description = "Fails if component/pattern code uses raw Color(0x…) literals instead of tokens."
    val srcRoot = layout.projectDirectory.dir("src/commonMain/kotlin/io/github/joelkanyi/jenga")
    inputs.dir(srcRoot)
    doLast {
        val rootDirFile = srcRoot.asFile
        val hex = Regex("""Color\(\s*0x""")
        val offenders = mutableListOf<String>()
        rootDirFile.walkTopDown()
            .filter { it.isFile && it.extension == "kt" }
            .forEach { file ->
                val rel = file.relativeTo(rootDirFile).path.replace('\\', '/')
                val inComponentLayer = rel.startsWith("component/") || rel.startsWith("pattern/")
                if (inComponentLayer) {
                    file.readLines().forEachIndexed { index, line ->
                        if (hex.containsMatchIn(line)) {
                            offenders += "${file.relativeTo(rootDir)}:${index + 1}: ${line.trim()}"
                        }
                    }
                }
            }
        if (offenders.isNotEmpty()) {
            throw GradleException(
                "Raw Color(0x…) literals in component/pattern code, use JengaTheme.colors tokens:\n" +
                    offenders.joinToString("\n"),
            )
        }
        logger.lifecycle("checkJengaTokenUsage: no raw color literals in components ✓")
    }
}

tasks.named("check").configure { dependsOn("checkJengaTokenUsage") }
