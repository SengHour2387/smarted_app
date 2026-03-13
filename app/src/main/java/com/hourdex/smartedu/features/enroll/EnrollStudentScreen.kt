package com.hourdex.smartedu.features.enroll

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.hourdex.smartedu.features.students.StudentsViewModel
import com.hourdex.smartedu.uis.components.GlassBottomSheet
import com.hourdex.smartedu.uis.components.MainTitle
import com.kyant.backdrop.backdrops.layerBackdrop
import com.kyant.backdrop.backdrops.rememberLayerBackdrop
import dev.chrisbanes.haze.hazeSource
import dev.chrisbanes.haze.rememberHazeState

@Composable
fun EnrollStudentScreen(
    selectedStudentId: Long?,
    enrollViewModel: EnrollViewModel = hiltViewModel(),
    onNavigateToSelectClass: () -> Unit,
    onBack: () -> Unit,
    studentsViewModel: StudentsViewModel = hiltViewModel(),
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope
) {

    val students by enrollViewModel.unenrolledStudents.collectAsStateWithLifecycle()


    val primaryColor = MaterialTheme.colorScheme.primary
    val hazeState = rememberHazeState()

    val isShowingNext = rememberSaveable { mutableStateOf(false) }

    val backgroundColor = MaterialTheme.colorScheme.background
    val backdrop = rememberLayerBackdrop {
        drawRect(backgroundColor)
        drawContent()
    }

    LaunchedEffect(Unit) {
        enrollViewModel.refresh()
        if(selectedStudentId != null) {
            enrollViewModel.addToSelection(selectedStudentId)
        }
    }

    with(sharedTransitionScope) { Box(
        modifier = Modifier
            .fillMaxSize()
            .sharedBounds(
                resizeMode = SharedTransitionScope.ResizeMode.scaleToBounds(),
                sharedContentState = rememberSharedContentState("action_tile_Enroll Student"),
                animatedVisibilityScope = animatedVisibilityScope
            )
    ) {
        val widths = rememberStudentMiniWidths(students)

        val selectedIds by enrollViewModel.selectedIds.collectAsStateWithLifecycle()

        LaunchedEffect(selectedIds) {
            isShowingNext.value = selectedIds.isNotEmpty()
        }

        fun toggle(id: Long) {
                if (id in selectedIds)
                    enrollViewModel.removeFromSelection(id)
                else
                    enrollViewModel.addToSelection(id)
        }

        UnenrolledStudentList(
            modifier = Modifier
                .fillMaxWidth()
                .hazeSource(hazeState)
                .layerBackdrop(backdrop)
                .background(backgroundColor),
            students = students,
            widths = widths,
            selectedIds = selectedIds,
            toggle = ::toggle
        )

//        LazyColumn(
//            modifier = Modifier
//                .fillMaxWidth()
//                .hazeSource(hazeState)
//                .layerBackdrop(backdrop)
//                .background(backgroundColor),
//            contentPadding = PaddingValues(top = 150.dp, bottom = 130.dp, start = 8.dp, end = 8.dp)
//        ) {
//            item {
//                Text(
//                    modifier = Modifier.fillMaxWidth(),
//                    textAlign = TextAlign.Center,
//                    text = "Select students to enroll", color = MaterialTheme.colorScheme.onSurface.copy(0.6f))
//                StudentsMiniHeader(widths)
//            }
//
//            items(students) { student ->
//                StudentMiniRow(
//                    student = student,
//                    widths = widths,
//                    isSelected = student.id in selectedIds,
//                    onCheckedChange = { toggle(it) }
//                )
//
//            }
//        }

        AnimatedVisibility(
            modifier = Modifier.align(Alignment.BottomCenter),
            visible = isShowingNext.value,
            enter = slideInVertically(
                animationSpec = spring(dampingRatio = Spring.DampingRatioLowBouncy),
                initialOffsetY = {it*2}) + scaleIn(
                animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy)
            ),
            exit = fadeOut() + scaleOut(targetScale = 1.1f)
        ) {
            this@Box.GlassBottomSheet(
                modifier = Modifier
                    .sharedElement(
                        rememberSharedContentState("add_subject_button"),
                        this@AnimatedVisibility
                    )
                    .safeContentPadding(),
                backdrop = backdrop) {
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        modifier = Modifier.wrapContentWidth(),
                        horizontalArrangement = Arrangement.spacedBy(5.dp)
                    ) {
                        Text("Selected:", color = MaterialTheme.colorScheme.onSurface)
                        Text("${selectedIds.size}", color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.SemiBold, fontSize = 20.sp)
                    }


                    Row(
                        modifier = Modifier.wrapContentWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            modifier = Modifier.padding(top = 5.dp),
                            onClick = {
                                enrollViewModel.clearSelection()
                            }
                        ) {
                            Text("Clear")
                        }
                        Button(
                            modifier = Modifier.padding(top = 5.dp),
                            onClick = onNavigateToSelectClass
                        ) {
                            Text("Next")
                        }
                    }
                }
            }

        }

        MainTitle(
            text = "Enroll Student",
            hazeState = hazeState,
            backdrop = backdrop,
            onBack = onBack,
            animatedVisibilityScope = animatedVisibilityScope,
            sharedTransitionScope = sharedTransitionScope,
        )
    }
    }
}