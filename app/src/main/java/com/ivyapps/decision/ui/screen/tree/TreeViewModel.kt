package com.ivyapps.decision.ui.screen.tree

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ivyapps.decision.data.TreeNode
import com.ivyapps.decision.logic.*
import com.ivyapps.decision.ui.theme.Blue3
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TreeViewModel @Inject constructor() : ViewModel() {
    private val tree = MutableStateFlow(
        TreeNode(
            title = "Decision",
            color = Blue3
        )
    )
    private val selectedKeys = MutableStateFlow(setOf<String>())
    private val editMode = MutableStateFlow(true)
    private val nodeCard = MutableStateFlow<NodeCard?>(null)

    val state = combine(
        tree, selectedKeys, editMode, nodeCard
    ) { tree, selectedKeys, editMode, nodeCard ->
        TreeState(
            tree = tree,
            selectedKeys = selectedKeys,
            editMode = editMode,
            nodeCard = nodeCard,
        )
    }.stateIn(
        scope = viewModelScope,
        initialValue = TreeState(
            tree = tree.value,
            selectedKeys = emptySet(),
            editMode = true,
            nodeCard = null,
        ),
        started = SharingStarted.WhileSubscribed(5_000)
    )


    // region Event Handling
    fun onEvent(event: TreeEvent) {
        viewModelScope.launch {
            when (event) {
                is TreeEvent.AddNode -> handleAddNode(event)
                is TreeEvent.DeleteNode -> handleDeleteNode(event)
                is TreeEvent.EditNode -> handleEditNode(event)
                is TreeEvent.NodeClicked -> handleNodeClicked(event)
                TreeEvent.ToggleEditMode -> handleToggleEditMode()
                is TreeEvent.ShowAddNodeModal -> handleAddNodeModal(event)
                TreeEvent.CloseNodeModal -> handleCloseNodeModal()
            }
        }
    }

    private fun handleCloseNodeModal() {
        nodeCard.value = null
    }

    private fun handleAddNode(event: TreeEvent.AddNode) {
        val newNode = TreeNode(
            title = event.title,
            color = event.color,
        )
        tree.value = tree.value.addNode(
            toKey = event.parentKey,
            atIndex = event.atIndex,
            node = newNode
        )
        // Start editing node, immediately after saving it
        nodeCard.value = NodeCard.EditNode(newNode)
    }

    private fun handleDeleteNode(event: TreeEvent.DeleteNode) {
        tree.value = tree.value.removeNode(keyToRemove = event.key)
        nodeCard.value = null
    }

    private fun handleEditNode(event: TreeEvent.EditNode) {
        tree.value = tree.value.updateNode(
            targetKey = event.key
        ) {
            it.copy(
                title = event.title,
                color = event.color,
            )
        }
    }

    // region Node click
    private fun handleNodeClicked(event: TreeEvent.NodeClicked) {
        if (editMode.value) {
            editModeNodeClick(event.node)
        } else {
            viewModeNodeClick(nodeKey = event.node.key)
        }
    }

    private fun editModeNodeClick(node: TreeNode) {
        nodeCard.value = NodeCard.EditNode(node)
    }

    private fun viewModeNodeClick(nodeKey: String) {
        selectedKeys.value = if (selectedKeys.value.contains(nodeKey)) {
            // deselect node
            val deselected = tree.value.deselect(nodeKey) ?: return
            selectedKeys.value.filter { !deselected.contains(it) }.toSet()
        } else {
            // select node
            val selected = tree.value.select(nodeKey) ?: return
            selectedKeys.value + selected
        }
    }
    // endregion

    private fun handleAddNodeModal(event: TreeEvent.ShowAddNodeModal) {
        nodeCard.value = event.card
    }

    private fun handleToggleEditMode() {
        editMode.value = !editMode.value
        if (!editMode.value) {
            // node can be selected for edit only in edit mode
            nodeCard.value = null
        }
    }
    // endregion
}