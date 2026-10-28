# Jenga Catalog

A Compose Multiplatform sample app that renders every Jenga token and component. It runs on
**Android, Desktop (JVM) and iOS** from one shared `commonMain` UI.

```
catalog/
├── src/commonMain/kotlin   # the shared catalog UI (CatalogApp, CatalogScreen, ComponentsGallery)
├── src/androidMain         # Android entry (CatalogActivity) + manifest + previews/goldens
├── src/desktopMain         # Desktop entry (main.kt)
├── src/iosMain             # iOS entry (MainViewController.kt)
└── iosApp/                 # Xcode project that hosts the iOS framework
```

## Run it

### Desktop

```bash
./gradlew :catalog:run
```

Opens a resizable window. To build an installer instead: `./gradlew :catalog:packageDistributionForCurrentOS`.

### Android

```bash
./gradlew :catalog:installDebug   # to a connected device/emulator
```

Then launch the "JengaCatalog" app, or open the project in Android Studio and run the `catalog` config.

### iOS

Open `catalog/iosApp/iosApp.xcodeproj` in Xcode, pick an **Apple-Silicon simulator** (or a
device) and hit Run. The `Compile Kotlin Framework` build phase invokes Gradle to build and
embed the `CatalogApp` framework automatically.

From the command line (simulator):

```bash
cd catalog/iosApp
xcodebuild -project iosApp.xcodeproj -scheme iosApp \
  -sdk iphonesimulator -configuration Debug \
  -destination 'generic/platform=iOS Simulator' build
```

Notes:
- Only `iosArm64` (devices) and `iosSimulatorArm64` (Apple-Silicon simulator) are supported;
  Compose Multiplatform no longer ships the Intel-simulator (`iosX64`) slice.
- To run on a physical device, set your development team under Signing & Capabilities.

## Screenshots

The catalog is itself screenshot-tested on the Android target (Roborazzi). Record/verify:

```bash
./gradlew :catalog:recordRoborazziDebug
./gradlew :catalog:verifyRoborazziDebug
```

The components gallery screenshot is a fixed 4250dp tall, so it covers the first groups only.
Every component also has its own light, dark, large-font and RTL screenshots in the library.
