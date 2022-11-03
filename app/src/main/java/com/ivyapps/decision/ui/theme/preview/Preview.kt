package com.ivyapps.decision.ui.theme.preview

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import com.ivyapps.decision.ui.theme.DecisionTreeTheme

@Composable
fun AppPreview(
    content: @Composable () -> Unit,
) {
    DecisionTreeTheme {
        Surface(
            color = MaterialTheme.colorScheme.background,
            content = content,
        )
    }
}