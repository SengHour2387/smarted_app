package com.hourdex.smartedu.features.classes


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.hourdex.smartedu.features.students.HeaderCell
import com.hourdex.smartedu.features.students.StudentRes
import com.hourdex.smartedu.features.students.TableCell

@Composable
fun StudentReadOnlyRow(
    student: StudentRes,
    widths: List<Dp>,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        TableCell(student.id.toString(), widths[0])
        TableCell(student.full_name, widths[1])
        TableCell(student.users.email, widths[2])
    }
}

@Composable
fun StudentReadOnlyHeader(widths: List<Dp>) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 12.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        HeaderCell("ID",    widths[0])
        HeaderCell("Name",  widths[1])
        HeaderCell("Email", widths[2])
    }
}

@Composable
fun rememberStudentReadOnlyWidths(students: List<StudentRes>): List<Dp> {
    val textMeasurer = rememberTextMeasurer()
    val density = LocalDensity.current
    val style = MaterialTheme.typography.bodyMedium

    return remember(students) {
        val columns = listOf(
            students.map { it.id.toString() } + "ID",
            students.map { it.full_name }      + "Name",
            students.map { it.users.email }    + "Email"
        )
        columns.map { column ->
            val maxWidth = column.maxOf {
                textMeasurer.measure(AnnotatedString(it), style = style).size.width
            }
            with(density) { maxWidth.toDp() + 24.dp }
        }
    }
}