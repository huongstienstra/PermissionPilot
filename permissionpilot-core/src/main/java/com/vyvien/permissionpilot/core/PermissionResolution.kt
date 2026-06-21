package com.vyvien.permissionpilot.core

data class PermissionResolution(
    val useCase: PermissionUseCase,
    val runtimePermissions: Set<String>,
    val manifestPermissions: List<RequiredPermission>,
    val recommendation: PermissionRecommendation? = null,
)

data class PermissionRecommendation(
    val message: String,
    val alternative: String? = null,
)
