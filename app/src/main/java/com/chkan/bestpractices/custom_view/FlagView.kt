package com.chkan.bestpractices.custom_view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import com.chkan.bestpractices.R

class FlagView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    // Rect для рисования прямоугольников
    private val rowRect = Rect()
    private val flagPartHeight = 100
    private val startMargin = 100

    // Для линии (ручка флага)
    private val linePaint = Paint().apply {
        strokeWidth = 5F
        color = ContextCompat.getColor(context, R.color.black)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val width = MeasureSpec.getSize(widthMeasureSpec) - 100
        val heightSpecSize = MeasureSpec.getSize(heightMeasureSpec)
        val height = when (MeasureSpec.getMode(heightMeasureSpec)) {
            // Нас никто не ограничивает - занимаем размер контента
            MeasureSpec.UNSPECIFIED -> 600
            // Ограничение "не больше, не меньше" - занимаем столько, сколько пришло в спеке
            MeasureSpec.EXACTLY -> heightSpecSize
            // Можно занять меньше места, чем пришло в спеке, но не больше
            MeasureSpec.AT_MOST -> 600
            // Успокаиваем компилятор, сюда не попадем
            else -> error("Unreachable")
        }
        setMeasuredDimension(width, height)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        // Размер изменился, надо пересчитать ширину
        rowRect.set(startMargin, 0, w, flagPartHeight)
    }

    override fun onDraw(canvas: Canvas) = with(canvas) {
        drawFlag()
    }

    private fun Canvas.drawFlag() {
        val topPartPaint = Paint().apply { color = ContextCompat.getColor(context, R.color.flag_blue) }
        val bottomPartPaint = Paint().apply { color = ContextCompat.getColor(context, R.color.flag_yellow) }
        //рисуем верхнюю часть флага
        drawRect(rowRect, topPartPaint)
        //сдвигаем вниз прямоугольник
        rowRect.offsetTo(startMargin, flagPartHeight)
        //рисуем нижнюю часть флага
        drawRect(rowRect, bottomPartPaint)
        //рисуем ручку флага
        drawLine(startMargin.toFloat(), 0f , startMargin.toFloat(), 300f, linePaint)
    }

}