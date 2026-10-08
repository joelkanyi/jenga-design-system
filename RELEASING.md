# Releasing Jenga to Maven Central

Jenga publishes to Maven Central through the [Central Portal](https://central.sonatype.com)
using the [vanniktech maven-publish plugin](https://vanniktech.github.io/gradle-maven-publish-plugin/).

Coordinates: `io.github.joelkanyi:jenga:<version>`

The published artifact is public, even though this source repository is private. Only the
built library, sources jar and javadoc jar leave the repo.

## One-time setup

### 1. Claim the namespace

On the Central Portal, verify ownership of the `io.github.joelkanyi` namespace (it is tied to
the GitHub account `joelkanyi`, so the portal will ask you to create a verification repo or
add a TXT record). This is done once.

### 2. Create a Central Portal user token

Central Portal -> Account -> Generate User Token. You get a username and password pair.

### 3. Create a GPG signing key

```bash
gpg --gen-key                       # if you don't already have one
gpg --list-secret-keys --keyid-format short   # note the KEY_ID
gpg --keyserver keyserver.ubuntu.com --send-keys <KEY_ID>   # publish the public key
# export the secret key in-memory form for Gradle:
gpg --armor --export-secret-keys <KEY_ID>
```

### 4. Put the credentials in `~/.gradle/gradle.properties`

Never commit these. Keep them in your home Gradle file (or pass them as
`ORG_GRADLE_PROJECT_*` environment variables in CI).

```properties
mavenCentralUsername=<central portal token username>
mavenCentralPassword=<central portal token password>

signingInMemoryKey=<the armored secret key from step 3, newlines as \n>
signingInMemoryKeyPassword=<the key passphrase>
```

A commented template lives in `gradle/publish.properties.template`.

## Cutting a release with CI (recommended)

Releases run from GitHub Actions (`.github/workflows/publish.yml`) on a macOS runner, so the
iOS artifacts are built and signed too.

One-time: add the four credentials from the setup section as **GitHub repository secrets**
(Settings -> Secrets and variables -> Actions):

| Secret | Value |
|--------|-------|
| `MAVEN_CENTRAL_USERNAME` | Central Portal token username |
| `MAVEN_CENTRAL_PASSWORD` | Central Portal token password |
| `SIGNING_KEY` | the armored GPG secret key (`gpg --armor --export-secret-keys <KEY_ID>`) |
| `SIGNING_KEY_PASSWORD` | the GPG key passphrase |

Then, to release:

1. Add a `## Version x.y.z` section to `CHANGELOG.md`, bump `version` in
   `jenga/build.gradle.kts` to the release version (no `-SNAPSHOT`), commit and push to `main`.
   Let CI go green.
2. Tag and push:

   ```bash
   git tag v0.1.0
   git push origin v0.1.0
   ```

   The push of a `v*` tag triggers the Publish workflow, which runs
   `publishAndReleaseToMavenCentral` and then creates the GitHub release with that version's
   `CHANGELOG.md` section as its notes.
3. After it succeeds, bump `version` to the next `-SNAPSHOT` on `main`.

You can also run the workflow manually from the Actions tab (`workflow_dispatch`), with a
`dry_run` option that publishes to Maven Local only.

## Cutting a release locally

1. Bump `version` in `jenga/build.gradle.kts` to the release version (no `-SNAPSHOT`).
2. Make sure everything is green: `./gradlew :jenga:check`.
3. Publish and release (builds, signs and uploads every target; takes a few minutes):

   ```bash
   ./gradlew publishAndReleaseToMavenCentral --no-configuration-cache
   ```

4. Tag the release and bump `version` to the next `-SNAPSHOT`.

To publish a snapshot instead (no release, consumable from the Central snapshots repo), use a
`-SNAPSHOT` version and run `./gradlew publishToMavenCentral`.

## CI

`.github/workflows/ci.yml` runs on every push to `main` and every PR:

- **JVM & Android** (Ubuntu): `:jenga:check` (compile Android + Desktop, apiCheck, lint,
  token-usage check, contrast tests, Roborazzi goldens) and the catalog's Android + Desktop
  builds, plus it generates and uploads the Dokka API docs as an artifact.
- **iOS** (macOS): compiles both iOS targets, links the catalog framework, and builds the iOS
  sample app with `xcodebuild` against the simulator.

Dependabot (`.github/dependabot.yml`) opens weekly update PRs for Gradle dependencies (Kotlin
and Compose grouped) and the GitHub Actions themselves.

## Consuming the published artifact

```kotlin
// settings.gradle.kts of the consumer app
dependencyResolutionManagement {
    repositories { mavenCentral() }
}

// a KMP module's build.gradle.kts
kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation("io.github.joelkanyi:jenga:<version>")
        }
    }
}
```

For an Android-only consumer, the same coordinate resolves the Android variant automatically.
