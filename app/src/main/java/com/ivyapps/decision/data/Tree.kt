package com.ivyapps.decision.data

import androidx.compose.ui.graphics.Color

data class TreeNode(
    val title: String,
    val desc: String?,
    val color: Color,
    val selected: Boolean,
    val children: List<TreeNode>,
)