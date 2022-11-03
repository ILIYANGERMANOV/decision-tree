package com.ivyapps.decision.data

import androidx.compose.ui.graphics.Color
import java.util.*

data class TreeNode(
    val title: String,
    val color: Color = Color.Blue,
    val desc: String? = null,
    val children: List<TreeNode> = emptyList(),
    val key: String = UUID.randomUUID().toString(),
)