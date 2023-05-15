package com.robert.passwordmanager.ui.health_indicator

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.animation.LinearInterpolator
import androidx.core.content.res.ResourcesCompat
import com.robert.passwordmanager.R
import kotlin.math.min

class CustomHealthIndicator(context: Context, attrs: AttributeSet?): View(context, attrs) {
    private var backgroundIndicatorColor = ResourcesCompat.getColor(resources, R.color.light_grey, null)
    private var foregroundIndicatorColor = ResourcesCompat.getColor(resources, R.color.blue_500, null)
    private var foregroundIndicatorSweepAngle = 0f
    private var previousHealthScore: Float = 0f
    private var currentHealthScore: Float = 0f
    private var backgroundStrokeWidth = 20f
    private var centerX = width.toFloat() / 2
    private var centerY = height.toFloat() / 2
    private var radius = min(width, height) / 2
    private var sweepAngle = 0f

    private var backgroundPaint = Paint().apply {
        color = backgroundIndicatorColor
        style = Paint.Style.STROKE
        isAntiAlias = true
        strokeWidth = 40f
        strokeCap = Paint.Cap.ROUND
    }
    private var foregroundPaint = Paint().apply {
        color = foregroundIndicatorColor
        style = Paint.Style.STROKE
        isAntiAlias = true
        strokeWidth = 40f
        strokeCap = Paint.Cap.ROUND
    }

    private var textPaint = Paint().apply {
        color = Color.WHITE
        isAntiAlias = true
        textSize = 100f
        textAlign = Paint.Align.CENTER
    }

    private var labelTextPaint = Paint().apply {
        color = Color.WHITE
        textSize = 40f
        isAntiAlias = true
        textAlign = Paint.Align.CENTER
    }

    private var sweepAngleAnimator = ValueAnimator.ofFloat(0.0f, 1.0f).apply {
        duration = 2000 // set the animation duration
        interpolator = LinearInterpolator()
        addUpdateListener { animator->
            val value = animator.animatedValue as Float
            sweepAngle = foregroundIndicatorSweepAngle * value
            invalidate()
        }
    }

    fun setHealthScore(score: Float){
        foregroundIndicatorSweepAngle = 240 * score
        currentHealthScore = score
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

        val text = "${(sweepAngle / 240 * 100).toInt()}%"
        canvas?.drawText(text, (width/2).toFloat(), (height/2).toFloat(), textPaint)
        canvas?.drawText("Health Score", (width/2).toFloat(), (height*0.8).toFloat(), labelTextPaint)
    }
}