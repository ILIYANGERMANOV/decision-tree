package com.ivyapps.decision.ui.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.*
import androidx.compose.ui.zIndex
import com.ivyapps.decision.data.TreeNode
import com.ivyapps.decision.ui.theme.preview.AppPreview
import com.ivyapps.decision.ui.util.contrastColor


@OptIn(ExperimentalUnitApi::class)
@Composable
fun TreeNodeCircle(
    node: TreeNode,
    selected: Boolean,
    modifier: Modifier = Modifier,
    size: Dp,
    fontSize: TextUnit,
    onClick: (TreeNode) -> Unit,
) {
    Surface(
        modifier = modifier.zIndex(16f),
        shape = CircleShape,
        color = if (selected) node.color else MaterialTheme.colorScheme.background,
        contentColor = when (selected) {
            true -> contrastColor(node.color)
            false -> MaterialTheme.colorScheme.onBackground
        },
        border = if (!selected) BorderStroke(2.dp, node.color) else null
    ) {
        Box(
            modifier = Modifier
                .size(size)
                .clickable {
                    onClick(node)
                },
            contentAlignment = Alignment.Center,
        ) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 4.dp),
                text = node.title,
                color = when (selected) {
                    true -> contrastColor(node.color)
                    false -> MaterialTheme.colorScheme.onBackground
                },
                fontSize = fontSize,
                maxLines = 2,
                lineHeight = TextUnit(
                    value = fontSize.value + 2.sp.value,
                    type = TextUnitType.Sp,
                ),
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
                children = emptyList()
            ),
            selected = true,
            size = 96.dp,
            fontSize = 16.sp,
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
                children = emptyList()
            ),
            selected = false,
            size = 96.dp,
            fontSize = 16.sp,
            onClick = {},
        )
    }
}
// endregion