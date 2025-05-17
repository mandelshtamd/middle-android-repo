package com.example.androidpracticumcustomview.ui.theme

import android.animation.ObjectAnimator
import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import android.widget.FrameLayout

/*
Задание:
Реализуйте необходимые компоненты;
Создайте проверку что дочерних элементов не более 2-х;
Предусмотрите обработку ошибок рендера дочерних элементов.
Задание по желанию:
Предусмотрите параметризацию длительности анимации.
 */

class CustomContainer @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : FrameLayout(context, attrs) {

    private companion object {
        private const val ALPHA_ANIMATION_DURATION = 2000L
        private const val DELAY_FOR_SECOND_VIEW = 2000L
        private const val TRANSLATION_ANIMATION_DURATION = 5000L
        private const val TRANSPARENT_VISIBILITY_ALPHA = 0F
        private const val MAX_CHILDREN = 2
        private const val CONTAINER_EXCEPTION_MESSAGE = "The maximum child count for container is 2."
    }

    init {
        setWillNotDraw(false)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val width = MeasureSpec.getSize(widthMeasureSpec)
        val height = MeasureSpec.getSize(heightMeasureSpec)
        setMeasuredDimension(width, height)
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        val parentWidth = right - left
        val parentHeight = bottom - top

        if (childCount > 0) {
            val firstChild = getChildAt(0)
            firstChild.layout(0, 0, parentWidth, parentHeight / 2)
        }

        if (childCount > 1) {
            val secondChild = getChildAt(1)
            secondChild.layout(0, parentHeight / 2, parentWidth, parentHeight)
        }
    }

    override fun addView(child: View) {
        if (childCount >= MAX_CHILDREN) {
            throw IllegalStateException(CONTAINER_EXCEPTION_MESSAGE)
        }

        with(child) {
            alpha = TRANSPARENT_VISIBILITY_ALPHA
            super.addView(this)
            animateView(this, childCount - 1)
        }
    }

    private fun animateView(view: View, viewIndex: Int) {
        val containerHeight = this.height.toFloat()

        val startYTranslation = when (viewIndex) {
            0 -> containerHeight / 4f
            1 -> -containerHeight / 4f
            else -> { throw IllegalStateException(CONTAINER_EXCEPTION_MESSAGE) }
        }

        view.translationY = startYTranslation

        ObjectAnimator.ofFloat(view, ALPHA, 0f, 1f).apply {
            interpolator = AccelerateInterpolator()
            duration = ALPHA_ANIMATION_DURATION
            if (viewIndex == 1) {
                startDelay = DELAY_FOR_SECOND_VIEW
            }
            start()
        }

        ObjectAnimator.ofFloat(view, TRANSLATION_Y, startYTranslation, 0f).apply {
            interpolator = DecelerateInterpolator()
            duration = TRANSLATION_ANIMATION_DURATION
            if (viewIndex == 1) {
                startDelay = DELAY_FOR_SECOND_VIEW
            }
            start()
        }
    }
}