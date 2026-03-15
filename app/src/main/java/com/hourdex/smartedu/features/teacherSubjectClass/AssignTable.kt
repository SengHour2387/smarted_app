package com.hourdex.smartedu.features.teacherSubjectClass

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.hourdex.smartedu.features.students.HeaderCell

@Composable
fun TeacherAssignmentTable(
    assignments: List<TeacherAssignRes>,
    onViewDetail: (TeacherAssignRes) -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .navigationBarsPadding()
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.outlineVariant,
                shape = RoundedCornerShape(12.dp)
            )
            .horizontalScroll(scrollState)
    ) {
        // Header
        Row(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.surfaceContainer)
                .padding(vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            HeaderCell("Teacher", COL_TEACHER)
            HeaderCell("Subject", COL_SUBJECT)
            HeaderCell("Class",   COL_CLASS)
            HeaderCell("",        COL_ACTION)
        }

        HorizontalDivider(thickness = 1.dp, color = MaterialTheme.colorScheme.outlineVariant)

        if (assignments.isEmpty()) {
            Box(
                modifier = Modifier
                    .width(COL_TEACHER + COL_SUBJECT + COL_CLASS + COL_ACTION)
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Not set yet",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        } else {
            assignments.forEachIndexed { index, assignment ->
                AssignmentRow(
                    assignment = assignment,
                    onViewDetail = { onViewDetail(assignment) }
                )
                if (index < assignments.lastIndex) {
                    HorizontalDivider(
                        thickness = 0.5.dp,
                        color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
                    )
                }
            }
        }
    }
}
