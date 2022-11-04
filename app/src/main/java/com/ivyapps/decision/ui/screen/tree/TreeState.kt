package com.ivyapps.decision.ui.screen.tree

import com.ivyapps.decision.data.TreeNode

data class TreeState(
    val tree: TreeNode,
    val selectedKeys: Set<String>,
    val editMode: Boolean,
    val nodeCard: NodeCard?,
)

sealed interface NodeCard {
    data class NewNode(val parentKey: String, val atIndex: Int) : NodeCard
    data class EditNode(val node: TreeNode) : NodeCard
}