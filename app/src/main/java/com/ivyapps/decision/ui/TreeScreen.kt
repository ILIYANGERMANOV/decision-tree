package com.ivyapps.decision.ui

import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.*
import com.ivyapps.decision.data.TreeNode
import com.ivyapps.decision.ui.component.TreeNodeCircle
import com.ivyapps.decision.ui.theme.Gray
import com.ivyapps.decision.ui.theme.Orange

@Composable
fun TreeScreen() {
    val root by remember {
        mutableStateOf(
            TreeNode(
                title = "Stocks?",
                color = Color.Blue,
                children = listOf(
                    TreeNode(
                        title = "Market down",
                        color = Orange,
                        children = listOf(
                            TreeNode(
                                title = "recover in 1yr",
                                color = Color.Yellow,
                                children = listOf(
                                    TreeNode(
                                        title = "Buy with 20%",
                                        color = Color.Blue,
                                    )
                                )
                            ),
                            TreeNode(
                                title = "recover in 3+ yrs",
                                color = Color.Red,
                                children = listOf(
                                    TreeNode(
                                        title = "Invest 10%",
                                        color = Color.LightGray,
                                    ),
                                    TreeNode(
                                        title = "Wait!!",
                                        color = Color.Blue,
                                    )
                                )
                            ),
                        )
                    ),
                    TreeNode(
                        title = "Market up",
                        color = Color.Green,
                        children = listOf(
                            TreeNode(
                                title = "Meta",
                                color = Color.Magenta,
                                children = listOf()
                            ),
                            TreeNode(
                                title = "Alphabet",
                                color = Color.Cyan,
                                children = listOf()
                            ),
                        )
                    )
                )
            )
        )
    }
    var selectedKeys by remember { mutableStateOf(setOf(root.key)) }

    Tree(
        root = root,
        selectedKeys = selectedKeys,
        onClick = { node ->
            selectedKeys = if (selectedKeys.contains(node.key)) {
                // deselect node
                selectedKeys.filter { it != node.key }.toSet()
            } else {
                // select node
                selectedKeys + node.key
            }
        }
    )
}

@Composable
private fun Tree(
    root: TreeNode,
    selectedKeys: Set<String>,
    onClick: (TreeNode) -> Unit,
) {
    val initialOffsetTop = with(LocalDensity.current) {
        24.dp.toPx()
    }
    var offset by remember { mutableStateOf(IntOffset(x = 0, y = initialOffsetTop.toInt())) }
    var scale by remember { mutableStateOf(1f) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectTransformGestures { centroid, pan, zoom, rotation ->
                    Log.d(
                        "test", "centroid = $centroid, " +
                                "pan = $pan, zoom = $zoom, rotation = $rotation"
                    )
                    offset = IntOffset(
                        x = offset.x + pan.x.toInt(),
                        y = offset.y + pan.y.toInt()
                    )
                    scale *= zoom
                }
            }
            .offset { IntOffset(x = offset.x, y = offset.y) },
    ) {
        val circleSize by remember { derivedStateOf { 96.dp * scale } }
        val fontSize by remember { derivedStateOf { 16.sp * scale } }
        val verSpace by remember { derivedStateOf { 24.dp * scale } }
        val horSpace by remember { derivedStateOf { 24.dp * scale } }

        val screenWidth = LocalConfiguration.current.screenWidthDp.dp
        DrawLevel(
            level = 0,
            levelItems = listOf(
                DpOffset(x = screenWidth / 2 - circleSize / 2, y = 0.dp) to listOf(root)
            ),
            selectedKeys = selectedKeys,
            fontSize = fontSize,
            circleSize = circleSize,
            onClick = onClick
        )
    }
}

@Composable
private fun DrawLevel(
    level: Int,
    levelItems: List<Pair<DpOffset, List<TreeNode>>>,
    selectedKeys: Set<String>,
    fontSize: TextUnit,
    circleSize: Dp,
    onClick: (TreeNode) -> Unit,
) {
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    val paddingBetweenItems = 24.dp
    val allNodes = levelItems.flatMap { it.second }
    val itemsWidth = allNodes.size * circleSize + (allNodes.size - 1) * paddingBetweenItems

    var x = screenWidth / 2 - itemsWidth / 2
    val y = (circleSize + 48.dp) * level

    val nextLevelItems = mutableListOf<Pair<DpOffset, List<TreeNode>>>()
    for ((parentXY, nodeGroup) in levelItems) {
        for (node in nodeGroup) {
            val selected = remember(selectedKeys, node.key) {
                selectedKeys.contains(node.key)
            }
            if (level > 0) {
                Line(
                    start = parentXY,
                    end = DpOffset(
                        x = x + circleSize / 2,
                        y = y
                    ),
                    color = if (selected) node.color else Gray,
                )
            }
            TreeNodeCircle(
                modifier = Modifier.offset(
                    x = x,
                    y = y,
                ),
                node = node,
                selected = selected,
                size = circleSize,
                fontSize = fontSize,
                onClick = onClick,
            )
            if (node.children.isNotEmpty()) {
                nextLevelItems.add(
                    DpOffset(
                        x = x + circleSize / 2,
                        y = y + circleSize,
                    ) to node.children
                )
            }
            x += circleSize + paddingBetweenItems
        }
    }

    if (nextLevelItems.isNotEmpty()) {
        DrawLevel(
            level = level + 1,
            levelItems = nextLevelItems,
            selectedKeys = selectedKeys,
            fontSize = fontSize,
            circleSize = circleSize,
            onClick = onClick,
        )
    }
}

@Composable
private fun Line(
    start: DpOffset,
    color: Color,
    end: DpOffset
) {
    Canvas(
        modifier = Modifier.fillMaxSize()
    ) {
        drawLine(
            color = color,
            start = Offset(
                x = start.x.toPx(),
                y = start.y.toPx(),
            ),
            end = Offset(
                x = end.x.toPx(),
                y = end.y.toPx(),
            ),
            strokeWidth = 2.dp.toPx()
        )
    }
}
