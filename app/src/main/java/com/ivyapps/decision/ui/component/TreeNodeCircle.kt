package com.ivyapps.decision.ui.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ivyapps.decision.data.TreeNode
import com.ivyapps.decision.ui.theme.preview.AppPreview
import com.ivyapps.decision.ui.util.contrastColor


@Composable
fun TreeNodeCircle(
    node: TreeNode,
    modifier: Modifier = Modifier,
    size: Dp = 96.dp,
    fontSize: TextUnit = 16.sp,
    onClick: () -> Unit,
) {
    val selected by remember { derivedStateOf { node.selected } }
    Surface(
        modifier = modifier,
        shape = CircleShape,
        color = if (selected) node.color else Color.Transparent,
        contentColor = when (selected) {
            true -> contrastColor(node.color)
            false -> MaterialTheme.colorScheme.onBackground
        },
        border = if (!selected) BorderStroke(2.dp, node.color) else null
    ) {
        Box(
            modifier = Modifier
                .size(size)
                .clickable(onClick = onClick),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = node.title,
                color = when (selected) {
                    true -> contrastColor(node.color)
                    false -> MaterialTheme.colorScheme.onBackground
                },
                fontSize = fontSize,
                maxLines = 2,
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}


// region Preview
@Preview
@Composable
private fun Preview_Selected() {
    AppPreview {
        TreeNodeCircle(
            node = TreeNode(
                title = "Stocks?",
                desc = "Buy now or not?",
                color = Color.Blue,
                selected = true,
                children = emptyList()
            ),
            onClick = {},
        )
    }
}

@Preview
@Composable
private fun Preview_NotSelected() {
    AppPreview {
        TreeNodeCircle(
            node = TreeNode(
                title = "Stocks?",
                desc = "Buy now or not?",
                color = Color.Blue,
                selected = false,
                children = emptyList()
            ),
            onClick = {},
        )
    }
}
// endregion