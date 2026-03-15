package com.hourdex.smartedu.features.subjects

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.kyant.backdrop.backdrops.LayerBackdrop
import com.kyant.backdrop.backdrops.layerBackdrop
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.hazeSource
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

@Composable
fun SubjectList(
    subjects: List<SubjectsRes>,
    backgroundColor: Color = MaterialTheme.colorScheme.surfaceContainer,
    hasEdit: Boolean = true,
    onClickEdit: (Long) -> Unit,
    onClick: ( (Long) -> Unit),
    onClickDelete: (Long) -> Unit,
    backdrop: LayerBackdrop,
    hazeState: HazeState
) {
    LazyColumn(
        Modifier.fillMaxSize()
            .hazeSource(hazeState)
            .layerBackdrop(backdrop)
            .background(color = backgroundColor),
        contentPadding = PaddingValues(top = 150.dp, bottom = 150.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
        }
        if(subjects.isNotEmpty()) {
            items(subjects.size) { index ->
                SubjectTileAdmin(subjectsRes = subjects[index],
                    hasEdit = hasEdit,
                    onClickEdit = onClickEdit ,
                    onClick = onClick,
                    onClickDelete = onClickDelete
                )
            }
        } else {
            item {
                Text("No subjects found")
            }
        }
    }
}