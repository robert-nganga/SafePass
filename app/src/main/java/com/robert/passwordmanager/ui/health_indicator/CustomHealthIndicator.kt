package com.robert.passwordmanager.ui.health_indicator

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import kotlin.math.min

class CustomHealthIndicator @JvmOverloads constructor(context: Context,
                                                      attrs: AttributeSet? = null,
                                                      defStyleAttr: Int = 0): View(context, attrs, defStyleAttr) {
    private var backgroundIndicatorColor = Color.GRAY
    private var backgroundStrokeWidth = 20f
    private var centerX = width.toFloat() / 2
    private var centerY = height.toFloat() / 2
    private var radius = min(width, height) / 2
    private var paint = Paint().apply {
        color = Color.GRAY
        style = Paint.Style.STROKE
        strokeWidth = 30f
        strokeCap = Paint.Cap.ROUND
    }

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.drawArc((width * 0.05).toFloat(), (height * 0.05).toFloat(),
            (width/1.05).toFloat(), (height/1.05).toFloat(), 150f, 240f, false, paint)
    }
}