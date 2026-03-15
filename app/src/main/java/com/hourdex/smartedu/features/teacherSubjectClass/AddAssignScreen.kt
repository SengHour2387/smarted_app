package com.hourdex.smartedu.features.teacherSubjectClass

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.displayCutoutPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.hourdex.smartedu.features.classes.ClassesList
import com.hourdex.smartedu.features.classes.ClassesVieModel
import com.hourdex.smartedu.features.subjects.SubjectList
import com.hourdex.smartedu.features.subjects.SubjectsViewModel
import com.hourdex.smartedu.features.teachers.TeacherList
import com.hourdex.smartedu.features.teachers.TeacherViewModel
import com.hourdex.smartedu.features.teachers.rememberTeacherColumnWidths
import com.kyant.backdrop.backdrops.rememberLayerBackdrop
import dev.chrisbanes.haze.rememberHazeState

@Composable
fun AddAssignScreen(
    teacherAssignViewModel: TeacherAssignViewModel,
    teacherViewModel: TeacherViewModel,
    subjectViewModel: SubjectsViewModel,
    classViewModel: ClassesVieModel,
    animatedVisibilityScope: AnimatedVisibilityScope,
    sharedTransitionScope: SharedTransitionScope,
    onDone: () -> Unit
) {
    val uiState by teacherAssignViewModel.uiState.collectAsStateWithLifecycle()
    val teachers by teacherViewModel.teachers.collectAsStateWithLifecycle()
    val subjects by subjectViewModel.subjects.collectAsStateWithLifecycle()
    val classes by classViewModel.classes.collectAsStateWithLifecycle()



    val hazeState = rememberHazeState()

    val backgroundColor = MaterialTheme.colorScheme.background
    val primaryColor = MaterialTheme.colorScheme.primary
    val backdrop = rememberLayerBackdrop {
        drawRect(backgroundColor)
        drawContent()
    }

    LaunchedEffect(uiState.step) {
        if (uiState.step is AssignStep.Complete) {
            onDone()
            teacherAssignViewModel.reset()
        }
    }

    Column(
        modifier = with(sharedTransitionScope) { Modifier
            .sharedBounds(
                resizeMode = SharedTransitionScope.ResizeMode.scaleToBounds(),
                sharedContentState = rememberSharedContentState("action_tile_Assign Teacher Class"),
                animatedVisibilityScope = animatedVisibilityScope
            )
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)}
    ) {
        // Step indicator
        StepIndicator(currentStep = uiState.step)

        // Loading bar
        if (uiState.loading) {
            LinearProgressIndicator(
                modifier = Modifier
                .displayCutoutPadding()
                .fillMaxWidth())
        }

        // Error
        uiState.error?.let {
            Text(
                text = it,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )
        }

        // Content
        Box(modifier = Modifier.weight(1f)) {
            when (val step = uiState.step) {
                is AssignStep.SelectTeacher ->
                    Box(modifier = Modifier.horizontalScroll(rememberScrollState())) {
                    TeacherList(
                        modifier = Modifier.fillMaxSize(),
                        teachers = teachers,
                        onTeacherClick = { teacherAssignViewModel.selectTeacher(it.id.toLong()) },
                        onTeacherDelete = {},
                        widths = rememberTeacherColumnWidths(teachers)
                    )
                }
                is AssignStep.SelectSubject -> SubjectList(
                    subjects = subjects,
                    onClick = { teacherAssignViewModel.selectSubject(it) },
                    backgroundColor = MaterialTheme.colorScheme.surfaceContainer,
                    hasEdit = false,
                    onClickEdit = {},
                    onClickDelete = {},
                    backdrop = backdrop,
                    hazeState = rememberHazeState()
                )
                is AssignStep.SelectClass -> ClassesList(
                    classes = classes,
                    onClickEdit = {},
                    isSelectable = true,
                    onClick = { teacherAssignViewModel.selectClass(it.id) },
                    onClickDelete = {}
                )
                is AssignStep.Complete -> Unit
            }
        }
    }
}

@Composable
fun StepIndicator(currentStep: AssignStep) {
    val steps = listOf("Teacher", "Subject", "Class")
    val currentIndex = when (currentStep) {
        is AssignStep.SelectTeacher -> 0
        is AssignStep.SelectSubject -> 1
        is AssignStep.SelectClass -> 2
        is AssignStep.Complete -> 3
    }

    Row(
        modifier = Modifier
            .displayCutoutPadding()
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        steps.forEachIndexed { index, label ->
            val done = index < currentIndex
            val active = index == currentIndex

            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(4.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(
                        when {
                            done -> MaterialTheme.colorScheme.primary
                            active -> MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)
                            else -> MaterialTheme.colorScheme.surfaceVariant
                        }
                    )
            )
        }
    }
}