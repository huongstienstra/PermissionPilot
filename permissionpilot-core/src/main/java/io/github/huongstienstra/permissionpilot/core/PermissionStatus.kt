package io.github.huongstienstra.permissionpilot.core

sealed interface PermissionStatus {
    val allowsAccess: Boolean

    data object Granted : PermissionStatus {
        override val allowsAccess = true
    }

    data object NotRequired : PermissionStatus {
        override val allowsAccess = true
    }

    data object Denied : PermissionStatus {
        override val allowsAccess = false
    }

    data object PermanentlyDenied : PermissionStatus {
        override val allowsAccess = false
    }

    data class Partial(
        val grantedPermissions: Set<String>,
        val deniedPermissions: Set<String>,
    ) : PermissionStatus {
        override val allowsAccess = false
    }
}
