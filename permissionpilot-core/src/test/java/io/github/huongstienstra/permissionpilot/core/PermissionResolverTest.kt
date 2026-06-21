package io.github.huongstienstra.permissionpilot.core

import android.Manifest
import android.os.Build
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class PermissionResolverTest {
    @Test
    fun notificationsAreNotRuntimePermissionsBeforeAndroid13() {
        val permissions = PermissionResolver.runtimePermissionsFor(
            useCase = PermissionUseCase.Notifications,
            sdkInt = Build.VERSION_CODES.S_V2,
        )

        assertTrue(permissions.isEmpty())
    }

    @Test
    fun notificationsUsePostNotificationsOnAndroid13AndNewer() {
        val permissions = PermissionResolver.runtimePermissionsFor(
            useCase = PermissionUseCase.Notifications,
            sdkInt = Build.VERSION_CODES.TIRAMISU,
        )

        assertEquals(setOf(Manifest.permission.POST_NOTIFICATIONS), permissions)
    }

    @Test
    fun nearbyBluetoothUsesNearbyDevicesOnAndroid12AndNewer() {
        val permissions = PermissionResolver.runtimePermissionsFor(
            useCase = PermissionUseCase.NearbyBluetooth,
            sdkInt = Build.VERSION_CODES.S,
        )

        assertEquals(
            setOf(
                Manifest.permission.BLUETOOTH_SCAN,
                Manifest.permission.BLUETOOTH_CONNECT,
            ),
            permissions,
        )
    }

    @Test
    fun photoLibraryIncludesSelectedAccessOnAndroid14AndNewer() {
        val permissions = PermissionResolver.runtimePermissionsFor(
            useCase = PermissionUseCase.PhotoLibrary,
            sdkInt = Build.VERSION_CODES.UPSIDE_DOWN_CAKE,
        )

        assertEquals(
            setOf(
                Manifest.permission.READ_MEDIA_IMAGES,
                Manifest.permission.READ_MEDIA_VIDEO,
                Manifest.permission.READ_MEDIA_VISUAL_USER_SELECTED,
            ),
            permissions,
        )
    }
}
