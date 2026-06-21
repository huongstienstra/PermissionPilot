package io.github.huongstienstra.permissionpilot.sample

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import io.github.huongstienstra.permissionpilot.compose.PermissionGate
import io.github.huongstienstra.permissionpilot.compose.PermissionPilotState
import io.github.huongstienstra.permissionpilot.compose.rememberPermissionPilotState
import io.github.huongstienstra.permissionpilot.core.PermissionStatus
import io.github.huongstienstra.permissionpilot.core.PermissionUseCase

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PermissionPilotSampleApp()
        }
    }
}

@Composable
private fun PermissionPilotSampleApp() {
    MaterialTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background,
        ) {
            PermissionLab()
        }
    }
}

@Composable
@OptIn(ExperimentalLayoutApi::class)
private fun PermissionLab() {
    val useCases = remember {
        listOf(
            PermissionUseCase.Camera,
            PermissionUseCase.Microphone,
            PermissionUseCase.Notifications,
            PermissionUseCase.FineLocation,
            PermissionUseCase.CoarseLocation,
            PermissionUseCase.NearbyBluetooth,
            PermissionUseCase.PhotoLibrary,
        )
    }
    var selectedUseCase by remember { mutableStateOf<PermissionUseCase>(PermissionUseCase.Camera) }
    val permissionState = rememberPermissionPilotState(selectedUseCase)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Text(
            text = "PermissionPilot",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
        )
        Text(
            text = "Select a use case and inspect the permissions PermissionPilot resolves for this device.",
            style = MaterialTheme.typography.bodyMedium,
        )

        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            useCases.forEach { useCase ->
                FilterChip(
                    selected = useCase == selectedUseCase,
                    onClick = { selectedUseCase = useCase },
                    label = { Text(useCase.label()) },
                )
            }
        }

        PermissionSummaryCard(permissionState)
        PermissionGate(
            state = permissionState,
            denied = { state ->
                PermissionActionCard(
                    title = "Permission is not granted",
                    message = "Request access when the user chooses a feature that needs ${state.useCase.label()}.",
                    primaryAction = "Request",
                    onPrimaryAction = state::request,
                    secondaryAction = "Refresh",
                    onSecondaryAction = state::refresh,
                )
            },
            permanentlyDenied = { state ->
                PermissionActionCard(
                    title = "Permission is denied",
                    message = "Open app settings if the user wants to grant access after denying the prompt.",
                    primaryAction = "Open settings",
                    onPrimaryAction = state::openAppSettings,
                    secondaryAction = "Refresh",
                    onSecondaryAction = state::refresh,
                )
            },
            partial = { state, status ->
                PermissionActionCard(
                    title = "Partial access",
                    message = "Granted: ${status.grantedPermissions.size}. Denied: ${status.deniedPermissions.size}.",
                    primaryAction = "Request missing",
                    onPrimaryAction = state::request,
                    secondaryAction = "Refresh",
                    onSecondaryAction = state::refresh,
                )
            },
        ) {
            ElevatedCard(modifier = Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    Text(
                        text = "Access ready",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                    )
                    Text(
                        text = "${selectedUseCase.label()} can proceed.",
                        style = MaterialTheme.typography.bodyMedium,
                    )
                }
            }
        }
    }
}

@Composable
private fun PermissionSummaryCard(state: PermissionPilotState) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            Text(
                text = state.useCase.label(),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.SemiBold,
            )
            Text(
                text = "Status: ${state.status.label()}",
                style = MaterialTheme.typography.bodyLarge,
            )
            Text(
                text = "Runtime permissions",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold,
            )
            if (state.requiredPermissions.isEmpty()) {
                Text("No runtime permission is required on this API level.")
            } else {
                state.requiredPermissions.forEach { permission ->
                    Text(text = permission)
                }
            }
            state.recommendation?.let { recommendation ->
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = recommendation.message,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold,
                )
                recommendation.alternative?.let { alternative ->
                    Text(
                        text = alternative,
                        style = MaterialTheme.typography.bodyMedium,
                    )
                }
            }
        }
    }
}

@Composable
private fun PermissionActionCard(
    title: String,
    message: String,
    primaryAction: String,
    onPrimaryAction: () -> Unit,
    secondaryAction: String,
    onSecondaryAction: () -> Unit,
) {
    ElevatedCard(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
            )
            Text(
                text = message,
                style = MaterialTheme.typography.bodyMedium,
            )
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(onClick = onPrimaryAction) {
                    Text(primaryAction)
                }
                OutlinedButton(onClick = onSecondaryAction) {
                    Text(secondaryAction)
                }
            }
        }
    }
}

private fun PermissionUseCase.label(): String {
    return when (this) {
        PermissionUseCase.Camera -> "Camera"
        PermissionUseCase.Microphone -> "Microphone"
        PermissionUseCase.Notifications -> "Notifications"
        PermissionUseCase.FineLocation -> "Fine location"
        PermissionUseCase.CoarseLocation -> "Coarse location"
        PermissionUseCase.NearbyBluetooth -> "Nearby Bluetooth"
        PermissionUseCase.PhotoLibrary -> "Photo library"
        is PermissionUseCase.Raw -> "Raw permissions"
    }
}

private fun PermissionStatus.label(): String {
    return when (this) {
        PermissionStatus.Denied -> "Denied"
        PermissionStatus.Granted -> "Granted"
        PermissionStatus.NotRequired -> "Not required"
        PermissionStatus.PermanentlyDenied -> "Permanently denied"
        is PermissionStatus.Partial -> "Partial"
    }
}
