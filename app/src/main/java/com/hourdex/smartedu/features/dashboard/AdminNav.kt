package com.hourdex.smartedu.features.dashboard

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.hourdex.smartedu.R
import com.hourdex.smartedu.features.auth.AuthViewModel
import com.hourdex.smartedu.features.classes.ClassesScreen
import com.hourdex.smartedu.features.classes.ClassesVieModel
import com.hourdex.smartedu.features.classes.StudentReadOnlyListScreen
import com.hourdex.smartedu.features.enroll.EnrollStudentScreen
import com.hourdex.smartedu.features.enroll.EnrollViewModel
import com.hourdex.smartedu.features.enroll.SelectClassScreen
import com.hourdex.smartedu.features.students.AddStudentScreen
import com.hourdex.smartedu.features.students.StudentDetailEdit
import com.hourdex.smartedu.features.students.StudentsScreen
import com.hourdex.smartedu.features.students.StudentsViewModel
import com.hourdex.smartedu.features.subjects.SubjectsScreen
import com.hourdex.smartedu.features.subjects.SubjectsViewModel
import com.hourdex.smartedu.features.teacherSubjectClass.AddAssignScreen
import com.hourdex.smartedu.features.teacherSubjectClass.TeacherAssignViewModel
import com.hourdex.smartedu.features.teachers.AddTeacherScreen
import com.hourdex.smartedu.features.teachers.TeacherViewModel
import com.hourdex.smartedu.features.teachers.TeachersScreen
import com.kyant.backdrop.backdrops.rememberLayerBackdrop
import androidx.compose.runtime.collectAsState

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun AdminNav(
    authViewModel: AuthViewModel = hiltViewModel()
)  {
    val user by authViewModel.user.collectAsStateWithLifecycle()
    Box(
        Modifier.fillMaxSize()
    ) {
        when(user?.role) {
            "admin"->{
                val navController = rememberNavController()
                val subjectsViewModel: SubjectsViewModel = hiltViewModel()
                val classesViewModel: ClassesVieModel = hiltViewModel()
                val studentsViewModel: StudentsViewModel = hiltViewModel()
                val teacherViewModel: TeacherViewModel = hiltViewModel()
                val teacherAssignViewModel: TeacherAssignViewModel = hiltViewModel()
                val enrollViewModel: EnrollViewModel = hiltViewModel()
                SharedTransitionLayout {
                    NavHost(
                        modifier = Modifier.fillMaxSize(),
                        navController = navController,
                        startDestination = "admin/dashboard",
                    ) {
                        composable("admin/dashboard") {
                            AdminDashboard(
                                subjectsViewModel = subjectsViewModel,
                                classesViewModel = classesViewModel,
                                studentsViewModel = studentsViewModel,
                                teachersViewModel = teacherViewModel,
                                enrollViewModel = enrollViewModel,
                                teacherAssignViewModel = teacherAssignViewModel,
                                sharedTransitionScope = this@SharedTransitionLayout,
                                animatedVisibilityScope = this@composable,
                                onNavigateToClasses = {navController.navigate("admin/classes")},
                                onNavigateToSubjects = { navController.navigate("admin/subjects")},
                                onNavigateToStudents = {navController.navigate("admin/students")},
                                onNavigateToEnrollStudent = {navController.navigate("admin/enroll-student")},
                                onNavigateToAssignTeacher = {navController.navigate("admin/assign-teacher")},
                                onNavigateToTeachers = {navController.navigate("admin/teachers")}
                            )
                        }
                        composable("admin/subjects") {
                            SubjectsScreen(
                                viewModel = subjectsViewModel,
                                onBack = { if(navController.previousBackStackEntry != null) navController.popBackStack()},
                                sharedTransitionScope = this@SharedTransitionLayout,
                                animatedVisibilityScope = this@composable
                            )
                        }
                        composable("admin/classes") {
                            ClassesScreen(
                                viewModel = classesViewModel,
                                onBack = {navController.popBackStack()},
                                onNavigateToSeeClass = {
                                    navController.navigate("admin/class/students")
                                },
                                sharedTransitionScope = this@SharedTransitionLayout,
                                animatedVisibilityScope = this@composable
                            )
                        }

                        composable(
                            route = "admin/class/students",
                            enterTransition = { slideInHorizontally { it } + fadeIn() },
                            exitTransition = { slideOutHorizontally { it } + fadeOut() }
                        ) {
                            StudentReadOnlyListScreen(
                                students = classesViewModel.studentInAClass.collectAsState().value,
                                class_id = classesViewModel.selectedClassId.value?:0L,
                                sharedTransitionScope = this@SharedTransitionLayout,
                                animatedVisibilityScope = this@composable,
                                modifier = Modifier
                                )
                        }
                        composable("admin/students") {
                            StudentsScreen(
                                studentsViewModel = studentsViewModel,
                                animatedVisibilityScope = this@composable,
                                onNavigateToAddStudent = {navController.navigate("admin/students/add")},
                                onNavigateToStudentDetails = {navController.navigate("admin/students/$it")},
                                onBack = { if(navController.previousBackStackEntry !=null) navController.popBackStack() },
                                sharedTransitionScope = this@SharedTransitionLayout
                            )
                        }
                        composable(
                            route = "admin/students/{id}",
                            arguments = listOf(navArgument("id") {  type = NavType.LongType }),
                            exitTransition = {
                                shrinkVertically()
                                scaleOut(
                                    targetScale = 0.5f
                                )
                            }
                        ) { entry->
                            val id = entry.arguments?.getLong("id")
                            StudentDetailEdit(
                                sharedTransitionScope = this@SharedTransitionLayout,
                                animatedVisibilityScope = this@composable,
                                studentsViewModel = studentsViewModel,
                                onBack = {  navController.popBackStack()},
                                id = id?:0L
                            )
                        }
                        composable("admin/students/add") {
                            AddStudentScreen(
                                viewModel = studentsViewModel,
                                animatedVisibilityScope = this@composable,
                                onBack = {
                                    navController.popBackStack()
                                },
                                sharedTransitionScope = this@SharedTransitionLayout
                            )
                        }
                        composable("admin/teachers") {
                            TeachersScreen(
                                onBack = {
                                    navController.popBackStack()
                                },
                                sharedTransitionScope = this@SharedTransitionLayout,
                                animatedVisibilityScope = this@composable,
                                teacherViewModel = teacherViewModel,
                                onNavigateToAddTeacher = {
                                    navController.navigate("admin/teachers/add")
                                }
                            )
                        }

                        composable(route = "admin/teachers/add") {
                            AddTeacherScreen(
                                onBack = {
                                    navController.popBackStack()
                                },
                                viewModel = teacherViewModel,
                                sharedTransitionScope = this@SharedTransitionLayout,
                                animatedVisibilityScope = this@composable
                            )
                        }

                        composable(
                            route ="admin/assign-teacher"
                        ) {
                            AddAssignScreen(
                                teacherViewModel = teacherViewModel,
                                teacherAssignViewModel = teacherAssignViewModel,
                                subjectViewModel = subjectsViewModel,
                                onDone = {
                                    navController.popBackStack()
                                },
                                classViewModel = classesViewModel,
                                sharedTransitionScope = this@SharedTransitionLayout,
                                animatedVisibilityScope = this@composable
                            )
                        }

                        composable(
                            route = "admin/enroll-student"
                        ) {
                            EnrollStudentScreen(
                                selectedStudentId = null,
                                sharedTransitionScope = this@SharedTransitionLayout,
                                animatedVisibilityScope = this@composable,
                                enrollViewModel = enrollViewModel,
                                onBack = {
                                    navController.popBackStack()
                                },
                                onNavigateToSelectClass = {
                                    navController.navigate("admin/select-class")
                                },
                                studentsViewModel = studentsViewModel
                            )
                        }
                        composable(
                            route = "admin/select-class",
                        ) {
                            SelectClassScreen(
                                sharedTransitionScope = this@SharedTransitionLayout,
                                animatedVisibilityScope = this@composable,
                                enrollViewModel = enrollViewModel,
                                onBack = {
                                    navController.popBackStack("admin/dashboard", inclusive = false)
                                }
                            )
                        }
                    }
                }
            }
            "student"->{}
            null->{}
            else -> {}
        }
    }
}
