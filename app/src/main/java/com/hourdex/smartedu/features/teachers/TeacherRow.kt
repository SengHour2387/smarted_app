package com.hourdex.smartedu.features.teachers

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.hourdex.smartedu.features.students.HeaderCell
import com.hourdex.smartedu.features.students.TableCell
import com.kyant.capsule.ContinuousRoundedRectangle

@Composable
fun TeacherRow(
    teacher: TeacherRes,
    widths: List<Dp>,
    onClick: () -> Unit,
    onClickDelete: () -> Unit
) {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(2.dp)
            .clip(ContinuousRoundedRectangle(35))
            .background(MaterialTheme.colorScheme.secondaryContainer)
            .clickable { onClick() }
            .padding(12.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        TableCell(teacher.id.toString(), widths[0])
        TableCell(teacher.full_name, widths[1])
        TableCell(teacher.users?.email ?: "", widths[2])
        TableCell(teacher.department ?: "", widths[3])
        TableCell(teacher.employee_code, widths[4])
    }
}

@Composable
fun TeachersHeader(widths: List<Dp>) {
    Row(
        Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface)
            .padding(12.dp)
    ) {
        HeaderCell("ID", widths[0])
        HeaderCell("Name", widths[1])
        HeaderCell("Email", widths[2])
        HeaderCell("Department", widths[3])
        HeaderCell("Code", widths[4])
    }
}


@Composable
fun rememberTeacherColumnWidths(teachers: List<TeacherRes>): List<Dp> {

    val textMeasurer = rememberTextMeasurer()
    val density = LocalDensity.current
    val style = MaterialTheme.typography.bodyMedium

    return remember(teachers) {

        val columns = listOf(
            teachers.map { it.id.toString() } + "ID",
            teachers.map { it.full_name } + "Name",
            teachers.map { it.users?.email ?: "" } + "Email",
            teachers.map { it.department ?: "" } + "Department",
            teachers.map { it.employee_code } + "Code"
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