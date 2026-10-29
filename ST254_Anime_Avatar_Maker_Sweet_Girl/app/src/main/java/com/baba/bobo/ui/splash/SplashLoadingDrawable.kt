package com.baba.bobo.ui.splash

import android.content.res.Resources
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.ColorFilter
import android.graphics.Paint
import android.graphics.PixelFormat
import android.graphics.drawable.Drawable

/** The 240 x 16 dp segmented loading bar from the splash reference. */
class SplashLoadingDrawable : Drawable() {
    private val density = Resources.getSystem().displayMetrics.density
    private val paint = Paint().apply { isAntiAlias = false }

    override fun draw(canvas: Canvas) {
        val area = bounds
        val left = area.left.toFloat()
        val top = area.top.toFloat()
        val width = area.width().toFloat()
        val height = area.height().toFloat()
        val border = density

        paint.color = Color.BLACK
        canvas.drawRect(left, top, left + width, top + height, paint)
        paint.color = Color.WHITE
        canvas.drawRect(left + border, top + border, left + width - border, top + height - border, paint)

        paint.color = Color.rgb(255, 102, 127)
        val filledSegments = (level * 20 + 9999) / 10000
        val segmentWidth = (width - 6 * density - 19 * 2 * density) / 20
        for (segment in 0 until filledSegments) {
            val segmentLeft = left + 3 * density + segment * (segmentWidth + 2 * density)
            canvas.drawRect(
                segmentLeft,
                top + 3 * density,
                segmentLeft + segmentWidth,
                top + height - 3 * density,
                paint
            )
        }
    }

    override fun onLevelChange(level: Int): Boolean {
        invalidateSelf()
        return true
    }

    override fun setAlpha(alpha: Int) {
        paint.alpha = alpha
        invalidateSelf()
    }

    override fun setColorFilter(colorFilter: ColorFilter?) {
        paint.colorFilter = colorFilter
        invalidateSelf()
    }

    @Deprecated("Deprecated in Java")
    override fun getOpacity(): Int = PixelFormat.OPAQUE
}
