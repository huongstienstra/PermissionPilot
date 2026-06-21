# QA Evidence

Date: June 21, 2026

## Environment

- App package: `com.vyvien.permissionpilot.sample`
- Device: `emulator-5554`
- Emulator model: `sdk_gphone64_arm64`
- Android version: `15`
- Gradle test target: `Pixel_7_Pro_API_35(AVD)`

## Automated UI Tests

Command:

```bash
ANDROID_HOME=$HOME/Library/Android/sdk ./gradlew :sample:connectedDebugAndroidTest
```

Result:

```text
Starting 3 tests on Pixel_7_Pro_API_35(AVD) - 15
Finished 3 tests on Pixel_7_Pro_API_35(AVD) - 15
BUILD SUCCESSFUL
```

Report:

- [Connected Android test report](../sample/build/reports/androidTests/connected/debug/index.html)
- [PermissionLabTest report](../sample/build/reports/androidTests/connected/debug/com.vyvien.permissionpilot.sample.PermissionLabTest.html)

Covered UI assertions:

- Permission lab renders the initial camera state.
- Selecting Photo library updates the summary.
- Denied state shows the action card with Request and Refresh actions.

## App Flow Screenshots

These screenshots show each PermissionPilot use case selected in the sample app before opening the Android permission dialog.

| Flow | Evidence |
| --- | --- |
| Camera | [01-camera.png](screenshots/01-camera.png) |
| Microphone | [02-microphone.png](screenshots/02-microphone.png) |
| Notifications | [03-notifications.png](screenshots/03-notifications.png) |
| Fine location | [04-fine-location.png](screenshots/04-fine-location.png) |
| Coarse location | [05-coarse-location.png](screenshots/05-coarse-location.png) |
| Nearby Bluetooth | [06-nearby-bluetooth.png](screenshots/06-nearby-bluetooth.png) |
| Photo library | [07-photo-library.png](screenshots/07-photo-library.png) |

## Android Permission Dialog Screenshots

These screenshots show the OS permission prompt after tapping Request for each use case. The app was reset between flows with `pm clear` so each prompt was captured from a clean state.

| Flow | Evidence |
| --- | --- |
| Camera request | [01-camera-request.png](screenshots/request-dialogs/01-camera-request.png) |
| Microphone request | [02-microphone-request.png](screenshots/request-dialogs/02-microphone-request.png) |
| Notifications request | [03-notifications-request.png](screenshots/request-dialogs/03-notifications-request.png) |
| Fine location request | [04-fine-location-request.png](screenshots/request-dialogs/04-fine-location-request.png) |
| Coarse location request | [05-coarse-location-request.png](screenshots/request-dialogs/05-coarse-location-request.png) |
| Nearby Bluetooth request | [06-nearby-bluetooth-request.png](screenshots/request-dialogs/06-nearby-bluetooth-request.png) |
| Photo library request | [07-photo-library-request.png](screenshots/request-dialogs/07-photo-library-request.png) |

## Notes

- Screenshot captures are full emulator resolution: `1440 x 3120`.
- Permission dialog copy and button order are OS-controlled and may differ by Android version, OEM skin, locale, and target SDK.
- The sample app correctly uses edge-to-edge rendering while preserving safe drawing insets for content.
