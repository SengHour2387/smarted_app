package com.hourdex.smartedu.features.dashboard

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kyant.backdrop.Backdrop
import com.kyant.backdrop.drawBackdrop
import com.kyant.backdrop.effects.blur
import com.kyant.backdrop.effects.lens
import com.kyant.backdrop.effects.vibrancy
import com.kyant.capsule.ContinuousRoundedRectangle

@Composable
fun ActionTile(
    text: String,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
    onClick: () -> Unit,
    icon: Painter,
    backdrop: Backdrop,
    color: Color = MaterialTheme.colorScheme.primary
) {
    Box(
        modifier =with(sharedTransitionScope)  { Modifier
            .sharedBounds(
                resizeMode = SharedTransitionScope.ResizeMode.scaleToBounds(),
                sharedContentState = rememberSharedContentState("action_tile_$text"),
                animatedVisibilityScope = animatedVisibilityScope
            )
            .drawBackdrop(
                backdrop = backdrop,
                shape = { ContinuousRoundedRectangle(15.dp)},
                onDrawSurface = {drawRect(color.copy(0.2f))},
                effects = {
                    blur(6f)
                    vibrancy()
                    lens(24f,24f, chromaticAberration = true, depthEffect = false)
                }
            )
            .clip(ContinuousRoundedRectangle(15.dp))
            .clickable(
                onClick = onClick
            )
            .border(width = 2.dp, color = color.copy(0.7f), shape = ContinuousRoundedRectangle(15.dp))
            .padding(horizontal = 10.dp, vertical = 5.dp)},
        contentAlignment = Alignment.Center
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(15.dp)
        ) {
            Image(icon, contentDescription = null, colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurface))
            Text(text = text, color = MaterialTheme.colorScheme.onBackground, fontSize = 18.sp)
        }
    }
}