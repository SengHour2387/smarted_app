package com.hourdex.smartedu.features.students

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.kyant.capsule.ContinuousRoundedRectangle

@Composable
fun StudentRow(
    student: StudentRes,
    onClick: () -> Unit,
    animatedVisibilityScope:  AnimatedVisibilityScope,
    sharedTransitionScope: SharedTransitionScope,
    widths: List<Dp>
) {
    Row(
        with(sharedTransitionScope) {
            Modifier
                .fillMaxWidth()
                .sharedBounds(
                    resizeMode = SharedTransitionScope.ResizeMode.RemeasureToBounds,
                    sharedContentState = rememberSharedContentState("details_edit_student_${student.id}"),
                    animatedVisibilityScope = animatedVisibilityScope
                )
                .padding(2.dp)
                .clip(ContinuousRoundedRectangle(8.dp))
                .background(MaterialTheme.colorScheme.secondaryContainer)
                .clickable(
                    onClick = onClick
                )
                .padding(12.dp)
        }
    ) {

        TableCell(student.id.toString(), widths[0])
        TableCell(student.full_name, widths[1])
        TableCell(student.users.email, widths[2])
        TableCell(student.users.phone.toString(), widths[3])
        TableCell(student.status, widths[4])
        TableCell(student.student_code, widths[5])
    }
}

@Composable
fun TableCell(
    text: String,
    width: Dp
) {

    Text(
        color = MaterialTheme.colorScheme.onBackground,
        text = text,
        modifier = Modifier
            .width(width)
            .padding(horizontal = 12.dp),
        textAlign = TextAlign.Start,
        style = MaterialTheme.typography.bodyMedium,
        maxLines = 1
    )
}

@Composable
fun rememberStudentColumnWidths(
    students: List<StudentRes>
): List<Dp> {

    val textMeasurer = rememberTextMeasurer()
    val density = LocalDensity.current
    val textStyle = MaterialTheme.typography.bodyMedium

    val columns = listOf(
        students.map { it.id.toString() } + "ID",
        students.map { it.full_name } + "Name",
        students.map { it.users.email } + "Email",
        students.map { it.users.phone } + "Phone",
        students.map { it.status } + "Status",
        students.map { it.student_code } + "Code"
    )

    return columns.map { column ->

        val maxWidthPx = column.maxOf { text ->

            textMeasurer.measure(
                text = AnnotatedString(text.toString()),
                style = textStyle
            ).size.width
        }

        with(density) {
            (maxWidthPx.toDp() + 24.dp) // padding safety
        }
    }
}

@Composable
fun StudentsHeader(widths: List<Dp>) {
    Row(
        Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background)
            .padding(12.dp)

    ) {

        HeaderCell("ID", widths[0])
        HeaderCell("Name", widths[1])
        HeaderCell("Email", widths[2])
        HeaderCell("Phone", widths[3])
        HeaderCell("Status", widths[4])
        HeaderCell("Code", widths[5])

    }
}

@Composable
fun HeaderCell(
    text: String,
    width: Dp
) {
    Text(
        text = text,
        modifier = Modifier
            .width(width)
            .padding(horizontal = 12.dp, vertical = 8.dp),
        style = MaterialTheme.typography.titleSmall,
        color = MaterialTheme.colorScheme.onPrimaryContainer
    )
}