package com.hourdex.smartedu.features.enroll

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.hourdex.smartedu.features.classes.ClassesList

@Composable
fun SelectClassScreen(
    enrollViewModel: EnrollViewModel,
    onBack: () -> Unit,
    animatedVisibilityScope: AnimatedVisibilityScope,
    sharedTransitionScope: SharedTransitionScope,
) {


    val availableClasses by enrollViewModel.availableClasses.collectAsStateWithLifecycle()
    val enrollSate by enrollViewModel.enrollState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        enrollViewModel.getAvailableClasses()
    }


with(sharedTransitionScope) {
    Box(
        modifier = Modifier.fillMaxSize()) {

        ClassesList(
            classes = availableClasses,
            isSelectable = true,
            onClick = {
                enrollViewModel.enrollManyStudents(it.id)
            },
            onClickEdit = { },
            onClickDelete = { },
        )
        Box(
            Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter).safeContentPadding(),
            contentAlignment = Alignment.Center
        ) {
            when(enrollSate) {
                is EnrollState.Error -> {
                    val message = (enrollSate as EnrollState.Error).message
                    Text(message, color = MaterialTheme.colorScheme.error)
                }
                is EnrollState.Idle -> {

                }
                is EnrollState.Loading -> {
                    CircularProgressIndicator()
                }
                is EnrollState.Success -> {
                    Column(
                        Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("Success", color = MaterialTheme.colorScheme.primary)
                        Button(
                            onBack
                        ) {
                            Text("Close Now")
                        }
                    }
                }

            }
        }

    }
}
}