package com.ivyapps.decision.data

import androidx.compose.ui.graphics.Color
import com.ivyapps.decision.ui.theme.Blue3
import java.util.*

data class TreeNode(
    val title: String,
    val color: Color = Blue3,
    val desc: String? = null,
    val children: List<TreeNode> = emptyList(),
    val key: String = UUID.randomUUID().toString(),
)