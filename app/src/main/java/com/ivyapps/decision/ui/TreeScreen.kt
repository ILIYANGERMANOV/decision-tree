package com.ivyapps.decision.ui

import android.util.Log
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ivyapps.decision.data.TreeNode
import com.ivyapps.decision.ui.component.TreeNodeCircle

@Composable
fun TreeScreen() {
    Tree(
        root = TreeNode(
            title = "Stocks?",
            desc = "",
            color = Color.Blue,
            selected = true,
            children = emptyList()
        )
    )
}

@Composable
private fun Tree(root: TreeNode) {
    var offset by remember { mutableStateOf(IntOffset.Zero) }
    var scale by remember { mutableStateOf(1f) }

    Column(
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
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        val circleSize by remember { derivedStateOf { 96.dp * scale } }
        val fontSize by remember { derivedStateOf { 16.sp * scale } }

        TreeNodeCircle(
            node = root,
            size = circleSize,
            fontSize = fontSize,
        ) {

        }
    }
}
