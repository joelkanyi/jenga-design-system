@file:OptIn(kotlinx.validation.ExperimentalBCVApi::class)

// Top-level build file for the standalone Jenga design system.

plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.multiplatform) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.compose.multiplatform) apply false
    alias(libs.plugins.roborazzi) apply false
    alias(libs.plugins.dokka) apply false
    alias(libs.plugins.maven.publish) apply false
    alias(libs.plugins.poko) apply false
    alias(libs.plugins.spotless)
    alias(libs.plugins.binary.compatibility.validator)
}

// Formatting and linting across every module, via ktlint. Run `spotlessApply`
// to fix, `spotlessCheck` (wired into `check`) to gate.
spotless {
    val ktlintVersion = libs.versions.ktlint.get()
    // Compose bends three ktlint naming rules: composables are PascalCase
    // (function-naming); *Defaults constants are PascalCase (property-naming);
    // and a block file bundles Foo + FooColors + FooDefaults, so the file name
    // need not match one declaration (filename). Passed as overrides because
    // spotless does not forward these rule-disable keys from .editorconfig.
    val rules = mapOf(
        "ktlint_standard_function-naming" to "disabled",
        "ktlint_standard_property-naming" to "disabled",
        "ktlint_standard_filename" to "disabled",
        "max_line_length" to "off",
        // A design system's theme tokens ARE its public CompositionLocals, the
        // same mechanism MaterialTheme uses; allowlist them for compose-rules.
        "compose_allowed_composition_locals" to listOf(
            "LocalJengaColors",
            "LocalJengaTypography",
            "LocalJengaSpacing",
            "LocalJengaShapes",
            "LocalJengaSizing",
            "LocalJengaElevation",
            "LocalJengaMotion",
            "LocalJengaIcons",
            "LocalJengaContentColor",
            "LocalJengaComponentStyles",
        ).joinToString(","),
    )
    val composeRules = libs.compose.rules.ktlint.get().toString()
    kotlin {
        target("**/src/**/*.kt")
        // The vendored HCT color math is kept verbatim and re-syncable, so it is
        // excluded from formatting/linting.
        targetExclude("**/build/**", "**/foundation/color/hct/**")
        ktlint(ktlintVersion)
            .customRuleSets(listOf(composeRules))
            .editorConfigOverride(rules)
    }
    kotlinGradle {
        target("**/*.gradle.kts")
        // The vendored HCT color math is kept verbatim and re-syncable, so it is
        // excluded from formatting/linting.
        targetExclude("**/build/**", "**/foundation/color/hct/**")
        ktlint(ktlintVersion).editorConfigOverride(rules)
    }
}

// Public-API tracking for the published library. Only :jenga is validated;
// the catalog is a consumer. Run `./gradlew apiDump` after intentional API
// changes; CI runs `apiCheck`.
apiValidation {
    ignoredProjects += listOf("catalog")
    // The library ships native (iOS) targets, so validate the KLib ABI too;
    // JVM `.api` dumps alone do not cover the native public surface.
    klib {
        enabled = true
    }
}
