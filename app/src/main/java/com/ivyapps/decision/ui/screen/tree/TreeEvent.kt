package com.ivyapps.decision.ui.screen.tree

import androidx.compose.ui.graphics.Color

sealed interface TreeEvent {
    data class AddNode(
        val parentKey: String,
        val index: Int,
        val title: String,
        val color: Color,
    ) : TreeEvent

    data class EditNode(
        val title: String,
        val color: Color,
    ) : TreeEvent

    data class DeleteNode(
        val key: String,
    ) : TreeEvent
}