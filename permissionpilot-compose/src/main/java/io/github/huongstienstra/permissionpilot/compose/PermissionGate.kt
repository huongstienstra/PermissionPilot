package io.github.huongstienstra.permissionpilot.compose

import androidx.compose.runtime.Composable
import io.github.huongstienstra.permissionpilot.core.PermissionStatus

@Composable
fun PermissionGate(
    state: PermissionPilotState,
    denied: @Composable (PermissionPilotState) -> Unit = {},
    permanentlyDenied: @Composable (PermissionPilotState) -> Unit = denied,
    partial: @Composable (PermissionPilotState, PermissionStatus.Partial) -> Unit = { permissionState, _ ->
        denied(permissionState)
    },
    content: @Composable () -> Unit,
) {
    when (val status = state.status) {
        PermissionStatus.Granted,
        PermissionStatus.NotRequired,
        -> content()
        PermissionStatus.Denied -> denied(state)
        PermissionStatus.PermanentlyDenied -> permanentlyDenied(state)
        is PermissionStatus.Partial -> partial(state, status)
    }
}
