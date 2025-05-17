package com.example.androidpracticumcustomview.ui.theme

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


/*
Задание:
Реализуйте необходимые компоненты;
Создайте проверку что дочерних элементов не более 2-х;
Предусмотрите обработку ошибок рендера дочерних элементов.
Задание по желанию:
Предусмотрите параметризацию длительности анимации.
 */
@Composable
fun CustomContainerCompose(
    firstChild: @Composable (() -> Unit)?,
    secondChild: @Composable (() -> Unit)?
) {
    val delayForSecondChildMillis = 2000L

    val firstChildAnimatedHeight = remember { Animatable(0f) }
    val secondChildAnimatedHeight = remember { Animatable(0f) }
    val firstChildAlpha = remember { Animatable(0f) }
    val secondChildAlpha = remember { Animatable(0f) }

    val screenHeight = LocalConfiguration.current.screenHeightDp
    val screenHeightPx = LocalDensity.current.run { screenHeight.dp.toPx() }

    // Блок активации анимации при первом запуске
    LaunchedEffect(Unit) {
        launch {
            firstChildAlpha.animateTo(
                targetValue = 1f,
                animationSpec = tween(durationMillis = 2000)
            )
        }

        launch {
            firstChildAnimatedHeight.animateTo(
                targetValue = -screenHeightPx / 4f,
                animationSpec = tween(durationMillis = 5000)
            )
        }

        launch {
            delay(delayForSecondChildMillis)
            secondChildAlpha.animateTo(
                targetValue = 1f,
                animationSpec = tween(durationMillis = 2000)
            )
        }

        launch {
            delay(delayForSecondChildMillis)
            secondChildAnimatedHeight.animateTo(
                targetValue = screenHeightPx / 4f,
                animationSpec = tween(durationMillis = 5000)
            )
        }
    }

    // Основной контейнер
    Box {
        Box(
            Modifier
                .graphicsLayer(
                    alpha = firstChildAlpha.value,
                    translationY = firstChildAnimatedHeight.value
                )
                .height((screenHeight / 2).dp)
        ) {
            firstChild?.invoke()
        }

        Box(
            Modifier
                .graphicsLayer(
                    alpha = secondChildAlpha.value,
                    translationY = secondChildAnimatedHeight.value
                )
                .height((screenHeight / 2).dp)
        ) {
            secondChild?.invoke()
        }
    }
}