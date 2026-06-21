package com.vyvien.permissionpilot.core

interface PermissionController {
    fun statusFor(useCase: PermissionUseCase): PermissionStatus
}
