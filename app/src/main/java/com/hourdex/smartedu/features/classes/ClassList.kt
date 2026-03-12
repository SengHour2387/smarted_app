package com.hourdex.smartedu.features.classes

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.kyant.backdrop.backdrops.layerBackdrop
import dev.chrisbanes.haze.hazeSource

@Composable
fun ClassesList(
    modifier: Modifier = Modifier,
    classes: List<ClassesRes>,
    onClick: (ClassesRes) -> Unit = {},
    isSelectable: Boolean = false,
    onClickEdit: (ClassesRes) -> Unit,
    onClickDelete: (ClassesRes) -> Unit,
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(top = 150.dp, start = 8.dp, end = 8.dp),
    ) {
        items(classes) {
            ClassesTile(
                classesRes = it,
                isSelectable = isSelectable,
                onClick = { onClick(it)},
                onClickEdit = { onClickEdit(it) },
                onClickDelete = { onClickDelete(it) }
            )
        }
    }
}