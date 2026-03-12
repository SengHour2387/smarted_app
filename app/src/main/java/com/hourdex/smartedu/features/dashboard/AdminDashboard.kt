package com.hourdex.smartedu.features.dashboard

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.displayCutoutPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.hourdex.smartedu.R
import com.hourdex.smartedu.features.classes.ClassesVieModel
import com.hourdex.smartedu.features.students.StudentsViewModel
import com.hourdex.smartedu.features.subjects.SubjectsViewModel
import com.kyant.backdrop.backdrops.rememberLayerBackdrop


@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun AdminDashboard(
    subjectsViewModel: SubjectsViewModel = hiltViewModel(),
    classesViewModel: ClassesVieModel = hiltViewModel(),
    studentsViewModel: StudentsViewModel=hiltViewModel(),
    onNavigateToSubjects: () -> Unit = {},
    onNavigateToClasses: () -> Unit = {},
    onNavigateToStudents: () -> Unit = {},
    onNavigateToEnrollStudent: () -> Unit = {},
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope

) {
    val subjects by subjectsViewModel.subjects.collectAsStateWithLifecycle()
    val classes by classesViewModel.classes.collectAsStateWithLifecycle()
    val students by studentsViewModel.students.collectAsStateWithLifecycle()


    val backdrop = rememberLayerBackdrop()
    val backgroundColor = MaterialTheme.colorScheme.background
    val primaryColor = MaterialTheme.colorScheme.primary

    LaunchedEffect(Unit) {
        subjectsViewModel.getSubjects()
    }
    with(sharedTransitionScope) {
        Column(
            Modifier.fillMaxSize().displayCutoutPadding().padding(top = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                modifier = Modifier.sharedBounds(
                    rememberSharedContentState("main_title"),
                    animatedVisibilityScope
                ),
                text = "Admin Dashboard", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onBackground)

            LazyRow(
                Modifier.fillMaxWidth().padding(top = 20.dp),
                horizontalArrangement = Arrangement.spacedBy(15.dp),
                contentPadding = PaddingValues(horizontal = 20.dp)
            ) {
                item {
                    ActionTile(
                        color = Color(0xFFA2CB8B),
                        text = "Enroll Student",
                        backdrop = backdrop,
                        sharedTransitionScope = sharedTransitionScope,
                        animatedVisibilityScope = animatedVisibilityScope,
                        icon = painterResource(R.drawable.style_bulk_8),
                        onClick = onNavigateToEnrollStudent
                    )
                }

                item {
                    ActionTile(
                        color = Color(0xFFD25353),
                        text = "Assign Teacher Class",
                        backdrop = backdrop,
                        sharedTransitionScope = sharedTransitionScope,
                        animatedVisibilityScope = animatedVisibilityScope,
                        icon = painterResource(R.drawable.student),
                        onClick = {}
                    )
                }
            }

            Row(
                Modifier.fillMaxWidth().padding(top = 20.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                SummaryCard(
                    modifier = Modifier
                        .sharedBounds(
                            resizeMode = SharedTransitionScope.ResizeMode.RemeasureToBounds,
                            sharedContentState = rememberSharedContentState("subjects_screen_card"),
                            animatedVisibilityScope = animatedVisibilityScope
                        ),
                    title = "Subjects",
                    value = subjects.size.toString(),
                    backdrop = backdrop,
                    icon = { }
                ) {
                    onNavigateToSubjects()
                }
                SummaryCard(
                    modifier = Modifier
                        .sharedBounds(
                            resizeMode = SharedTransitionScope.ResizeMode.RemeasureToBounds,
                            sharedContentState = rememberSharedContentState("class_screen_card"),
                            animatedVisibilityScope = animatedVisibilityScope
                        ),
                    title = "Classes",
                    value = classes.size.toString(),
                    backdrop = backdrop,
                    icon = { }
                ) {
                    onNavigateToClasses()
                }
            }
            Row(
                Modifier.fillMaxWidth().padding(top = 20.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                SummaryCard(
                    modifier = Modifier.sharedBounds(
                        resizeMode = SharedTransitionScope.ResizeMode.RemeasureToBounds,
                        sharedContentState = rememberSharedContentState("students_screen_card"),
                        animatedVisibilityScope = animatedVisibilityScope
                    ),
                    title = "Students",
                    value = students.size.toString(),
                    backdrop = backdrop,
                    icon = { }
                ) {
                    onNavigateToStudents()
                }
            }
        }
    }
}