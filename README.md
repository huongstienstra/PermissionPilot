# PermissionPilot

PermissionPilot is a Kotlin-first Android permissions toolkit for apps that need predictable permission flows across API versions.

The library focuses on use-case based permission requests instead of raw permission strings:

```kotlin
val cameraPermission = rememberPermissionPilotState(PermissionUseCase.Camera)

PermissionGate(state = cameraPermission) {
    CameraScreen()
}
```

## Modules

- `permissionpilot-core`: permission use cases, API-level mapping, and status models.
- `permissionpilot-compose`: Compose state and UI gate helpers.
- `permissionpilot-test`: fake permission controller for tests.
- `sample`: Compose sample app.

## Gradle Usage

After the first Maven Central release:

```kotlin
dependencies {
    implementation("com.vyvien.permissionpilot:permissionpilot-core:0.1.0")
    implementation("com.vyvien.permissionpilot:permissionpilot-compose:0.1.0")
    testImplementation("com.vyvien.permissionpilot:permissionpilot-test:0.1.0")
}
```

Publishing notes live in [docs/PUBLISHING.md](docs/PUBLISHING.md).

## Current Scope

PermissionPilot v1 starts with common modern Android permissions:

- Camera
- Microphone
- Notifications
- Fine and coarse location
- Nearby Bluetooth
- Photo and video library

Lint checks are planned after the initial API is stable.
