package com.chkan.bestpractices.custom_view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.Path
import android.graphics.Shader
import android.text.TextPaint
import android.util.AttributeSet
import android.view.View
import androidx.annotation.ColorInt
import kotlin.math.PI


class ArcView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var fullSize = 0f
    private var halfSize = 0f

    private var textResult = ""
    private var colorResult = Color.TRANSPARENT
    private var hOffsetForText = 0f
    private var vOffsetForText = 0f

    private val pathArc = Path()
    private val pathCircleInCenter = Path()
    private val pathText = Path()

    private val paintArc = Paint()

    private val paintText = TextPaint(Paint.ANTI_ALIAS_FLAG).apply { //сглаживает кривые при рисовании
        color = Color.WHITE
        style = Paint.Style.FILL_AND_STROKE
        letterSpacing = 0.1f
        textSize = 25f
    }

    fun setTextAndColor(text: String, @ColorInt colorArc: Int){
        textResult = if (text.length > 13) calculateText(text) else text
        colorResult = colorArc
        hOffsetForText = calculateHOffset(textResult)
        invalidate()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        fullSize = MeasureSpec.getSize(widthMeasureSpec).toFloat()
        halfSize = fullSize/ 2f
        vOffsetForText = -(fullSize * 0.04f)
        hOffsetForText = calculateHOffset(textResult)
    }

    // дергается в начале и после изменения размера родителя (здесь уже есть померянные размеры с onMesure)
    // поэтому здесь инициализируем обьекты для канваса где нужны размер родителя
    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        paintArc.apply {
            color = colorResult
            shader = LinearGradient(0f,fullSize, halfSize,halfSize, colorResult, Color.TRANSPARENT, Shader.TileMode.CLAMP)
        }
        pathCircleInCenter.addCircle(halfSize,halfSize,fullSize/3,Path.Direction.CW)
        pathArc.apply {
            addCircle(halfSize,halfSize,halfSize,Path.Direction.CW)
            op(pathCircleInCenter,Path.Op.DIFFERENCE)
        }
        pathText.addCircle(halfSize,halfSize,halfSize,Path.Direction.CCW)
    }

    override fun onDraw(canvas: Canvas) {
        canvas.drawPath(pathArc, paintArc)
        canvas.drawTextOnPath(textResult, pathText, hOffsetForText,vOffsetForText, paintText)
    }

    private fun calculateText(text: String): String {
        return text.substring(0,11) + "..."
    }

    private fun calculateHOffset(text: String): Float {
        val lenghtCircle = 2f * PI * halfSize // s = 2 π r
        val widthText = paintText.measureText(text)
        return lenghtCircle.toFloat() - (lenghtCircle.toFloat() / 4f) - (lenghtCircle.toFloat() / 8f) - (widthText / 2f)
    }

}