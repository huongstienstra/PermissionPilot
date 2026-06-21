package io.github.huongstienstra.permissionpilot.test

import io.github.huongstienstra.permissionpilot.core.PermissionController
import io.github.huongstienstra.permissionpilot.core.PermissionStatus
import io.github.huongstienstra.permissionpilot.core.PermissionUseCase

class FakePermissionController(
    initialStatuses: Map<PermissionUseCase, PermissionStatus> = emptyMap(),
    private val defaultStatus: PermissionStatus = PermissionStatus.Denied,
) : PermissionController {
    private val statuses = initialStatuses.toMutableMap()

    override fun statusFor(useCase: PermissionUseCase): PermissionStatus {
        return statuses[useCase] ?: defaultStatus
    }

    fun grant(useCase: PermissionUseCase) {
        statuses[useCase] = PermissionStatus.Granted
    }

    fun deny(useCase: PermissionUseCase) {
        statuses[useCase] = PermissionStatus.Denied
    }

    fun permanentlyDeny(useCase: PermissionUseCase) {
        statuses[useCase] = PermissionStatus.PermanentlyDenied
    }

    fun clear(useCase: PermissionUseCase) {
        statuses.remove(useCase)
    }
}
