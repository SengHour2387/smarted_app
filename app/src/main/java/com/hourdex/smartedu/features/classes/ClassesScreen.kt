package com.hourdex.smartedu.features.classes

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.hourdex.smartedu.R
import com.hourdex.smartedu.features.edYears.EdYearsViewModel
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
import dev.chrisbanes.haze.hazeSource
import dev.chrisbanes.haze.rememberHazeState
import kotlinx.coroutines.launch

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ClassesScreen(
    viewModel: ClassesVieModel = hiltViewModel(),
    adYearsViewModel: EdYearsViewModel = hiltViewModel(),
    animatedVisibilityScope: AnimatedVisibilityScope,
    sharedTransitionScope: SharedTransitionScope,
    onBack: () -> Unit,
) {

    val classes by viewModel.classes.collectAsStateWithLifecycle()
    val message by viewModel.reqMessage.collectAsStateWithLifecycle()

    val primaryColor = MaterialTheme.colorScheme.primary
    val isShowingAdd = remember { mutableStateOf(false) }
    val isShowingEdit = remember { mutableStateOf(false) }
    val openAlertDialog = remember { mutableStateOf(false) }
    val name = remember { mutableStateOf("") }
    val gradeLevel = remember { mutableStateOf("") }
    val capacity = remember { mutableStateOf("") }
    val selectedId = remember { mutableStateOf(-1L) }

    val hazeState = rememberHazeState()
    val backgroundColor = MaterialTheme.colorScheme.background
    val backdrop = rememberLayerBackdrop {
        drawRect(backgroundColor)
        drawContent()
    }

    if(classes.isEmpty()) {
        viewModel.getAllClasses()
    }

    with(sharedTransitionScope) {
        Box(
            Modifier.fillMaxSize()
                .sharedBounds(
                    resizeMode = SharedTransitionScope.ResizeMode.scaleToBounds(),
                    sharedContentState = rememberSharedContentState("class_screen_card"),
                    animatedVisibilityScope = animatedVisibilityScope
                ),
        ) {

            ClassesList(
                classes = classes,
                onClickEdit ={
                    name.value = it.name
                    gradeLevel.value = it.grade_level.toString()
                    capacity.value = it.capacity.toString()
                    selectedId.value = it.id
                    isShowingEdit.value = true
                },
                onClickDelete = {
                    selectedId.value = it.id
                    openAlertDialog.value = true
                }
            )

//            LazyColumn(
//                modifier = Modifier.fillMaxWidth()
//                    .layerBackdrop(backdrop)
//                    .hazeSource(hazeState)
//                    .background(backgroundColor),
//                contentPadding = PaddingValues(top = 150.dp, start = 8.dp, end = 8.dp),
//            ) {
//                items(classes) {
//                    ClassesTile(
//                        classesRes = it,
//                        onClickEdit = {
//                            name.value = it.name
//                            gradeLevel.value = it.grade_level.toString()
//                            capacity.value = it.capacity.toString()
//                            selectedId.value = it.id
//                           isShowingEdit.value = true
//                        },
//                        onClickDelete = {
//                            selectedId.value = it.id
//                            openAlertDialog.value = true
//                        }
//                    )
//                }
//            }
            MainTitle(
                text = "Classes",
                animatedVisibilityScope = animatedVisibilityScope,
                sharedTransitionScope = sharedTransitionScope,
                hazeState = hazeState,
                backdrop = backdrop,
                onBack = onBack,
            )


            if (
                message != null
            ) {
                AnimatedVisibility(
                    visible = true,
                    enter = slideInVertically(),
                    exit = slideOutVertically()
                ) {
                    Box(
                        modifier = Modifier
                            .padding(top = 100.dp)
                            .align(Alignment.TopCenter)
                            .background(MaterialTheme.colorScheme.surfaceContainer)
                            .shadow(20.dp)
                            .clip(CircleShape)
                            .padding(10.dp)
                        ,
                    ) {
                        Text(
                            text = message!!, color = MaterialTheme.colorScheme.error)
                    }
                }
            }


            AnimatedVisibility(
                visible = isShowingAdd.value || isShowingEdit.value,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                Box(Modifier.fillMaxSize()
                    .clickable(
                        onClick = {
                            isShowingAdd.value = false
                            name.value = ""
                            gradeLevel.value = ""
                            capacity.value = ""
                            selectedId.value = -1L
                            isShowingEdit.value = false
                        }
                    )
                    .background(Color.Black.copy(0.3f)))
            }

            //=========ADD BUTTON======================
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
                            }
                        )
                        .padding(8.dp)

                ) {
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Add Class", color = MaterialTheme.colorScheme.onPrimary)
                        Image(
                            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onPrimary),
                            painter = painterResource(R.drawable.style_bulk_4),
                            contentDescription = null)
                    }
                }

            }


//========= UPDATE======================
            AnimatedVisibility(
                modifier = Modifier.align(Alignment.BottomCenter),
                visible = isShowingEdit.value,
                enter = slideInVertically() {it*2},
                exit = slideOutVertically() {it*2}
            ) {
                GlassBottomSheet(
                    modifier = Modifier.align(Alignment.BottomCenter)
                        .sharedElement(
                            rememberSharedContentState("edit_subject_button"),
                            this
                        )
                        .safeContentPadding(),
                    backdrop = backdrop
                ) {
                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        LLTextField(
                            text = name.value,
                            placeHolderText = "Enter class name",
                            onTextChange = { name.value = it }
                        )
                        LLTextField(
                            keyboardType = KeyboardType.Number,
                            placeHolderText = "Enter grade level",
                            text = gradeLevel.value,
                            onTextChange = { gradeLevel.value = it }
                        )
                        LLTextField(
                            keyboardType = KeyboardType.Number,
                            placeHolderText = "Enter capacity",
                            text = capacity.value,
                            onTextChange = { capacity.value = it }
                        )
                        Button(
                            modifier = Modifier.padding(top = 5.dp),
                            onClick = {
                                viewModel.updateClass(classesReq = ClassesReq(
                                    name = name.value,
                                    grade_level = gradeLevel.value.toLong(),
                                    capacity = capacity.value.toLong(),
                                    academic_year_id = adYearsViewModel.lastEdYears.value?.id?:2L
                                ), id = selectedId.value
                                )
                                isShowingEdit.value = false
                            }
                        ) {
                            Text("Update")
                        }
                    }
                }
            }

//=========ADD NEW======================
            AnimatedVisibility(
                modifier = Modifier.align(Alignment.BottomCenter),
                visible = isShowingAdd.value,
            ) {
                GlassBottomSheet(
                    modifier = Modifier.align(Alignment.BottomCenter)
                        .sharedElement(
                            rememberSharedContentState("add_subject_button"),
                            this
                        )
                        .safeContentPadding(),
                    backdrop = backdrop
                ) {
                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        LLTextField(
                            text = name.value,
                            placeHolderText = "Enter class name",
                            onTextChange = { name.value = it }
                        )
                        LLTextField(
                            keyboardType = KeyboardType.Number,
                            placeHolderText = "Enter grade level",
                            text = gradeLevel.value,
                            onTextChange = { gradeLevel.value = it }
                        )
                        LLTextField(
                            keyboardType = KeyboardType.Number,
                            placeHolderText = "Enter capacity",
                            text = capacity.value,
                            onTextChange = { capacity.value = it }
                        )
                        Button(
                            modifier = Modifier.padding(top = 5.dp),
                            onClick = {
                                viewModel.addClass(classesReq = ClassesReq(
                                    name = name.value,
                                    grade_level = gradeLevel.value.toLong(),
                                    capacity = capacity.value.toLong(),
                                    academic_year_id = adYearsViewModel.lastEdYears.value?.id?:2L
                                    )
                                )
                                isShowingAdd.value = false
                            }
                        ) {
                            Text("Save")
                        }
                    }
                }
            }

            when {
                openAlertDialog.value->
                    AlertDialogExample(
                        onDismissRequest = {
                            openAlertDialog.value = false
                            selectedId.value = -1L
                        },
                        onConfirmation = {
                            viewModel.deleteClass(selectedId.value)
                            openAlertDialog.value = false
                            selectedId.value = -1L
                        },
                        dialogTitle = "Delete Class",
                        dialogText = "Are you sure you want to delete this class?",
                    )
            }


        }
    }
}