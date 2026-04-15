package com.rudra.quikscore.overlay

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class OverlayPermissionManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    fun hasPermission(): Boolean {
        return Settings.canDrawOverlays(context)
    }

    fun createPermissionSettingsIntent(): Intent {
        return Intent(
            Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
            Uri.parse("package:${context.packageName}")
        ).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
    }

    fun openPermissionSettings() {
        context.startActivity(createPermissionSettingsIntent())
    }
}
