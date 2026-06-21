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

## Current Scope

PermissionPilot v1 starts with common modern Android permissions:

- Camera
- Microphone
- Notifications
- Fine and coarse location
- Nearby Bluetooth
- Photo and video library

Lint checks and publishing automation are planned after the initial API is stable.
