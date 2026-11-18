package com.abbas.haydayproducts

import android.app.Service
import android.content.Intent
import android.graphics.PixelFormat
import android.os.IBinder
import android.provider.Settings
import android.view.Gravity
import android.view.WindowManager
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Toast

class FloatingControlService : Service() {
    private var windowManager: WindowManager? = null
    private var panel: LinearLayout? = null

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (!Settings.canDrawOverlays(this)) {
            stopSelf()
            return START_NOT_STICKY
        }
        if (panel == null) showPanel()
        return START_NOT_STICKY
    }

    private fun showPanel() {
        windowManager = getSystemService(WINDOW_SERVICE) as WindowManager
        val layout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(8, 8, 8, 8)
            setBackgroundColor(0xEE244B2B.toInt())
        }
        layout.addView(Button(this).apply {
            text = "▶ تشغيل"
            isAllCaps = false
            setOnClickListener {
                Toast.makeText(this@FloatingControlService, "محرك التشغيل الآلي قيد التطوير", Toast.LENGTH_SHORT).show()
            }
        })
        layout.addView(Button(this).apply {
            text = "■ إيقاف / إخفاء"
            isAllCaps = false
            setOnClickListener { stopSelf() }
        })
        val params = WindowManager.LayoutParams(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
            PixelFormat.TRANSLUCENT
        ).apply {
            gravity = Gravity.TOP or Gravity.END
            x = 12
            y = 160
        }
        panel = layout
        windowManager?.addView(layout, params)
    }

    override fun onDestroy() {
        panel?.let { view -> runCatching { windowManager?.removeView(view) } }
        panel = null
        windowManager = null
        super.onDestroy()
    }
}
