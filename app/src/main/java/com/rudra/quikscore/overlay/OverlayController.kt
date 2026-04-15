package com.rudra.quikscore.overlay

import android.content.Context
import androidx.core.content.ContextCompat
import dagger.hilt.android.qualifiers.ApplicationContext
import com.rudra.quikscore.model.MatchesItem
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class OverlayController @Inject constructor(
    @ApplicationContext
    private val context: Context,
    private val permissionManager: OverlayPermissionManager
) {
    fun canDrawOverlays(): Boolean = permissionManager.hasPermission()

    fun startOverlay(match: MatchesItem? = null): Boolean {
        if (!permissionManager.hasPermission()) return false

        ContextCompat.startForegroundService(
            context,
            OverlayService.createStartIntent(context, match)
        )
        return true
    }

    fun requestOverlayPermission() {
        permissionManager.openPermissionSettings()
    }

    fun stopOverlay() {
        ContextCompat.startForegroundService(
            context,
            OverlayService.createStopIntent(context)
        )
    }

    fun isOverlayRunning(): Boolean = OverlayService.isOverlayVisible
}
