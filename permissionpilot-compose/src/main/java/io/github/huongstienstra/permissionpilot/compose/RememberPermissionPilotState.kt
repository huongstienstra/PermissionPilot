package io.github.huongstienstra.permissionpilot.compose

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import io.github.huongstienstra.permissionpilot.core.PermissionResolver
import io.github.huongstienstra.permissionpilot.core.PermissionStatus
import io.github.huongstienstra.permissionpilot.core.PermissionUseCase

@Composable
fun rememberPermissionPilotState(
    useCase: PermissionUseCase,
): PermissionPilotState {
    val context = LocalContext.current
    val activity = context.findActivity()
    val lifecycleOwner = LocalLifecycleOwner.current
    val resolution = remember(useCase) {
        PermissionResolver.resolve(useCase)
    }
    val state = remember(useCase) {
        PermissionPilotState(useCase)
    }

    fun currentStatus(): PermissionStatus {
        val requiredPermissions = resolution.runtimePermissions
        if (requiredPermissions.isEmpty()) {
            return PermissionStatus.NotRequired
        }

        val grantedPermissions = requiredPermissions.filterTo(mutableSetOf()) { permission ->
            ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED
        }
        val deniedPermissions = requiredPermissions - grantedPermissions

        return when {
            deniedPermissions.isEmpty() -> PermissionStatus.Granted
            grantedPermissions.isNotEmpty() -> PermissionStatus.Partial(
                grantedPermissions = grantedPermissions,
                deniedPermissions = deniedPermissions,
            )
            state.hasRequested && activity != null && deniedPermissions.all { permission ->
                !ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)
            } -> PermissionStatus.PermanentlyDenied
            else -> PermissionStatus.Denied
        }
    }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions(),
    ) {
        state.markRequested()
        state.status = currentStatus()
    }

    SideEffect {
        state.requiredPermissions = resolution.runtimePermissions
        state.recommendation = resolution.recommendation
        state.status = currentStatus()
        state.bind(
            requestAction = {
                state.markRequested()
                if (resolution.runtimePermissions.isEmpty()) {
                    state.status = PermissionStatus.NotRequired
                } else {
                    launcher.launch(resolution.runtimePermissions.toTypedArray())
                }
            },
            refreshAction = {
                state.status = currentStatus()
            },
            openSettingsAction = {
                val intent = Intent(
                    Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                    Uri.fromParts("package", context.packageName, null),
                ).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                context.startActivity(intent)
            },
        )
    }

    DisposableEffect(lifecycleOwner, useCase) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                state.refresh()
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    return state
}
