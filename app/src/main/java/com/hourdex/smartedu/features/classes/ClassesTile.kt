package com.hourdex.smartedu.features.classes

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hourdex.smartedu.R
import com.hourdex.smartedu.uis.components.randomPastelColor

@Composable
fun ClassesTile(
    classesRes: ClassesRes,
    isSelectable: Boolean = false,
    onClick:()-> Unit = {},
    onClickEdit: () -> Unit = {},
    onClickDelete: () -> Unit = {},
) {
    Box(
        Modifier.padding(5.dp)
            .fillMaxWidth().height(120.dp)
            .clickable(
                enabled = isSelectable,
                onClick = onClick
            )
            .clip(RoundedCornerShape(24))
            .background(color = MaterialTheme.colorScheme.surfaceContainer)
            ,
        contentAlignment = Alignment.CenterStart

    ) {

        Image(
            modifier = Modifier.matchParentSize(),
            contentScale = ContentScale.FillWidth,
            alignment = Alignment.BottomCenter,
            colorFilter = ColorFilter.tint(Color(randomPastelColor())),
            painter = painterResource(R.drawable.rectangle_154_2), contentDescription = null
        )

        Column(
            modifier = Modifier
                .padding(20.dp)
                .fillMaxHeight(),
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            Text(text = classesRes.name, fontSize = 24.sp, fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.onSurface)
            Text( " Grade: "+ classesRes.grade_level.toString(), color = MaterialTheme.colorScheme.onSurface)
        }
        Column(
            modifier = Modifier
                .padding(10.dp)
                .fillMaxHeight()
                .align(Alignment.CenterEnd),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.End
        ) {
            if(!isSelectable) {
                Row(
                    modifier =Modifier.clip(CircleShape)
                        .background(MaterialTheme.colorScheme.surface).padding(5.dp).padding(1.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .padding(2.dp)
                            .width(24.dp).height(24.dp)
                            .clip(CircleShape)
                            .clickable(onClick = onClickDelete)

                    ) {
                        Image(
                            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurface),
                            painter = painterResource(R.drawable.style_bulk_7),contentDescription = null)
                    }
                    Box(
                        modifier = Modifier
                            .padding(2.dp)
                            .width(24.dp).height(24.dp)
                            .clip(CircleShape)
                            .clickable(onClick = onClickEdit)
                    ) {
                        Image(
                            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurface),
                            painter = painterResource(R.drawable.style_bulk_6),contentDescription = null)
                    }
                }
            }
            Row(
                verticalAlignment = Alignment.Bottom
            ) {
                Text( "Capacity: ", color = MaterialTheme.colorScheme.onBackground)
                Text(text = "${classesRes.capacity}", fontSize = 24.sp, fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.onSurface)
            }
        }
    }
}