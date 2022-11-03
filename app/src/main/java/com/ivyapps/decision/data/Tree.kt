package com.ivyapps.decision.data

import androidx.compose.ui.graphics.Color
import java.util.*

data class TreeNode(
    val key: String = UUID.randomUUID().toString(),
    val title: String,
    val desc: String? = null,
    val color: Color,
    val children: List<TreeNode>,
)