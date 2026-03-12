package com.hourdex.smartedu.features.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kyant.backdrop.Backdrop
import com.kyant.backdrop.drawBackdrop
import com.kyant.backdrop.effects.blur
import com.kyant.backdrop.effects.lens
import com.kyant.backdrop.effects.vibrancy
import com.kyant.capsule.ContinuousRoundedRectangle

@Composable
fun SummaryCard(
    modifier: Modifier = Modifier,
    title: String,
    value: String,
    backdrop: Backdrop,
    color: Color  = MaterialTheme.colorScheme.primaryContainer,
    icon: @Composable RowScope.() -> Unit,
    onClick: () -> Unit
) {
    Column(
        modifier = modifier.width(150.dp).height(100.dp)
            .drawBackdrop(
                backdrop = backdrop,
                shape = { RoundedCornerShape(26)},
                onDrawSurface = {drawRect(color.copy(0.7f))},
                effects = {
                    blur(6f)
                    vibrancy()
                    lens(24f,24f, chromaticAberration = true, depthEffect = false)
                }
            )
            .clip(RoundedCornerShape(26))
            .clickable(onClick = onClick),
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(value, fontSize = 24.sp, color = MaterialTheme.colorScheme.onSecondaryContainer, fontWeight = FontWeight.SemiBold)
        Text(title, fontSize = 24.sp, color = MaterialTheme.colorScheme.onSecondaryContainer, fontWeight = FontWeight.Bold)
    }
}