package com.carriez.flutter_hbb

import android.content.Context
import android.graphics.Color
import android.graphics.PixelFormat
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.util.TypedValue
import android.view.Gravity
import android.view.WindowManager
import android.widget.TextView

/**
 * Soporte SIA: franja arriba mientras soporte esta conectado, para que el guarda no vea
 * el equipo "moviendose solo". No se puede tocar (los toques pasan a la app de abajo) y
 * tambien sale en la imagen que ve soporte. Se pone al autorizar una sesion de pantalla
 * y se quita con la ultima (stop_capture) o si el servicio muere.
 */
object AvisoMantenimiento {
    private const val TEXTO = "Mantenimiento de la citofonía en curso"
    private val main = Handler(Looper.getMainLooper())
    private var vista: TextView? = null

    fun mostrar(ctx: Context) = main.post {
        if (vista != null || !Settings.canDrawOverlays(ctx)) return@post
        val wm = ctx.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val t = TextView(ctx).apply {
            text = TEXTO
            setTextColor(Color.WHITE)
            setBackgroundColor(Color.parseColor("#E6F59E0B"))
            setTextSize(TypedValue.COMPLEX_UNIT_SP, 15f)
            gravity = Gravity.CENTER
            val p = (10 * resources.displayMetrics.density).toInt()
            setPadding(p, p, p, p)
        }
        val lp = WindowManager.LayoutParams(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE or
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or
                WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN,
            PixelFormat.TRANSLUCENT
        ).apply { gravity = Gravity.TOP }
        try { wm.addView(t, lp); vista = t } catch (e: Exception) {}
    }

    fun quitar(ctx: Context) = main.post {
        val v = vista ?: return@post
        try { (ctx.getSystemService(Context.WINDOW_SERVICE) as WindowManager).removeView(v) } catch (e: Exception) {}
        vista = null
    }
}
