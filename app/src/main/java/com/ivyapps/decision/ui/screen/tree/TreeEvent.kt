package com.ivyapps.decision.ui.screen.tree

import androidx.compose.ui.graphics.Color
import com.ivyapps.decision.data.TreeNode

sealed interface TreeEvent {
    data class AddNodeModal(
        val card: NodeCard,
    ) : TreeEvent

    data class AddNode(
        val parentKey: String,
        val atIndex: Int,
        val title: String,
        val color: Color,
    ) : TreeEvent

    data class EditNode(
        val key: String,
        val title: String,
        val color: Color,
    ) : TreeEvent

    data class DeleteNode(
        val key: String,
    ) : TreeEvent

    data class NodeClicked(val node: TreeNode) : TreeEvent
    object ResetSelected : TreeEvent
    object ToggleEditMode : TreeEvent
}