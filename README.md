# PermissionPilot

PermissionPilot is a Kotlin-first Android permissions toolkit for apps that need predictable permission flows across API versions.

Instead of making each screen reason about raw permission strings and SDK checks, PermissionPilot lets the app request feature-level access:

```kotlin
val cameraPermission = rememberPermissionPilotState(PermissionUseCase.Camera)

PermissionGate(state = cameraPermission) {
    CameraScreen()
}
```

## Modules

- `permissionpilot-core`: permission use cases, API-level mapping, status models, and resolver APIs.
- `permissionpilot-compose`: Compose permission state and UI gate helpers.
- `permissionpilot-test`: fake permission controller for unit tests.
- `sample`: Compose sample app that exercises each supported use case.

## Install

After the first Maven Central release:

```kotlin
dependencies {
    implementation("io.github.huongstienstra.permissionpilot:permissionpilot-core:0.1.0")
    implementation("io.github.huongstienstra.permissionpilot:permissionpilot-compose:0.1.0")
    testImplementation("io.github.huongstienstra.permissionpilot:permissionpilot-test:0.1.0")
}
```

Until the first release is published, use the repo locally:

```kotlin
// settings.gradle.kts
includeBuild("../PermissionPilot")
```

## Compose Usage

```kotlin
@Composable
fun CameraScreen() {
    val permission = rememberPermissionPilotState(PermissionUseCase.Camera)

    PermissionGate(
        state = permission,
        denied = { state ->
            Button(onClick = state::request) {
                Text("Allow camera")
            }
        },
        permanentlyDenied = { state ->
            Button(onClick = state::openAppSettings) {
                Text("Open settings")
            }
        },
    ) {
        CameraContent()
    }
}
```

PermissionPilot refreshes state when the app resumes, so returning from Settings updates the UI.

## Supported Use Cases

```kotlin
PermissionUseCase.Camera
PermissionUseCase.Microphone
PermissionUseCase.Notifications
PermissionUseCase.FineLocation
PermissionUseCase.CoarseLocation
PermissionUseCase.NearbyBluetooth
PermissionUseCase.PhotoLibrary
PermissionUseCase.Raw(setOf(...))
```

The app still owns its AndroidManifest declarations. Use `PermissionResolver` to inspect required manifest and runtime permissions:

```kotlin
val resolution = PermissionResolver.resolve(PermissionUseCase.PhotoLibrary)

resolution.runtimePermissions
resolution.manifestPermissions
resolution.recommendation
```

## Testing Support

Use `FakePermissionController` in ViewModel or domain tests:

```kotlin
val permissions = FakePermissionController(
    mapOf(PermissionUseCase.Camera to PermissionStatus.Granted),
)

check(permissions.statusFor(PermissionUseCase.Camera).allowsAccess)
```

## Sample App

Run the sample:

```bash
ANDROID_HOME=$HOME/Library/Android/sdk ./gradlew :sample:installDebug
```

The sample includes:

- edge-to-edge Compose UI
- runtime permission summary cards
- request and settings recovery actions
- permission recommendations for Bluetooth and media access

## Verification

Run checks and build the UI test APK:

```bash
ANDROID_HOME=$HOME/Library/Android/sdk ./gradlew check :sample:assembleDebugAndroidTest
```

Run Compose UI tests on an attached emulator or device:

```bash
ANDROID_HOME=$HOME/Library/Android/sdk ./gradlew :sample:connectedDebugAndroidTest
```

QA evidence lives in [qa/EVIDENCE.md](qa/EVIDENCE.md), including screenshots for app flows and Android permission dialogs.

## Publishing

Publishing is configured for Maven Central under:

```text
io.github.huongstienstra.permissionpilot
```

Release instructions live in [docs/PUBLISHING.md](docs/PUBLISHING.md).

## Roadmap

- Android lint rules for manifest and API-level permission mistakes.
- More granular media use cases for images, video, and selected-only access.
- Additional instrumented tests for settings return and partial media access.

## License

Apache License 2.0. See [LICENSE](LICENSE).
