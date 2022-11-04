package com.ivyapps.decision.ui.screen.tree.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.DeleteForever
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ivyapps.decision.data.TreeNode
import com.ivyapps.decision.ui.screen.tree.NodeCard
import com.ivyapps.decision.ui.screen.tree.TreeEvent
import com.ivyapps.decision.ui.theme.*
import com.ivyapps.decision.ui.theme.preview.AppPreview
import com.ivyapps.decision.ui.util.animatedKeyboardPadding
import com.ivyapps.decision.ui.util.rememberContrast
import com.ivyapps.decision.ui.util.rememberDynamicContrast
import kotlinx.coroutines.delay

@Composable
fun ModifyNodeCard(
    nodeCard: NodeCard?,
    onAddNodeModal: (TreeEvent.AddNodeModal) -> Unit,
    onAddNode: (TreeEvent.AddNode) -> Unit,
    onEditNode: (TreeEvent.EditNode) -> Unit,
    onDeleteNode: (TreeEvent.DeleteNode) -> Unit,
) {
    val keyboardPadding = animatedKeyboardPadding()
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = keyboardPadding)
    ) {
        AnimatedVisibility(
            modifier = Modifier.align(Alignment.BottomCenter),
            visible = nodeCard != null
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 16.dp),
                shape = MaterialTheme.shapes.large
            ) {
                val editNode = (nodeCard as? NodeCard.EditNode)?.node
                var selectedColor by remember {
                    mutableStateOf(editNode?.color ?: Blue3)
                }
                var title by remember { mutableStateOf(editNode?.title ?: "") }
                Spacer(modifier = Modifier.height(12.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TitleInput(
                        modifier = Modifier
                            .weight(1f)
                            .padding(end = 8.dp),
                        initialValue = editNode?.title ?: "",
                        onValueChange = { title = it }
                    )
                    if (editNode != null) {
                        var deleteConfirmed by remember { mutableStateOf(false) }
                        IconButton(
                            modifier = Modifier.clip(CircleShape),
                            onClick = {
                                if (deleteConfirmed) {
                                    onDeleteNode(TreeEvent.DeleteNode(key = editNode.key))
                                } else {
                                    deleteConfirmed = true
                                }
                            },
                            colors = IconButtonDefaults.iconButtonColors(
                                containerColor = when (deleteConfirmed) {
                                    true -> MaterialTheme.colorScheme.error
                                    false -> MaterialTheme.colorScheme.errorContainer
                                },
                                contentColor = when (deleteConfirmed) {
                                    true -> MaterialTheme.colorScheme.onError
                                    false -> MaterialTheme.colorScheme.error
                                },
                            )
                        ) {
                            Icon(
                                imageVector = if (deleteConfirmed)
                                    Icons.Rounded.DeleteForever else Icons.Rounded.DeleteForever,
                                contentDescription = "delete"
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                ColorPicker(
                    selectedColor = selectedColor,
                    onColorSelect = {
                        selectedColor = it
                    }
                )
                if (editNode != null) {
                    Spacer(modifier = Modifier.height(12.dp))
                    Divider(Modifier.fillMaxWidth())
                    Spacer(modifier = Modifier.height(8.dp))
                    NodeChildren(
                        parentKey = editNode.key,
                        children = editNode.children,
                        onAddNode = { parentKey, atIndex ->
                            onAddNodeModal(
                                TreeEvent.AddNodeModal(
                                    card = NodeCard.NewNode(
                                        parentKey = parentKey,
                                        atIndex = atIndex,
                                    )
                                )
                            )
                        },
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    onClick = {
                        when (nodeCard) {
                            is NodeCard.EditNode -> onEditNode(
                                TreeEvent.EditNode(
                                    key = nodeCard.node.key,
                                    title = title,
                                    color = selectedColor,
                                )
                            )
                            is NodeCard.NewNode -> onAddNode(
                                TreeEvent.AddNode(
                                    parentKey = nodeCard.parentKey,
                                    atIndex = nodeCard.atIndex,
                                    title = title,
                                    color = selectedColor,
                                )
                            )
                            null -> {}
                        }
                    }
                ) {
                    Text(text = if (editNode != null) "Save" else "Add")
                }
                Spacer(modifier = Modifier.height(12.dp))
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TitleInput(
    initialValue: String,
    modifier: Modifier = Modifier,
    onValueChange: (String) -> Unit,
) {
    var textField by remember {
        // move the cursor at the end of the text
        val selection = TextRange(initialValue.length)
        mutableStateOf(TextFieldValue(initialValue, selection))
    }
    LaunchedEffect(initialValue) {
        if (initialValue != textField.text && initialValue.isNotBlank()) {
            delay(50) // fix race condition
            textField = TextFieldValue(
                initialValue, TextRange(initialValue.length)
            )
        }
    }
    OutlinedTextField(
        modifier = modifier,
        value = textField,
        onValueChange = { newValue ->
            // new value different than the current one
            if (newValue.text != textField.text) {
                onValueChange(newValue.text)
            }
            textField = newValue
        },
        placeholder = {
            Text(
                text = "Enter node title",
                color = MaterialTheme.colorScheme.scrim,
            )
        }
    )
}

// region Color Picker
@Composable
private fun ColorPicker(
    modifier: Modifier = Modifier,
    selectedColor: Color,
    onColorSelect: (Color) -> Unit,
) {
    Row(
        modifier = modifier.padding(horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Spacer(modifier = Modifier.weight(1f))
        ColorButton(color = Blue3, selected = selectedColor == Blue3, onClick = onColorSelect)
        Spacer(modifier = Modifier.weight(1f))
        ColorButton(color = Green, selected = selectedColor == Green, onClick = onColorSelect)
        Spacer(modifier = Modifier.weight(1f))
        ColorButton(color = Red, selected = selectedColor == Red, onClick = onColorSelect)
        Spacer(modifier = Modifier.weight(1f))
        ColorButton(color = Orange, selected = selectedColor == Orange, onClick = onColorSelect)
        Spacer(modifier = Modifier.weight(1f))
        ColorButton(color = Purple80, selected = selectedColor == Purple80, onClick = onColorSelect)
        Spacer(modifier = Modifier.weight(1f))
    }
}

@Composable
private fun ColorButton(
    color: Color,
    selected: Boolean,
    onClick: (Color) -> Unit,
) {
    val selectedModifier = if (selected)
        Modifier.border(4.dp, rememberDynamicContrast(color = color), CircleShape)
    else Modifier

    Spacer(
        modifier = Modifier
            .size(48.dp)
            .clip(CircleShape)
            .background(color)
            .then(selectedModifier)
            .clickable {
                onClick(color)
            }
    )
}
// endregion

// region Add child options
@Composable
private fun NodeChildren(
    parentKey: String,
    children: List<TreeNode>,
    onAddNode: (String, Int) -> Unit,
) {
    LazyRow(
        verticalAlignment = Alignment.CenterVertically
    ) {
        item {
            Spacer(modifier = Modifier.width(8.dp))
            if (children.isEmpty()) {
                Spacer(modifier = Modifier.width(8.dp))
                Button(
                    onClick = {
                        onAddNode(parentKey, 0)
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.secondary,
                        contentColor = MaterialTheme.colorScheme.onSecondary,
                    ),
                    contentPadding = PaddingValues(
                        top = 4.dp,
                        bottom = 4.dp,
                        start = 8.dp,
                        end = 16.dp,
                    )
                ) {
                    Icon(imageVector = Icons.Rounded.Add, contentDescription = "Add")
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "Add node")
                }
            } else {
                AddButton {
                    onAddNode(parentKey, 0)
                }
            }
            Spacer(modifier = Modifier.width(8.dp))
        }
        itemsIndexed(items = children) { index, node ->
            ChildNode(node = node)
            Spacer(modifier = Modifier.width(8.dp))
            AddButton {
                onAddNode(parentKey, index + 1)
            }
            Spacer(modifier = Modifier.width(8.dp))
        }
        item {
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
private fun ChildNode(node: TreeNode) {
    Text(
        modifier = Modifier
            .clip(CircleShape)
            .background(node.color)
            .padding(all = 12.dp),
        text = node.title,
        style = MaterialTheme.typography.bodySmall,
        textAlign = TextAlign.Center,
        fontWeight = FontWeight.Bold,
        color = rememberContrast(color = node.color),
        maxLines = 2,
    )
}

@Composable
private fun AddButton(
    onClick: () -> Unit,
) {
    IconButton(
        modifier = Modifier.width(32.dp),
        onClick = onClick,
    ) {
        Icon(
            imageVector = Icons.Rounded.Add,
            contentDescription = "add",
        )
    }
}
// endregion


// region Preview
@Preview
@Composable
private fun Preview_EditNode_Empty() {
    AppPreview {
        ModifyNodeCard(
            nodeCard = NodeCard.EditNode(TreeNode("Root")),
            onAddNode = {},
            onAddNodeModal = {},
            onDeleteNode = {},
            onEditNode = {}
        )
    }
}

@Preview
@Composable
private fun Preview_EditNode_WithChildren() {
    AppPreview {
        ModifyNodeCard(
            nodeCard = NodeCard.EditNode(
                TreeNode(
                    title = "Root",
                    children = listOf(
                        TreeNode("Node 1", Purple80),
                        TreeNode("Node 2", Red),
                        TreeNode("Node 3", Green),
                    )
                ),
            ),
            onDeleteNode = {},
            onEditNode = {},
            onAddNodeModal = {},
            onAddNode = {}
        )
    }
}

@Preview
@Composable
private fun Preview_NewNode() {
    AppPreview {
        ModifyNodeCard(
            nodeCard = NodeCard.NewNode(
                parentKey = "key", atIndex = 0
            ),
            onAddNodeModal = {},
            onAddNode = {},
            onEditNode = {},
            onDeleteNode = {}
        )
    }
}
// endregion