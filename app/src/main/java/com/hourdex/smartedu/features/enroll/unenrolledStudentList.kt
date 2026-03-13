package com.hourdex.smartedu.features.enroll

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.hourdex.smartedu.features.students.StudentRes
import com.kyant.backdrop.backdrops.layerBackdrop
import dev.chrisbanes.haze.hazeSource

@Composable
fun UnenrolledStudentList(
    modifier: Modifier = Modifier,
    students: List<StudentRes>,
    widths: List<Dp>,
    hasSelection: Boolean = true,
    selectedIds: Set<Long>,
    toggle: (Long) -> Unit,
    contentPaddingValues: PaddingValues = PaddingValues(top = 150.dp, bottom = 130.dp, start = 8.dp, end = 8.dp)
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = contentPaddingValues
    ) {
        if(hasSelection) {
            item {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    text = "Select students to enroll", color = MaterialTheme.colorScheme.onSurface.copy(0.6f))
                StudentsMiniHeader(widths)
            }
        }

        items(students) { student ->
            StudentMiniRow(
                student = student,
                widths = widths,
                hasSelection = hasSelection,
                isSelected = student.id in selectedIds,
                onCheckedChange = { toggle(it) }
            )

        }
    }
}