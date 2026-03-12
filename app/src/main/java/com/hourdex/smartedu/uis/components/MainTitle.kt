package com.hourdex.smartedu.uis.components

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hourdex.smartedu.R
import com.kyant.backdrop.Backdrop
import com.kyant.backdrop.drawBackdrop
import com.kyant.backdrop.effects.blur
import com.kyant.backdrop.effects.lens
import com.kyant.backdrop.effects.vibrancy
import dev.chrisbanes.haze.HazeProgressive
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.hazeEffect

@Composable
fun BoxScope.MainTitle(
    modifier: Modifier = Modifier,
    text: String,
    hazeState: HazeState,
    backdrop: Backdrop?,
    onBack: () -> Unit = {},
    animatedVisibilityScope: AnimatedVisibilityScope,
    sharedTransitionScope: SharedTransitionScope
) {
    Box(
        modifier
            .align(Alignment.TopCenter)
            .fillMaxWidth()
            .height(150.dp)
            .hazeEffect(hazeState) {
                progressive = HazeProgressive.verticalGradient(
                    preferPerformance = true,
                    startIntensity = 1f,
                    endIntensity = 0f
                )
            },
        contentAlignment = Alignment.Center
    ) {

        if (backdrop != null) {
            Row(
                Modifier.fillMaxWidth().padding(horizontal = 15.dp)
            ) {
                Box(
                    Modifier.size(40.dp)
                        .drawBackdrop(
                        backdrop = backdrop,
                        shape = { CircleShape },
                        effects = {
                            blur(6f)
                            vibrancy()
                            lens(16.dp.toPx(),20.dp.toPx(), chromaticAberration = true, depthEffect = true)
                        }
                        )
                        .clip(shape = CircleShape)
                        .clickable(
                            onClick = onBack
                        )
                    ,
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurface),
                        painter = painterResource(R.drawable.round_arrow_back_ios_new_24),contentDescription = null)
                }
            }
        }
        Text(
            modifier = with(sharedTransitionScope) {Modifier.sharedBounds(
                rememberSharedContentState("main_title"),
                animatedVisibilityScope
            )},
            text = text, fontSize = 24.sp, fontWeight = FontWeight.SemiBold,color = MaterialTheme.colorScheme.onSurface)
    }
}