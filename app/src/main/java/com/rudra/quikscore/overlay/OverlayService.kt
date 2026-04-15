package com.rudra.quikscore.overlay

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.pm.ServiceInfo
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.graphics.PixelFormat
import android.graphics.Rect
import android.view.Gravity
import android.view.WindowManager
import android.widget.FrameLayout
import androidx.core.app.NotificationCompat
import androidx.core.app.ServiceCompat
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.setViewTreeLifecycleOwner
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.savedstate.setViewTreeSavedStateRegistryOwner
import androidx.savedstate.SavedStateRegistry
import androidx.savedstate.SavedStateRegistryController
import androidx.savedstate.SavedStateRegistryOwner
import com.rudra.quikscore.MainActivity
import com.rudra.quikscore.R
import com.rudra.quikscore.model.MatchesItem
import com.rudra.quikscore.presentation.component.FloatingOverlayCard
import com.rudra.quikscore.presentation.component.OverlayScoreState
import com.rudra.quikscore.ui.theme.AppTheme
import com.rudra.quikscore.util.dpToPx
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OverlayService : LifecycleService() {

    private val windowManager by lazy { getSystemService(Context.WINDOW_SERVICE) as WindowManager }
    private var overlayRoot: FrameLayout? = null
    private var isShowing = false
    private var viewTreeOwner: OverlayViewTreeOwner? = null

    private val layoutParams: WindowManager.LayoutParams by lazy {
        WindowManager.LayoutParams().apply {
            width = WindowManager.LayoutParams.WRAP_CONTENT
            height = WindowManager.LayoutParams.WRAP_CONTENT
            gravity = Gravity.TOP or Gravity.START
            format = PixelFormat.TRANSLUCENT
            flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or
                WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
            type = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
            } else {
                @Suppress("DEPRECATION")
                WindowManager.LayoutParams.TYPE_PHONE
            }
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            ACTION_STOP -> stopOverlayInternal()
            ACTION_START,
            null -> startOverlayInternal(intent)
        }

        return START_REDELIVER_INTENT
    }

    override fun onDestroy() {
        hideOverlay()
        isOverlayVisible = false
        super.onDestroy()
    }

    override fun onBind(intent: Intent): IBinder? {
        return super.onBind(intent)
    }

    private fun startOverlayInternal(intent: Intent?) {
        if (!SettingsHelper.canDrawOverlays(this)) {
            stopSelf()
            return
        }

        createNotificationChannel()
        ServiceCompat.startForeground(
            this,
            NOTIFICATION_ID,
            createNotification(),
            ServiceInfo.FOREGROUND_SERVICE_TYPE_SPECIAL_USE
        )

        val match = intent?.parcelableExtra(EXTRA_MATCH)
        showOverlay(match)
        isOverlayVisible = true
    }

    private fun stopOverlayInternal() {
        hideOverlay()
        isOverlayVisible = false
        ServiceCompat.stopForeground(this, ServiceCompat.STOP_FOREGROUND_REMOVE)
        stopSelf()
    }

    private fun showOverlay(match: MatchesItem?) {
        hideOverlay()
        val root = createOverlayRoot(match).also { overlayRoot = it }

        runCatching {
            windowManager.addView(root, layoutParams)
            isShowing = true
        }
    }

    private fun hideOverlay() {
        val root = overlayRoot ?: return
        if (!isShowing) return

        runCatching {
            windowManager.removeViewImmediate(root)
        }
        isShowing = false
        overlayRoot = null
        viewTreeOwner?.onStop()
        viewTreeOwner?.onDestroy()
        viewTreeOwner = null
    }

    private fun createOverlayRoot(match: MatchesItem?): FrameLayout {
        val root = FrameLayout(this)
        val owner = viewTreeOwner ?: OverlayViewTreeOwner().also {
            it.onCreate()
            it.onStart()
            viewTreeOwner = it
        }
        root.setViewTreeLifecycleOwner(owner)
        root.setViewTreeSavedStateRegistryOwner(owner)

        val composeView = ComposeView(this).apply {
            layoutParams = FrameLayout.LayoutParams(
                dpToPx(336),
                FrameLayout.LayoutParams.WRAP_CONTENT
            )
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnDetachedFromWindow)
            setViewTreeLifecycleOwner(owner)
            setViewTreeSavedStateRegistryOwner(owner)
            setContent {
                AppTheme {
                    FloatingOverlayCard(
                        state = OverlayScoreState.from(match),
                        onCloseClick = { stopOverlayInternal() },
                        onDrag = ::moveOverlayBy
                    )
                }
            }
        }

        root.addView(composeView)
        return root
    }

    private fun moveOverlayBy(deltaX: Float, deltaY: Float) {
        val root = overlayRoot ?: return
        if (!isShowing) return

        val visibleFrame = Rect().also { root.getWindowVisibleDisplayFrame(it) }
        layoutParams.x = (layoutParams.x + deltaX.toInt())
            .coerceIn(0, maxOf(0, visibleFrame.width() - root.width))
        layoutParams.y = (layoutParams.y + deltaY.toInt())
            .coerceIn(0, maxOf(0, visibleFrame.height() - root.height))

        windowManager.updateViewLayout(root, layoutParams)
    }

    private fun createNotification(): Notification {
        val openAppIntent = PendingIntent.getActivity(
            this,
            1,
            Intent(this, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            },
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        val stopIntent = createStopIntent(this)
        val stopPendingIntent = PendingIntent.getService(
            this,
            0,
            stopIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        return NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_icon)
            .setContentTitle(getString(R.string.app_name))
            .setContentText("Overlay is running")
            .setOnlyAlertOnce(true)
            .setOngoing(true)
            .setCategory(NotificationCompat.CATEGORY_SERVICE)
            .setContentIntent(openAppIntent)
            .addAction(R.drawable.ic_icon, "Stop", stopPendingIntent)
            .build()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) return

        val channel = NotificationChannel(
            NOTIFICATION_CHANNEL_ID,
            "Overlay",
            NotificationManager.IMPORTANCE_LOW
        )

        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.createNotificationChannel(channel)
    }

    private fun Intent.parcelableExtra(key: String): MatchesItem? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            getParcelableExtra(key, MatchesItem::class.java)
        } else {
            @Suppress("DEPRECATION")
            getParcelableExtra(key)
        }
    }

    companion object {
        const val ACTION_START = "com.rudra.quikscore.overlay.action.START"
        const val ACTION_STOP = "com.rudra.quikscore.overlay.action.STOP"
        const val EXTRA_MATCH = "com.rudra.quikscore.overlay.extra.MATCH"
        const val NOTIFICATION_CHANNEL_ID = "overlay_channel"
        const val NOTIFICATION_ID = 1001

        @Volatile
        var isOverlayVisible: Boolean = false
            private set

        fun createStartIntent(context: Context, match: MatchesItem?): Intent {
            return Intent(context, OverlayService::class.java).apply {
                action = ACTION_START
                if (match != null) {
                    putExtra(EXTRA_MATCH, match)
                }
            }
        }

        fun createStopIntent(context: Context): Intent {
            return Intent(context, OverlayService::class.java).apply {
                action = ACTION_STOP
            }
        }
    }
}

private object SettingsHelper {
    fun canDrawOverlays(context: Context): Boolean {
        return android.provider.Settings.canDrawOverlays(context)
    }
}

private class OverlayViewTreeOwner : SavedStateRegistryOwner {
    private val lifecycleRegistry = LifecycleRegistry(this)
    private val savedStateRegistryController = SavedStateRegistryController.create(this)

    override val lifecycle: Lifecycle
        get() = lifecycleRegistry

    override val savedStateRegistry: SavedStateRegistry
        get() = savedStateRegistryController.savedStateRegistry

    fun onCreate() {
        savedStateRegistryController.performRestore(null)
        lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_CREATE)
    }

    fun onStart() {
        lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_START)
    }

    fun onStop() {
        lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_STOP)
    }

    fun onDestroy() {
        lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    }
}
