package com.vyvien.permissionpilot.core

data class RequiredPermission(
    val name: String,
    val minSdk: Int = 1,
    val maxSdk: Int? = null,
    val usesPermissionFlags: String? = null,
)
