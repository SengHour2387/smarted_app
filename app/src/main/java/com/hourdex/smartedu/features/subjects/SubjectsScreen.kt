package com.hourdex.smartedu.features.subjects

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.hourdex.smartedu.R
import com.hourdex.smartedu.uis.components.AlertDialogExample
import com.hourdex.smartedu.uis.components.GlassBottomSheet
import com.hourdex.smartedu.uis.components.LLTextField
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
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalSharedTransitionApi::class)
@Composable
fun SubjectsScreen(
    viewModel: SubjectsViewModel = hiltViewModel(),
    sharedTransitionScope: SharedTransitionScope,
    onBack: () -> Unit = {},
    animatedVisibilityScope: AnimatedVisibilityScope,
) {
    val subjects by viewModel.subjects.collectAsStateWithLifecycle()
    val sheetEditState = rememberModalBottomSheetState()
    val sheetAddState = rememberModalBottomSheetState()
    val isShowingEdit = remember { mutableStateOf(false) }
    val isShowingAdd = remember { mutableStateOf(false) }
    val newSubjectName = remember { mutableStateOf("") }
    val selectedId = remember { mutableLongStateOf(-1L) }
    val openAlertDialog = remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    val primaryColor = MaterialTheme.colorScheme.primary
    val hazeState = rememberHazeState()

    if(subjects.isEmpty()) {
        LaunchedEffect(Unit) {
            viewModel.getSubjects()
        }
    }

    with(sharedTransitionScope) {
        Box(
            modifier = Modifier.fillMaxSize().sharedBounds(
                resizeMode = SharedTransitionScope.ResizeMode.scaleToBounds(),
                sharedContentState = rememberSharedContentState("subjects_screen_card"),
                animatedVisibilityScope = animatedVisibilityScope
            )

        ) {
            val backgroundColor = MaterialTheme.colorScheme.background
            val backdrop = rememberLayerBackdrop {
                drawRect(backgroundColor)
                drawContent()
            }

            LazyColumn(
                Modifier.fillMaxSize()
                    .hazeSource(hazeState)
                    .layerBackdrop(backdrop)
                    .background(backgroundColor),
                contentPadding = PaddingValues(top = 150.dp, bottom = 150.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                item {
                }
                if(subjects.isNotEmpty()) {
                    items(subjects.size) { index ->
                        SubjectTileAdmin(subjectsRes = subjects[index],
                            hasEdit = true,
                            onClickEdit = {id->
                                selectedId.longValue = id
                                isShowingEdit.value = true
                                newSubjectName.value = subjects[index].name
                                coroutineScope.launch {
                                    sheetEditState.show()
                                }
                            },
                            onClickDelete = {id->
                                openAlertDialog.value = true
                                selectedId.longValue = id
                            }
                        )
                    }
                } else {
                    item {
                        Text("No subjects found")
                    }
                }
            }

            MainTitle(
                text = "Subjects",
                hazeState = hazeState,
                backdrop = backdrop,
                onBack = onBack,
                animatedVisibilityScope = animatedVisibilityScope,
                sharedTransitionScope = this@with
            )
            Box(
                Modifier.align(Alignment.BottomCenter).fillMaxWidth().height(100.dp)
                    .hazeEffect(hazeState) {
                        progressive = HazeProgressive.verticalGradient(
                            preferPerformance = true,
                            startIntensity = 0f,
                            endIntensity = 1f
                        )
                    }
            )

            AnimatedVisibility(
                visible = isShowingAdd.value,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                Box(Modifier.fillMaxSize()
                    .clickable(
                        onClick = {
                            newSubjectName.value = ""
                            selectedId.longValue = -1L
                            isShowingAdd.value = false
                        }
                    )
                    .background(Color.Black.copy(0.3f)))
            }

                AnimatedVisibility(
                    modifier = Modifier.align(Alignment.BottomCenter),
                    visible = isShowingAdd.value,
                    enter = fadeIn(),
                    exit = fadeOut()
                ) {
                    this@Box.GlassBottomSheet(
                            modifier = Modifier
                                .sharedElement(
                                    rememberSharedContentState("add_subject_button"),
                                    this@AnimatedVisibility
                                )
                                .safeContentPadding(),
                            backdrop = backdrop) {
                            Column(
                                Modifier.fillMaxWidth().padding(8.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                LLTextField(
                                    placeHolderText = "Enter subject name",
                                    text = newSubjectName.value,
                                    onTextChange = { newSubjectName.value = it }
                                )
                                Button(
                                    modifier = Modifier.padding(top = 5.dp),
                                    onClick = {
                                        viewModel.addSubject(newSubjectName.value)
                                        isShowingAdd.value = false
                                    }
                                ) {
                                    Text("Save")
                                }
                            }
                    }

                }
                AnimatedVisibility(
                    modifier = Modifier.align(Alignment.BottomCenter),
                    visible = !isShowingAdd.value,
                ) {
                    Box(
                        modifier = Modifier
                            .sharedElement(
                                rememberSharedContentState("add_subject_button"),
                                this
                            )
                            .safeContentPadding()
                            .align(Alignment.BottomCenter)
                            .drawBackdrop(
                                backdrop = backdrop,
                                shape = { ContinuousRoundedRectangle(32.dp) },
                                effects = {
                                    vibrancy()
                                    blur(4f.dp.toPx())
                                    lens(14f.dp.toPx(), 18f.dp.toPx(), true,chromaticAberration = true)
                                },
                                onDrawSurface = { drawRect(primaryColor.copy(0.5f)) }
                            )
                            .clickable(
                                onClick = {
                                    isShowingAdd.value = true
                                    coroutineScope.launch {
                                        sheetAddState.show()
                                    }
                                }
                            )
                            .padding(8.dp)

                    ) {
                        Row(
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("Add Subject", color = MaterialTheme.colorScheme.onPrimary)
                            Image(
                                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onPrimary),
                                painter = painterResource(R.drawable.style_bulk_4),
                                contentDescription = null)
                        }
                    }

                }

            when {
                openAlertDialog.value->
                    AlertDialogExample(
                        onDismissRequest = {
                            openAlertDialog.value = false
                            selectedId.longValue = -1L
                        },
                        onConfirmation = {
                            coroutineScope.launch {
                                viewModel.deleteSubject(selectedId.longValue)
                                openAlertDialog.value = false
                                selectedId.longValue = -1L
                            }
                        },
                        dialogTitle = "Delete Subject",
                        dialogText = "Are you sure you want to delete this subject?",
                    )
            }
            if(isShowingEdit.value) {
                ModalBottomSheet(
                    contentColor = Color.Transparent,
                    containerColor = Color.Transparent,
                    dragHandle = {},
                    onDismissRequest = {
                        newSubjectName.value = ""
                        selectedId.longValue = -1L
                        isShowingEdit.value = false },
                    sheetState = sheetEditState
                ) {
                    this@Box.GlassBottomSheet(
                        modifier = Modifier.safeContentPadding(),
                        backdrop = backdrop) {
                        Column(
                            Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            LLTextField(
                                text = newSubjectName.value,
                                onTextChange = { newSubjectName.value = it }
                            )
                            Button(
                                modifier = Modifier.padding(top = 5.dp),
                                onClick = {
                                    viewModel.updateSubject(id = selectedId.longValue, newName = newSubjectName.value)
                                    isShowingEdit.value = false
                                }
                            ) {
                                Text("Update")
                            }
                        }
                    }

                }
            }
//            if(isShowingAdd.value) {
//                ModalBottomSheet(
//                    contentColor = Color.Transparent,
//                    containerColor = Color.Transparent,
//                    dragHandle = {},
//                    onDismissRequest = {
//                        newSubjectName.value = ""
//                        selectedId.longValue = -1L
//                        isShowingAdd.value = false },
//                    sheetState = sheetAddState
//                ) {
//
//                    this@Box.GlassBottomSheet(
//                        modifier = Modifier.safeContentPadding(),
//                        backdrop = backdrop) {
//                        Column(
//                            Modifier.fillMaxWidth().padding(8.dp),
//                            horizontalAlignment = Alignment.CenterHorizontally
//                        ) {
//                            LLTextField(
//                                placeHolderText = "Enter subject name",
//                                text = newSubjectName.value,
//                                onTextChange = { newSubjectName.value = it }
//                            )
//                            Button(
//                                modifier = Modifier.padding(top = 5.dp),
//                                onClick = {
//                                    viewModel.addSubject(newSubjectName.value)
//                                    isShowingAdd.value = false
//                                }
//                            ) {
//                                Text("Save")
//                            }
//                        }
//                    }
//
//                }
//            }
        }
    }
}