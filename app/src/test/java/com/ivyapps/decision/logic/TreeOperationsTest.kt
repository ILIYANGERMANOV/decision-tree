package com.ivyapps.decision.logic

import androidx.compose.ui.graphics.Color
import com.ivyapps.decision.data.TreeNode
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe

class TreeOperationsTest : FreeSpec({
    fun TreeNode.toList(): List<Pair<String, String>> {
        return listOf(key to title) + children.flatMap { it.toList() }
    }

    "adds a node to root" {
        val root = TreeNode(title = "Root", color = Color.Black)
        val newNode = TreeNode(title = "New", color = Color.Blue)

        val res = root.addNode(
            toKey = root.key,
            atIndex = 0,
            node = newNode
        )

        res shouldBe root.copy(
            children = listOf(newNode)
        )
    }

    "adds a node to nested level" {
        val tree = TreeNode(
            key = "root",
            title = "Root",
            children = listOf(
                TreeNode(
                    key = "key1",
                    title = "Level 1"
                )
            )
        )
        val newNode = TreeNode(key = "new", title = "New")

        val res = tree.addNode(
            toKey = "key1",
            atIndex = 0,
            node = newNode
        )

        res.toList() shouldBe listOf(
            "root" to "Root", "key1" to "Level 1", "new" to "New"
        )
    }

    "removing root does nothing" {
        val tree = TreeNode("Root")

        val res = tree.removeNode(keyToRemove = tree.key)

        res shouldBe tree
    }

    "updates the root" {
        val tree = TreeNode("Root")

        val res = tree.updateNode(targetKey = tree.key) {
            it.copy(title = "Root New", color = Color.Red)
        }

        res shouldBe TreeNode(
            key = tree.key,
            title = "Root New",
            color = Color.Red,
        )
    }

    // region property-based

    // endregion
})