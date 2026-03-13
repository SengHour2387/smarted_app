package com.hourdex.smartedu.features.enroll

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.hourdex.smartedu.features.students.HeaderCell
import com.hourdex.smartedu.features.students.StudentRes
import com.hourdex.smartedu.features.students.TableCell
import com.kyant.capsule.ContinuousRoundedRectangle


@Composable
fun StudentMiniRow(
    student: StudentRes,
    widths: List<Dp>,
    isSelected: Boolean,
    hasSelection: Boolean = true,
    onCheckedChange: (Long) -> Unit
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(2.dp)
            .clip(ContinuousRoundedRectangle(35))
            .background(MaterialTheme.colorScheme.surfaceContainer)
            .clickable {
                onCheckedChange(student.id) }
            .padding(5.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (hasSelection) {
            Checkbox(
                modifier = Modifier.clip(CircleShape),
                checked = isSelected,
                onCheckedChange = { onCheckedChange(student.id) }
            )
        }

        TableCell(student.id.toString(), widths[0])
        TableCell(student.full_name, widths[1])
        TableCell(student.users.email, widths[2])
    }
}

@Composable
fun StudentsMiniHeader(widths: List<Dp>) {

    Row(
        Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background)
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Spacer(Modifier.width(40.dp)) // space for checkbox

        HeaderCell("ID", widths[0])
        HeaderCell("Name", widths[1])
        HeaderCell("Email", widths[2])
    }
}

@Composable
fun rememberStudentMiniWidths(students: List<StudentRes>): List<Dp> {

    val textMeasurer = rememberTextMeasurer()
    val density = LocalDensity.current
    val style = MaterialTheme.typography.bodyMedium

    return remember(students) {

        val columns = listOf(
            students.map { it.id.toString() } + "ID",
            students.map { it.full_name } + "Name",
            students.map { it.users.email } + "Email"
        )

        columns.map { column ->

            val maxWidth = column.maxOf {
                textMeasurer.measure(
                    text = AnnotatedString(it),
                    style = style
                ).size.width
            }

            with(density) { maxWidth.toDp() + 24.dp }
        }
    }
}