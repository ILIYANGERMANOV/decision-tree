package com.ivyapps.decision.ui.screen.tree

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ivyapps.decision.data.TreeNode
import com.ivyapps.decision.ui.theme.Blue3
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TreeViewModel @Inject constructor() : ViewModel() {
    val state = MutableStateFlow(
        TreeState(
            tree = TreeNode(
                title = "Decision",
                color = Blue3
            ),
            selectedKeys = setOf()
        )
    )

    fun onEvent(event: TreeEvent) {
        viewModelScope.launch {
            when (event) {
                is TreeEvent.AddNode -> handleAddNode(event)
                is TreeEvent.DeleteNode -> TODO()
                is TreeEvent.EditNode -> TODO()
            }
        }
    }

    private fun handleAddNode(event: TreeEvent.AddNode) {
        val currentTree = state.value.tree
    }
}