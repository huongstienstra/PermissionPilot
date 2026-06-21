package com.vyvien.permissionpilot.compose

import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.vyvien.permissionpilot.core.PermissionRecommendation
import com.vyvien.permissionpilot.core.PermissionStatus
import com.vyvien.permissionpilot.core.PermissionUseCase

@Stable
class PermissionPilotState internal constructor(
    val useCase: PermissionUseCase,
) {
    var status: PermissionStatus by mutableStateOf(PermissionStatus.Denied)
        internal set

    var requiredPermissions: Set<String> by mutableStateOf(emptySet())
        internal set

    var recommendation: PermissionRecommendation? by mutableStateOf(null)
        internal set

    var hasRequested: Boolean by mutableStateOf(false)
        internal set

    val allowsAccess: Boolean
        get() = status.allowsAccess

    private var requestAction: () -> Unit = {}
    private var refreshAction: () -> Unit = {}
    private var openSettingsAction: () -> Unit = {}

    fun request() {
        requestAction()
    }

    fun refresh() {
        refreshAction()
    }

    fun openAppSettings() {
        openSettingsAction()
    }

    internal fun bind(
        requestAction: () -> Unit,
        refreshAction: () -> Unit,
        openSettingsAction: () -> Unit,
    ) {
        this.requestAction = requestAction
        this.refreshAction = refreshAction
        this.openSettingsAction = openSettingsAction
    }

    internal fun markRequested() {
        hasRequested = true
    }
}
