package io.github.huongstienstra.permissionpilot.core

import android.annotation.SuppressLint
import android.Manifest
import android.os.Build

@SuppressLint("InlinedApi")
object PermissionResolver {
    fun resolve(
        useCase: PermissionUseCase,
        sdkInt: Int = Build.VERSION.SDK_INT,
    ): PermissionResolution {
        val runtimePermissions = runtimePermissionsFor(useCase, sdkInt)
        val manifestPermissions = manifestPermissionsFor(useCase)
        val recommendation = recommendationFor(useCase, sdkInt)

        return PermissionResolution(
            useCase = useCase,
            runtimePermissions = runtimePermissions,
            manifestPermissions = manifestPermissions,
            recommendation = recommendation,
        )
    }

    fun runtimePermissionsFor(
        useCase: PermissionUseCase,
        sdkInt: Int = Build.VERSION.SDK_INT,
    ): Set<String> {
        return when (useCase) {
            PermissionUseCase.Camera -> setOf(Manifest.permission.CAMERA)
            PermissionUseCase.Microphone -> setOf(Manifest.permission.RECORD_AUDIO)
            PermissionUseCase.Notifications -> if (sdkInt >= Build.VERSION_CODES.TIRAMISU) {
                setOf(Manifest.permission.POST_NOTIFICATIONS)
            } else {
                emptySet()
            }
            PermissionUseCase.FineLocation -> setOf(Manifest.permission.ACCESS_FINE_LOCATION)
            PermissionUseCase.CoarseLocation -> setOf(Manifest.permission.ACCESS_COARSE_LOCATION)
            PermissionUseCase.NearbyBluetooth -> if (sdkInt >= Build.VERSION_CODES.S) {
                setOf(
                    Manifest.permission.BLUETOOTH_SCAN,
                    Manifest.permission.BLUETOOTH_CONNECT,
                )
            } else {
                setOf(Manifest.permission.ACCESS_FINE_LOCATION)
            }
            PermissionUseCase.PhotoLibrary -> when {
                sdkInt >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE -> setOf(
                    Manifest.permission.READ_MEDIA_IMAGES,
                    Manifest.permission.READ_MEDIA_VIDEO,
                    Manifest.permission.READ_MEDIA_VISUAL_USER_SELECTED,
                )
                sdkInt >= Build.VERSION_CODES.TIRAMISU -> setOf(
                    Manifest.permission.READ_MEDIA_IMAGES,
                    Manifest.permission.READ_MEDIA_VIDEO,
                )
                else -> setOf(Manifest.permission.READ_EXTERNAL_STORAGE)
            }
            is PermissionUseCase.Raw -> useCase.permissions
        }
    }

    fun manifestPermissionsFor(useCase: PermissionUseCase): List<RequiredPermission> {
        return when (useCase) {
            PermissionUseCase.Camera -> listOf(
                RequiredPermission(Manifest.permission.CAMERA),
            )
            PermissionUseCase.Microphone -> listOf(
                RequiredPermission(Manifest.permission.RECORD_AUDIO),
            )
            PermissionUseCase.Notifications -> listOf(
                RequiredPermission(
                    name = Manifest.permission.POST_NOTIFICATIONS,
                    minSdk = Build.VERSION_CODES.TIRAMISU,
                ),
            )
            PermissionUseCase.FineLocation -> listOf(
                RequiredPermission(Manifest.permission.ACCESS_FINE_LOCATION),
            )
            PermissionUseCase.CoarseLocation -> listOf(
                RequiredPermission(Manifest.permission.ACCESS_COARSE_LOCATION),
            )
            PermissionUseCase.NearbyBluetooth -> listOf(
                RequiredPermission(
                    name = Manifest.permission.BLUETOOTH,
                    maxSdk = Build.VERSION_CODES.R,
                ),
                RequiredPermission(
                    name = Manifest.permission.BLUETOOTH_ADMIN,
                    maxSdk = Build.VERSION_CODES.R,
                ),
                RequiredPermission(
                    name = Manifest.permission.ACCESS_FINE_LOCATION,
                    maxSdk = Build.VERSION_CODES.R,
                ),
                RequiredPermission(
                    name = Manifest.permission.BLUETOOTH_SCAN,
                    minSdk = Build.VERSION_CODES.S,
                    usesPermissionFlags = "neverForLocation",
                ),
                RequiredPermission(
                    name = Manifest.permission.BLUETOOTH_CONNECT,
                    minSdk = Build.VERSION_CODES.S,
                ),
            )
            PermissionUseCase.PhotoLibrary -> listOf(
                RequiredPermission(
                    name = Manifest.permission.READ_EXTERNAL_STORAGE,
                    maxSdk = Build.VERSION_CODES.S_V2,
                ),
                RequiredPermission(
                    name = Manifest.permission.READ_MEDIA_IMAGES,
                    minSdk = Build.VERSION_CODES.TIRAMISU,
                ),
                RequiredPermission(
                    name = Manifest.permission.READ_MEDIA_VIDEO,
                    minSdk = Build.VERSION_CODES.TIRAMISU,
                ),
                RequiredPermission(
                    name = Manifest.permission.READ_MEDIA_VISUAL_USER_SELECTED,
                    minSdk = Build.VERSION_CODES.UPSIDE_DOWN_CAKE,
                ),
            )
            is PermissionUseCase.Raw -> useCase.permissions.map { permission ->
                RequiredPermission(permission)
            }
        }
    }

    private fun recommendationFor(
        useCase: PermissionUseCase,
        sdkInt: Int,
    ): PermissionRecommendation? {
        return when {
            useCase == PermissionUseCase.PhotoLibrary && sdkInt >= Build.VERSION_CODES.TIRAMISU -> {
                PermissionRecommendation(
                    message = "Request media permissions only when broad library access is required.",
                    alternative = "Use Android Photo Picker for user-selected media.",
                )
            }
            useCase == PermissionUseCase.NearbyBluetooth && sdkInt >= Build.VERSION_CODES.S -> {
                PermissionRecommendation(
                    message = "Use Nearby Devices permissions for Bluetooth on Android 12 and newer.",
                    alternative = "Set BLUETOOTH_SCAN neverForLocation when scan results are not used for location.",
                )
            }
            else -> null
        }
    }
}
