package com.hourdex.smartedu.features.teacherSubjectClass

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle

// ── Column widths ────────────────────────────────────────────────────────────

val COL_TEACHER = 160.dp
val COL_SUBJECT = 140.dp
val COL_CLASS   = 120.dp
val COL_ACTION  = 80.dp

// ── Screen ───────────────────────────────────────────────────────────────────

@Composable
fun TeacherAssignmentListScreen(
    viewModel: TeacherAssignViewModel,
    onViewDetail: (TeacherAssignRes) -> Unit = {}
) {
    val assignments by viewModel.teacherAssignments.collectAsStateWithLifecycle()
    val uiState    by viewModel.uiState.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {

        // ── Header ───────────────────────────────────────────────────────────
        Text(
            text = "Teacher Assignments",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
        )

        if (uiState.loading) {
            LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
        }

        uiState.error?.let {
            Text(
                text = it,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
            )
        }

        // ── Table ────────────────────────────────────────────────────────────
        val scrollState = rememberScrollState()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .horizontalScroll(scrollState)
        ) {
            // Header row
            AssignmentTableHeader()

            HorizontalDivider(thickness = 1.dp, color = MaterialTheme.colorScheme.outlineVariant)

            if (assignments.isEmpty() && !uiState.loading) {
                Box(
                    modifier = Modifier
                        .width(COL_TEACHER + COL_SUBJECT + COL_CLASS + COL_ACTION)
                        .padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No assignments yet",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            } else {
                LazyColumn {
                    items(assignments, key = { it.id ?: 0 }) { assignment ->
                        AssignmentRow(
                            assignment = assignment,
                            onViewDetail = { onViewDetail(assignment) }
                        )
                        HorizontalDivider(
                            thickness = 0.5.dp,
                            color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
                        )
                    }
                }
            }
        }
    }
}

// ── Header row ───────────────────────────────────────────────────────────────

@Composable
private fun AssignmentTableHeader() {
    Row(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.surfaceContainer)
            .padding(vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        HeaderCell("Teacher",      COL_TEACHER)
        HeaderCell("Subject",      COL_SUBJECT)
        HeaderCell("Class",        COL_CLASS)
        HeaderCell("",             COL_ACTION)
    }
}

@Composable
private fun HeaderCell(text: String, width: Dp) {
    Text(
        text = text,
        modifier = Modifier
            .width(width)
            .padding(horizontal = 12.dp),
        style = MaterialTheme.typography.labelMedium,
        fontWeight = FontWeight.SemiBold,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis
    )
}

// ── Data row ─────────────────────────────────────────────────────────────────

@Composable
fun AssignmentRow(
    assignment: TeacherAssignRes,
    onViewDetail: () -> Unit
) {
    Row(
        modifier = Modifier
            .clickable { onViewDetail() }
            .padding(vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        DataCell(assignment.teacher?.full_name ?: "-", COL_TEACHER)
        DataCell(assignment.subject?.name       ?: "-", COL_SUBJECT)
        DataCell(assignment.classInfo?.name     ?: "-", COL_CLASS)

//        // Action
//        Box(
//            modifier = Modifier.width(COL_ACTION),
//            contentAlignment = Alignment.Center
//        ) {
//            Box(
//                modifier = Modifier
//                    .clip(RoundedCornerShape(6.dp))
//                    .border(
//                        width = 1.dp,
//                        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f),
//                        shape = RoundedCornerShape(6.dp)
//                    )
//                    .clickable { onViewDetail() }
//                    .padding(horizontal = 10.dp, vertical = 4.dp)
//            ) {
//                Text(
//                    text = "View",
//                    style = MaterialTheme.typography.labelSmall,
//                    color = MaterialTheme.colorScheme.primary,
//                    fontWeight = FontWeight.Medium
//                )
//            }
//        }
    }
}

@Composable
private fun DataCell(text: String, width: Dp) {
    Text(
        text = text,
        modifier = Modifier
            .width(width)
            .padding(horizontal = 12.dp),
        style = MaterialTheme.typography.bodyMedium,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis
    )
}