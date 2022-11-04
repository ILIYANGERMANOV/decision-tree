package com.ivyapps.decision.logic

import com.ivyapps.decision.data.TreeNode

fun TreeNode.addNode(
    toKey: String,
    atIndex: Int,
    node: TreeNode,
): TreeNode = this.updateNode(
    targetKey = toKey,
) { targetNode ->
    val newChildren = targetNode.children.toMutableList()
    newChildren.add(atIndex, node)
    targetNode.copy(children = newChildren)
}

fun TreeNode.updateNode(
    targetKey: String,
    update: (TreeNode) -> TreeNode
): TreeNode {
    if (this.key == targetKey) {
        return update(this)
    }

    return this.copy(
        children = this.children.map {
            it.updateNode(targetKey = targetKey, update = update)
        }
    )
}

fun TreeNode.removeNode(
    keyToRemove: String,
): TreeNode {
    fun TreeNode.removeNodeInternal(): TreeNode? {
        if (this.key == keyToRemove) return null
        return this.copy(
            children = this.children.mapNotNull {
                it.removeNodeInternal()
            }
        )
    }
    // Cannot remove the "Root" node
    return removeNodeInternal() ?: this.copy(children = emptyList())
}

fun TreeNode.find(targetKey: String): TreeNode? {
    if (key == targetKey) return this
    for (child in children) {
        val res = child.find(targetKey)
        if (res != null) return res
    }
    return null
}