package com.ivyapps.decision.logic

import com.ivyapps.decision.data.TreeNode
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe

class TreeSelectionTest : FreeSpec({
    "selects root" {
        val root = TreeNode("Root")

        val res = root.select(nodeKey = root.key)

        res shouldBe listOf(root.key)
    }

    "selects level-2" {
        val tree = TreeNode(
            title = "Root",
            key = "root",
            children = listOf(
                TreeNode(
                    key = "l1",
                    title = "Level 1", children = listOf(
                        TreeNode("Level 2", key = "l2")
                    )
                )
            )
        )

        val res = tree.select(nodeKey = "l2")

        res shouldBe listOf("root", "l1", "l2")
    }
})