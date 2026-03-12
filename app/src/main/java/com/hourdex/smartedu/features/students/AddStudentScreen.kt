package com.hourdex.smartedu.features.students

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.animateContentSize
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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.hourdex.smartedu.R
import com.hourdex.smartedu.uis.components.AlertDialogExample
import com.hourdex.smartedu.uis.components.LLTextField
import com.kyant.backdrop.backdrops.layerBackdrop
import com.kyant.backdrop.backdrops.rememberBackdrop
import com.kyant.backdrop.backdrops.rememberLayerBackdrop
import com.kyant.backdrop.drawBackdrop
import com.kyant.backdrop.effects.blur
import com.kyant.backdrop.effects.lens
import com.kyant.backdrop.effects.vibrancy
import com.kyant.capsule.ContinuousRoundedRectangle
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun AddStudentScreen(
    viewModel: StudentsViewModel = hiltViewModel(),
    sharedTransitionScope: SharedTransitionScope,
    onBack: () -> Unit,
    animatedVisibilityScope: AnimatedVisibilityScope,
) {

    val name = rememberSaveable { mutableStateOf("") }
    val email = rememberSaveable { mutableStateOf("") }
    val phone = rememberSaveable { mutableStateOf("") }
    val password = rememberSaveable { mutableStateOf("") }
    val backdrop = rememberLayerBackdrop()
    val backgroundColor = MaterialTheme.colorScheme.background
    val primaryColor = MaterialTheme.colorScheme.primary

    val addStudentSate by viewModel.addStudentState.collectAsStateWithLifecycle()
    var showConfirmationDialog by rememberSaveable { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    if(
        name.value.isNotEmpty() ||
        email.value.isNotEmpty() ||
        phone.value.isNotEmpty() ||
        password.value.isNotEmpty()
    ) {
        BackHandler() {
            showConfirmationDialog = true
        }
    }

    with(sharedTransitionScope) {
        Box(
            Modifier
                .fillMaxSize()
                .sharedBounds(
                    resizeMode = SharedTransitionScope.ResizeMode.scaleToBounds(),
                    sharedContentState = rememberSharedContentState("add_student_screen"),
                    animatedVisibilityScope = animatedVisibilityScope
                )
                .background(color = MaterialTheme.colorScheme.background)
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
                        placeHolderText = "Enter student's full name",
                        text = name.value,
                        onTextChange = { name.value = it }
                    )
                    Text("Email",color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.padding(top = 15.dp, start = 15.dp),
                        )
                    LLTextField(
                        placeHolderText = "Enter student's email",
                        text = email.value,
                        onTextChange = { email.value = it }
                    )
                    Text("Phone",color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.padding(top = 15.dp, start = 15.dp),
                        )
                    LLTextField(
                        placeHolderText = "Enter student's phone number",
                        text = phone.value,
                        onTextChange = { phone.value = it }
                    )
                    Text("Password",color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.padding(top = 15.dp, start = 15.dp),
                        )
                    LLTextField(
                        placeHolderText = "Enter student's password",
                        text = password.value,
                        onTextChange = { password.value = it }
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

            if(showConfirmationDialog) {
               if(
                   name.value.isNotEmpty() ||
                   email.value.isNotEmpty() ||
                   phone.value.isNotEmpty() ||
                   password.value.isNotEmpty()
               ) {
                   AlertDialogExample(
                       onConfirmation = {
                           onBack()
                           showConfirmationDialog = false
                       },
                       onDismissRequest = {
                           showConfirmationDialog = false
                       },
                       dialogTitle = "Are you sure?",
                       dialogText = "Do you have to leave and clear?"
                   )
               }
            }

            Box(
                modifier = Modifier
                    .safeContentPadding()
                    .align(Alignment.BottomCenter)
                    .drawBackdrop(
                        backdrop = backdrop,
                        shape = { ContinuousRoundedRectangle(32.dp) },
                        effects = {
                            vibrancy()
                            blur(2f.dp.toPx())
                            lens(14f.dp.toPx(), 24f.dp.toPx(), false,chromaticAberration = true)
                        },
                        onDrawSurface = { drawRect(primaryColor.copy(0.2f)) }
                    )
                    .clickable(
                        onClick = {
                            viewModel.createStudent(
                                StudentReq(
                                    email = email.value,
                                    full_name = name.value,
                                    password = password.value,
                                    phone = phone.value,
                                )
                            )
                        }
                    )
                    .animateContentSize()
                    .clip(ContinuousRoundedRectangle(32.dp) )
                    .padding(vertical = 8.dp, horizontal = 12.dp)
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    when(addStudentSate) {
                        AddStudentSate.Idle-> {
                            Text("Save", color = MaterialTheme.colorScheme.onSurface)
                            Image(
                                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurface),
                                painter = painterResource(R.drawable.round_check_circle_24),
                                contentDescription = null)
                        }
                        AddStudentSate.Loading-> {
                            CircularProgressIndicator(
                                modifier = Modifier.size(100.dp).padding(20.dp),
                                strokeWidth = 8.dp
                            )
                        }
                        is AddStudentSate.Success -> {
                            Text("succeed ", color = MaterialTheme.colorScheme.onSurface)
                            name.value = ""
                            email.value = ""
                            phone.value = ""
                            password.value = ""
                        }
                        is AddStudentSate.Error -> {
                            Text((addStudentSate as AddStudentSate.Error).message, color = MaterialTheme.colorScheme.onSurface)
                        }
                    }
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
        }
    }
}