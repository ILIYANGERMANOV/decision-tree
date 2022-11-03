package com.ivyapps.decision.ui.screen.tree

import com.ivyapps.decision.data.TreeNode

data class TreeState(
    val tree: TreeNode,
    val selectedKeys: Set<String>,
)