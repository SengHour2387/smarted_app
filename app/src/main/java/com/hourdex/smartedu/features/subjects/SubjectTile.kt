package com.hourdex.smartedu.features.subjects

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
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
import com.hourdex.smartedu.R

@Composable
fun SubjectTileAdmin(
    hasEdit: Boolean = false,
    subjectsRes: SubjectsRes,
    onClickEdit: (id:Long) -> Unit = {},
    onClickDelete: (id:Long) -> Unit = {}
) {

    Row(
        Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clip(RoundedCornerShape(35))
            .background(color = MaterialTheme.colorScheme.surfaceContainer)
            .padding(10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(subjectsRes.name, Modifier.weight(1f), fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSecondaryContainer)
        Text(subjectsRes.code, Modifier.weight(1f),color = MaterialTheme.colorScheme.onSecondaryContainer)
        if(hasEdit) {
                Image(
                    modifier = Modifier.clip(CircleShape).clickable(onClick = {onClickEdit(subjectsRes.id)}).padding(8.dp),
                    colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurface),
                    painter = painterResource(R.drawable.style_bulk_6), contentDescription = null)

        }
        Spacer(Modifier.width(5.dp))
        if(hasEdit) {
                Image(
                    modifier = Modifier.clip(CircleShape).clickable(onClick = {onClickDelete(subjectsRes.id)}).padding(8.dp),
                    colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurface),
                    painter = painterResource(R.drawable.style_bulk_7), contentDescription = null)

        }
    }
}