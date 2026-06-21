package io.github.huongstienstra.permissionpilot.core

interface PermissionController {
    fun statusFor(useCase: PermissionUseCase): PermissionStatus
}
