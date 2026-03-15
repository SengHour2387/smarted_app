package com.hourdex.smartedu.features.teachers

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.kyant.backdrop.backdrops.layerBackdrop
import dev.chrisbanes.haze.hazeSource

@Composable
fun TeacherList(
    modifier: Modifier = Modifier,
    widths: List<Dp>,
    teachers: List<TeacherRes>,
    onTeacherClick: (teacher: TeacherRes) -> Unit,
    onTeacherDelete: (teacher: TeacherRes) -> Unit,
    contentPaddingValues: PaddingValues = PaddingValues(
        top = 150.dp,
        start = 15.dp,
        end = 15.dp,
        bottom = 100.dp
    )
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = contentPaddingValues
    ) {


        item {
            TeachersHeader(widths)
        }

        items(teachers) { teacher ->
            TeacherRow(
                teacher = teacher,
                widths = widths,
                onClick = { onTeacherClick(teacher)},
                onClickDelete = { onTeacherDelete(teacher) }
            )
        }
    }
}