package com.hourdex.smartedu.features.classes

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.hourdex.smartedu.features.students.StudentRes

@Composable
fun StudentReadOnlyListScreen(
    students: List<StudentRes>,
    class_id: Long,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
    modifier: Modifier = Modifier
) {
    val widths = rememberStudentReadOnlyWidths(students)

    LazyColumn(
        modifier =  with(sharedTransitionScope) { modifier
            .sharedBounds(
                resizeMode = SharedTransitionScope.ResizeMode.scaleToBounds(),
                sharedContentState = rememberSharedContentState("students_screen_card_$class_id"),
                animatedVisibilityScope = animatedVisibilityScope
            )
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)},
        contentPadding = PaddingValues(top = 150.dp, bottom = 150.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Sticky header
        stickyHeader {
            StudentReadOnlyHeader(widths = widths)
            HorizontalDivider(
                thickness = 1.dp,
                color = MaterialTheme.colorScheme.outlineVariant
            )
        }

        if (students.isNotEmpty()) {
            items(students.size) { index ->
                StudentReadOnlyRow(
                    student = students[index],
                    widths = widths
                )
                HorizontalDivider(
                    thickness = 0.5.dp,
                    color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f)
                )
            }
        } else {
            item {
                Text(
                    text = "No students found",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(32.dp)
                )
            }
        }
    }
}