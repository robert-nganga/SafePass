package com.robert.passwordmanager.ui.health_indicator

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.LinearInterpolator
import androidx.core.content.res.ResourcesCompat
import com.robert.passwordmanager.R
import kotlin.math.min

class CustomHealthIndicator(context: Context, attrs: AttributeSet?): View(context, attrs) {
    private var backgroundIndicatorColor = ResourcesCompat.getColor(resources, R.color.light_grey, null)
    private var foregroundIndicatorColor = ResourcesCompat.getColor(resources, R.color.blue_500, null)
    private var backgroundStrokeWidth = 20f
    private var centerX = width.toFloat() / 2
    private var centerY = height.toFloat() / 2
    private var radius = min(width, height) / 2
    private var sweepAngle = 0f

    private var backgroundPaint = Paint().apply {
        color = backgroundIndicatorColor
        style = Paint.Style.STROKE
        strokeWidth = 30f
        strokeCap = Paint.Cap.ROUND
    }
    private var foregroundPaint = Paint().apply {
        color = foregroundIndicatorColor
        style = Paint.Style.STROKE
        strokeWidth = 30f
        strokeCap = Paint.Cap.ROUND
    }

    private var textPaint = Paint().apply {
        color = foregroundIndicatorColor
        style = Paint.Style.STROKE
        strokeWidth = 18f
        strokeCap = Paint.Cap.ROUND
    }
    private var sweepAngleAnimator = ValueAnimator.ofFloat(0.0f, 1.0f).apply {
        duration = 1000 // set the animation duration
        interpolator = LinearInterpolator()
        addUpdateListener { animator->
            val value = animator.animatedValue as Float
            sweepAngle = 180 * value
            Log.i("CustomHealthIndicator", value.toString())
            invalidate()
        }
    }

    fun setForegroundSweepAngle(angle: Float){
        sweepAngleAnimator.start()
    }


    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.drawArc((width * 0.10).toFloat(), (height * 0.10).toFloat(),
            (width/1.10).toFloat(), height.toFloat(), 150f, 240f, false, backgroundPaint)

        //foreground indicator
        canvas?.drawArc((width * 0.10).toFloat(), (height * 0.10).toFloat(),
            (width/1.10).toFloat(), height.toFloat(), 150f, sweepAngle, false, foregroundPaint)

    }
}