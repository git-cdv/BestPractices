package com.chkan.bestpractices.custom_view

import android.animation.ValueAnimator
import android.animation.TypeEvaluator
import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.view.animation.BaseInterpolator
import android.widget.TextView
import androidx.core.content.withStyledAttributes
import com.chkan.bestpractices.R
import java.lang.Integer.max
import kotlin.math.roundToInt

////https://github.com/silverxcoins/CustomView

class CustomViewGroup @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ViewGroup(context, attrs, defStyleAttr) {

    private val firstChild: View?
        get() = if (childCount > 0) getChildAt(0) else null
    private val secondChild: View?
        get() = if (childCount > 1) getChildAt(1) else null

    private var verticalOffset = 0

    ////////БЛОК ПОСТРОЕНИЯ ViewGroup и вьюх в ней

    init {
        //вытаскиваем значения атрибутов заданных через XML (зарегены в res/values/attrs.xml)
        context.withStyledAttributes(attrs, R.styleable.CustomViewGroup, defStyleAttr) {
            verticalOffset = getDimensionPixelOffset(R.styleable.CustomViewGroup_verticalOffset, 0)
        }
    }

    //здесь меряется вьюха, как аргументы приходят от родителя ограничения по ширине и высоте
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        checkChildCount()

        //меряем чалды чтобы понимать сколько места мы займем
        //здесь учитываем только высоту, подразумевая скролящийся экран
        firstChild?.let { measureChild(it, widthMeasureSpec) }
        secondChild?.let { measureChild(it, widthMeasureSpec) }

        //когда в measureChild померялись и установились ограничения мы можем получить размеры чалдов
        val firstWidth = firstChild?.measuredWidth ?: 0
        val firstHeight = firstChild?.measuredHeight ?: 0
        val secondWidth = secondChild?.measuredWidth ?: 0
        val secondHeight = secondChild?.measuredHeight ?: 0

        //размер и моде от родителя
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)

        //проверяем вмещаются ли чалды в одну строку
        val childrenOnSameLine = firstWidth + secondWidth < widthSize || widthMode == MeasureSpec.UNSPECIFIED

        val width = when (widthMode) {
            MeasureSpec.UNSPECIFIED -> firstWidth + secondWidth
            MeasureSpec.EXACTLY -> widthSize

            MeasureSpec.AT_MOST -> {
                //если поместились то сумма ширин, если нет - то ширина самой широкой вью
                if (childrenOnSameLine) {
                    firstWidth + secondWidth
                } else {
                    max(firstWidth, secondWidth)
                }
            }

            else -> error("Unreachable")
        }

        val height = if (childrenOnSameLine) {
            max(firstHeight, secondHeight)
        } else {
            firstHeight + secondHeight  + verticalOffset
        }
        //устанавливаем размеры для ViewGroup
        setMeasuredDimension(width, height)
    }

    //здесь располагаем вью-чалды внутри вьюгруп по координатам (начиная с верхего левого угла)
    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        firstChild?.layout(
            0, //left
            0, //top
            firstChild?.measuredWidth ?: 0, //right
            firstChild?.measuredHeight ?: 0 //bottom
        )
        //r,l - это крайние координаты которые мы можем зянять по ширине во ВЬЮ ГРУПП!!!
        //r-l - высчитываем ширину, которую можем занять ВСЕГО
        //r-l-(secondChild?.measuredWidth ?: 0) - высчитываем левые координаты
        //b-t-(secondChild?.measuredHeight ?: 0) - высчитываем верхние координаты
        secondChild?.layout(
            r - l - (secondChild?.measuredWidth ?: 0),
            b - t - (secondChild?.measuredHeight ?: 0),
            r - l,
            b - t
        )
    }

    private fun measureChild(child: View, widthMeasureSpec: Int) {
        //MeasureSpec состоит из размера и моды (правила размещения)
        //получаем через MeasureSpec.getSize(widthMeasureSpec) и MeasureSpec.getMode(widthMeasureSpec)
        val specSize = MeasureSpec.getSize(widthMeasureSpec)

        //UNSPECIFIED - никак не ограниченны
        //EXACTLY - должны занять столько сколько пришло в Size (типа точный размер или match_parent)
        //AT_MOST - Size это макс размер, может быть меньше, но не больше (типа wrap_content)
        val childWidthSpec = when (MeasureSpec.getMode(widthMeasureSpec)) {
            MeasureSpec.UNSPECIFIED -> widthMeasureSpec
            MeasureSpec.AT_MOST -> widthMeasureSpec
            MeasureSpec.EXACTLY -> MeasureSpec.makeMeasureSpec(specSize, MeasureSpec.AT_MOST)
            else -> error("Unreachable")
        }
        val childHeightSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED)

        //задаем ограничения для каждой чалд вьюхи
        child.measure(childWidthSpec, childHeightSpec)
    }

    private fun checkChildCount() {
        if (childCount > 2) error("CustomViewGroup should not contain more than 2 children")
    }

    ////////БЛОК Анимации
    //НО ЭТО ПЛОХОЙ ПРИМЕР С ТЕКСТОМ из-за того что используется TextView для анимации, а в нем чтобы показать изменения
    //нужно полностью перерисовать вью со всеми расчетами размеров (это очень дорого) - лучше использовать Edit Text
    // или пример кастомной текстовой вью из класса CustomTextView

    private val animator = ValueAnimator
            ///эвалюейтор, начальное и конечное значение
        .ofObject(StringEvaluator(), "Привет", "Привет! Как дела? Как настроение?")
        .apply {
            duration = 4000L
            repeatCount = ValueAnimator.INFINITE
            repeatMode = ValueAnimator.REVERSE
            interpolator = TwoStepsInterpolator()

            addUpdateListener { animator -> //действие при каждом тике анимации
                val animatedValue = animator.animatedValue.toString()
                (firstChild as? TextView)?.text = animatedValue
            }
        }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        animator.start()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        animator.cancel()
    }

    //или так, если анимация тяжелая и ее надо закрывать даже при скрыввании экрана
 /*   override fun onVisibilityAggregated(isVisible: Boolean) {
        super.onVisibilityAggregated(isVisible)
        if (isVisible) animator.start() else animator.cancel()
    }*/

}

//расчитывает конкретное состояние обьекта анимации в зависимости от прогресса анимации
class StringEvaluator : TypeEvaluator<String> {
    override fun evaluate(fraction: Float, startValue: String, endValue: String): String {
        val coercedFraction = fraction.coerceIn(0f, 1f)

        val lengthDiff = endValue.length - startValue.length
        val currentDiff = (lengthDiff * coercedFraction).roundToInt()
        return if (currentDiff > 0) {
            endValue.substring(0, startValue.length + currentDiff)
        } else {
            startValue.substring(0, startValue.length + currentDiff)
        }
    }
}
//кастомный интерполятор (определяет как анимация распределенна по времени), есть много дефолтных
class TwoStepsInterpolator : BaseInterpolator() {
    // input - прогресс анимации по времени
    // return прогресс анимируемого значения значения. 0 -> начальное значение, 1 -> конечное значение
    //в данном примере: первая половина анимации за 30% времени, стоит на месте - 40% времени, и остаток тоже за 30% времени
    override fun getInterpolation(input: Float): Float {
        return when {
            input < 0.3f -> 0.5f * (input / 0.3f)
            input > 0.7f -> 0.5f + (0.5f * (input - 0.7f) / 0.3f)
            else -> 0.5f
        }
    }
}