package com.ivyapps.decision.ui.screen.tree.component

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material.icons.rounded.Preview
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ivyapps.decision.ui.theme.preview.AppPreview

@Composable
fun Toolbar(
    editMode: Boolean,
    onToggleEditMode: () -> Unit,
    onReset: () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(bottom = 16.dp)
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Button(
                colors = ButtonDefaults.buttonColors(
                    containerColor = when (editMode) {
                        true -> MaterialTheme.colorScheme.secondary
                        false -> MaterialTheme.colorScheme.primary
                    },
                    contentColor = when (editMode) {
                        true -> MaterialTheme.colorScheme.onSecondary
                        false -> MaterialTheme.colorScheme.onPrimary
                    }
                ),
                contentPadding = PaddingValues(
                    top = 8.dp,
                    bottom = 8.dp,
                    start = 12.dp,
                    end = 16.dp
                ),
                onClick = onToggleEditMode
            ) {
                Icon(
                    imageVector = if (editMode) Icons.Rounded.Edit else Icons.Rounded.Preview,
                    contentDescription = "edit mode"
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = if (editMode) "Edit mode" else "View mode")
            }
            Spacer(modifier = Modifier.weight(1f))
            Button(
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.errorContainer,
                    contentColor = MaterialTheme.colorScheme.onErrorContainer,
                ),
                onClick = onReset,
            ) {
                Text(text = "Reset")
            }
        }
    }
}


@Preview
@Composable
private fun Preview() {
    AppPreview {
        Toolbar(editMode = true, onToggleEditMode = {}, onReset = {})
    }
}