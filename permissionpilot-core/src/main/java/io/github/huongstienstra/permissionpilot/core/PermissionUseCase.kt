package io.github.huongstienstra.permissionpilot.core

/**
 * Describes the feature-level access an app needs.
 *
 * PermissionPilot maps these use cases to the right runtime permissions for the
 * device API level, keeping call sites away from version-specific permission names.
 */
sealed interface PermissionUseCase {
    data object Camera : PermissionUseCase
    data object Microphone : PermissionUseCase
    data object Notifications : PermissionUseCase
    data object FineLocation : PermissionUseCase
    data object CoarseLocation : PermissionUseCase
    data object NearbyBluetooth : PermissionUseCase
    data object PhotoLibrary : PermissionUseCase

    data class Raw(
        val permissions: Set<String>,
    ) : PermissionUseCase
}
