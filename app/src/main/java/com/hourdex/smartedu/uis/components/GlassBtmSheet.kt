package com.hourdex.smartedu.uis.components


import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.kyant.backdrop.Backdrop
import com.kyant.backdrop.backdrops.rememberLayerBackdrop
import com.kyant.backdrop.drawBackdrop
import com.kyant.backdrop.effects.blur
import com.kyant.backdrop.effects.lens
import com.kyant.backdrop.effects.vibrancy
import com.kyant.capsule.ContinuousRoundedRectangle

@Composable
fun BoxScope.GlassBottomSheet(
    modifier: Modifier = Modifier,
    backdrop: Backdrop,
    content: @Composable () -> Unit
    ) {
    val bottomSheetBackdrop = rememberLayerBackdrop()
    val color = MaterialTheme.colorScheme.background
    Column(
        modifier
            .drawBackdrop(
                backdrop = backdrop,
                shape = { ContinuousRoundedRectangle(32.dp) },
                effects = {
                    vibrancy()
                    blur(4f.dp.toPx())
                    lens(24f.dp.toPx(), 48f.dp.toPx(), true,chromaticAberration = true)
                },
                exportedBackdrop = bottomSheetBackdrop,
                onDrawSurface = { drawRect(color.copy(alpha = 0.5f)) }
            )
            .fillMaxWidth()
            .align(Alignment.BottomCenter)
            .padding(8.dp)
    ) {
        content.invoke()
    }
}
