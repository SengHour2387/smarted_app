package com.hourdex.smartedu.features.teachers

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.hourdex.smartedu.R
import com.hourdex.smartedu.uis.components.MainTitle
import com.kyant.backdrop.backdrops.layerBackdrop
import com.kyant.backdrop.backdrops.rememberLayerBackdrop
import com.kyant.backdrop.drawBackdrop
import com.kyant.backdrop.effects.blur
import com.kyant.backdrop.effects.lens
import com.kyant.backdrop.effects.vibrancy
import com.kyant.capsule.ContinuousRoundedRectangle
import dev.chrisbanes.haze.HazeProgressive
import dev.chrisbanes.haze.hazeEffect
import dev.chrisbanes.haze.hazeSource
import dev.chrisbanes.haze.rememberHazeState

@Composable
fun TeachersScreen(
    teacherViewModel: TeacherViewModel = hiltViewModel(),
    onNavigateToAddTeacher: () -> Unit,
    onBack: () -> Unit,
    animatedVisibilityScope: AnimatedVisibilityScope,
    sharedTransitionScope: SharedTransitionScope,
) {

    val teachers by teacherViewModel.teachers.collectAsStateWithLifecycle()

    val scrollState = rememberScrollState()
    val widths = rememberTeacherColumnWidths(teachers)

    val hazeState = rememberHazeState()

    val backgroundColor = MaterialTheme.colorScheme.background
    val primaryColor = MaterialTheme.colorScheme.primary

    val backdrop = rememberLayerBackdrop {
        drawRect(backgroundColor)
        drawContent()
    }

    with(sharedTransitionScope) {

        Box(
            Modifier
                .fillMaxSize()
                .background(backgroundColor)
                .sharedBounds(
                    resizeMode = SharedTransitionScope.ResizeMode.scaleToBounds(),
                    sharedContentState = rememberSharedContentState("teachers_screen_card"),
                    animatedVisibilityScope = animatedVisibilityScope
                )
        ) {
            TeacherList(
                modifier =Modifier
                    .hazeSource(hazeState)
                    .layerBackdrop(backdrop)
                    .background(backgroundColor)
                    .horizontalScroll(scrollState),
                widths = widths,
                teachers = teachers,
                onTeacherClick = { },
                onTeacherDelete = {
                    teacherViewModel.deleteTeacher(it.id.toLong())
                }
            )

//            LazyColumn(
//                Modifier
//                    .hazeSource(hazeState)
//                    .layerBackdrop(backdrop)
//                    .background(backgroundColor)
//                    .horizontalScroll(scrollState),
//                contentPadding = PaddingValues(
//                    top = 150.dp,
//                    start = 15.dp,
//                    end = 15.dp,
//                    bottom = 100.dp
//                )
//            ) {
//                item {
//                    TeachersHeader(widths)
//                }
//
//                items(teachers) { teacher ->
//                    TeacherRow(
//                        teacher =teacher,
//                        widths = widths,
//                        onClick = { },
//                        onClickDelete = {
//                            teacherViewModel.deleteTeacher(teacher.id.toLong())
//                        }
//                    )
//                }
//            }

            MainTitle(
                text = "Teachers",
                hazeState = hazeState,
                backdrop = backdrop,
                onBack = onBack,
                animatedVisibilityScope = animatedVisibilityScope,
                sharedTransitionScope = sharedTransitionScope
            )

            Box(
                Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .height(100.dp)
                    .hazeEffect(hazeState) {
                        progressive = HazeProgressive.verticalGradient(
                            preferPerformance = true,
                            startIntensity = 0f,
                            endIntensity = 1f
                        )
                    }
            )

            Box(
                modifier = Modifier
                    .sharedBounds(
                        resizeMode = SharedTransitionScope.ResizeMode.RemeasureToBounds,
                        sharedContentState = rememberSharedContentState("add_teacher_screen"),
                        animatedVisibilityScope = animatedVisibilityScope
                    )
                    .safeContentPadding()
                    .align(Alignment.BottomCenter)
                    .drawBackdrop(
                        backdrop = backdrop,
                        shape = { ContinuousRoundedRectangle(32.dp) },
                        effects = {
                            vibrancy()
                            blur(2f.dp.toPx())
                            lens(14f.dp.toPx(), 24f.dp.toPx(), false, chromaticAberration = true)
                        },
                        onDrawSurface = { drawRect(primaryColor.copy(0.2f)) }
                    )
                    .clickable {
                        onNavigateToAddTeacher()
                    }
                    .padding(vertical = 8.dp, horizontal = 12.dp)
            ) {

                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Text(
                        "Add a new teacher",
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Image(
                        painter = painterResource(R.drawable.round_add_circle_24),
                        contentDescription = null,
                        colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurface)
                    )
                }
            }
        }
    }
}