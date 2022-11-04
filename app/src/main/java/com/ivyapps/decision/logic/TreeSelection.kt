package com.ivyapps.decision.logic

import com.ivyapps.decision.data.TreeNode

/**
 * @return a list of the nodes that must be selected
 */
fun TreeNode.select(
    nodeKey: String,
    path: List<String> = emptyList(),
): List<String>? {
    if (key == nodeKey) return path + listOf(nodeKey)
    for (child in children) {
        val result = child.select(nodeKey, path + listOf(key))
        if (result != null) return result
    }
    return null
}

/**
 * @return a list of the nodes that must be de-selected
 */
fun TreeNode.deselect(
    nodeKey: String,
): List<String>? {
    val target = find(nodeKey) ?: return null
    return target.nodes().map { it.key }
}

fun TreeNode.nodes(): List<TreeNode> {
    return listOf(this) + this.children + this.children.flatMap { it.nodes() }
}