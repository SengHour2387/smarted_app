package com.hourdex.smartedu.features.students

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.hourdex.smartedu.R
import com.hourdex.smartedu.uis.components.LLTextField
import com.kyant.backdrop.backdrops.layerBackdrop
import com.kyant.backdrop.backdrops.rememberLayerBackdrop
import com.kyant.backdrop.drawBackdrop
import com.kyant.backdrop.effects.blur
import com.kyant.backdrop.effects.lens
import com.kyant.backdrop.effects.vibrancy
import com.kyant.capsule.ContinuousRoundedRectangle

@Composable
fun StudentDetailEdit(
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
    id: Long,
    studentsViewModel: StudentsViewModel = hiltViewModel(),
    onBack:()-> Unit = {}
) {

    val backdrop = rememberLayerBackdrop()
    val backgroundColor = MaterialTheme.colorScheme.background
    val primaryColor = MaterialTheme.colorScheme.primary

    var student by remember { mutableStateOf<StudentRes?>(null) }


    val name = rememberSaveable { mutableStateOf("") }
    val email = rememberSaveable { mutableStateOf("") }
    val phone = rememberSaveable { mutableStateOf("") }
    val password = rememberSaveable { mutableStateOf("") }
    val editable = rememberSaveable { mutableStateOf(false)}

    LaunchedEffect(id) {
        val s =  studentsViewModel.getStudentById(id)
        student = s
        name.value = s?.full_name ?: ""
        email.value = s?.users?.email ?: ""
        phone.value = s?.users?.phone ?: ""
        password.value = ""
    }

    with(sharedTransitionScope) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .sharedElement(
                    sharedContentState = rememberSharedContentState("details_edit_student_${id}"),
                    animatedVisibilityScope = animatedVisibilityScope
                )
                .background(backgroundColor)
        ) {
            LazyColumn(
                modifier = Modifier
                    .layerBackdrop(backdrop)
                    .fillMaxWidth(),
                contentPadding = PaddingValues(top = 150.dp)
            ) {
                item {

                    Text("Full Name",color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.padding(start = 15.dp),
                    )
                    LLTextField(
                        editable = editable.value,
                        placeHolderText = "Enter student's full name",
                        text = name.value,
                        onTextChange = { name.value = it }
                    )
                    Text("Email",color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.padding(top = 15.dp, start = 15.dp),
                    )
                    LLTextField(
                        editable = editable.value,
                        placeHolderText = "Enter student's email",
                        text = email.value,
                        onTextChange = { email.value = it }
                    )
                    Text("Phone",color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.padding(top = 15.dp, start = 15.dp),
                    )
                    LLTextField(
                        editable = editable.value,
                        placeHolderText = "Enter student's phone number",
                        text = phone.value,
                        onTextChange = { phone.value = it }
                    )
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp),
                        textAlign = TextAlign.Center,
                        text = "password can be changed by the student later",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(0.6f)
                    )

                }
            }

            Box(
                Modifier
                    .padding(start = 15.dp, top = 60.dp)
                    .size(40.dp)
                    .drawBackdrop(
                        backdrop = backdrop,
                        shape = { CircleShape },
                        effects = {
                            blur(6f)
                            vibrancy()
                            lens(40f,50f, chromaticAberration = true, depthEffect = false)
                        }
                    )
                    .clip(shape = CircleShape)
                    .clickable(
                        onClick = onBack
                    )
                ,
                contentAlignment = Alignment.Center
            ) {
                Image(
                    colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurface),
                    painter = painterResource(R.drawable.round_arrow_back_ios_new_24),contentDescription = null)
            }

            AnimatedVisibility(
                visible = editable.value,
                modifier = Modifier.align(Alignment.BottomCenter)
            ) {
                Row{
                    Box(
                        modifier = Modifier
                            .sharedElement(
                                sharedContentState = rememberSharedContentState("edit_student_btn"),
                                animatedVisibilityScope = animatedVisibilityScope
                            )
                            .safeContentPadding()
                            .drawBackdrop(
                                backdrop = backdrop,
                                shape = { ContinuousRoundedRectangle(32.dp) },
                                effects = {
                                    vibrancy()
                                    blur(2f.dp.toPx())
                                    lens(
                                        14f.dp.toPx(),
                                        24f.dp.toPx(),
                                        false,
                                        chromaticAberration = true
                                    )
                                },
                                onDrawSurface = { drawRect(primaryColor.copy(0.2f)) }
                            )
                            .clickable(
                                onClick = {
                                    editable.value = false
                                }
                            )
                            .animateContentSize()
                            .clip(ContinuousRoundedRectangle(32.dp))
                            .padding(vertical = 8.dp, horizontal = 12.dp)
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("Cancel", color = MaterialTheme.colorScheme.onSurface)
                            Image(
                                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurface),
                                painter = painterResource(R.drawable.round_cancel_24),contentDescription = null)
                        }
                    }
                    Box(
                        modifier = Modifier
                            .sharedElement(
                                sharedContentState = rememberSharedContentState("edit_student_btn"),
                                animatedVisibilityScope = animatedVisibilityScope
                            )
                            .safeContentPadding()
                            .drawBackdrop(
                                backdrop = backdrop,
                                shape = { ContinuousRoundedRectangle(32.dp) },
                                effects = {
                                    vibrancy()
                                    blur(2f.dp.toPx())
                                    lens(
                                        14f.dp.toPx(),
                                        24f.dp.toPx(),
                                        false,
                                        chromaticAberration = true
                                    )
                                },
                                onDrawSurface = { drawRect(primaryColor.copy(0.2f)) }
                            )
                            .clickable(
                                onClick = {
                                    student?.let { currentStudent ->
                                        if (email.value.isNotEmpty()
                                            || phone.value.isNotEmpty()
                                            || name.value.isNotEmpty()
                                        ) {
                                            val updateReq = StudentUpdateReq(
                                                full_name = name.value.ifEmpty { null },
                                                email = email.value.ifEmpty { null },
                                                phone = phone.value.ifEmpty { null },
                                            )
                                            studentsViewModel.updateStudent(updateReq,id)
                                            editable.value = false
                                        }
                                    }
                                }
                            )
                            .animateContentSize()
                            .clip(ContinuousRoundedRectangle(32.dp))
                            .padding(vertical = 8.dp, horizontal = 12.dp)
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("Save", color = MaterialTheme.colorScheme.onSurface)
                            Image(
                                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurface),
                                painter = painterResource(R.drawable.round_check_circle_24),contentDescription = null)
                        }
                    }
                }
            }

            AnimatedVisibility(
                visible = !editable.value,
                modifier = Modifier.align(Alignment.BottomCenter)
            ) {
                Box(
                    modifier = Modifier
                        .sharedBounds(
                            resizeMode = SharedTransitionScope.ResizeMode.RemeasureToBounds,
                            sharedContentState = rememberSharedContentState("edit_student_btn"),
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
                                lens(
                                    14f.dp.toPx(),
                                    24f.dp.toPx(),
                                    false,
                                    chromaticAberration = true
                                )
                            },
                            onDrawSurface = { drawRect(primaryColor.copy(0.2f)) }
                        )
                        .clickable(
                            onClick = {
                                editable.value = true
                            }
                        )
                        .animateContentSize()
                        .clip(ContinuousRoundedRectangle(32.dp))
                        .padding(vertical = 8.dp, horizontal = 12.dp)
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Edit", color = MaterialTheme.colorScheme.onSurface)
                        Image(
                            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurface),
                            painter = painterResource(R.drawable.style_bulk_6),contentDescription = null)
                    }
                }
            }
        }
    }
}