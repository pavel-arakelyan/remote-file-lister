package com.tools.remotefilelister.ui.view.tree

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.tools.remotefilelister.LoggerProvider
import com.tools.remotefilelister.rpc.FileNode
import org.jetbrains.jewel.ui.component.Text

/**
 * @author pavel.arakelyan
 */
@Composable
fun FileTreeNodeRow(
	flatNode: FlatFileNode,
	flatNodeList: SnapshotStateList<FlatFileNode>,
	lazyLoader: LazyFileTreeLoader,
	onFileSelected: (FileNode) -> Unit,
	onCollapse: (List<FileNode>) -> Unit
) {
//	LoggerProvider.logger.fine("Rendered ${flatNode.path()}")
	Row(
		modifier = Modifier
			.fillMaxWidth()
			.background(if (flatNode.isSelected.value) Color.LightGray else Color.Transparent)
			.padding(
				start = (flatNode.depth * 16).dp,
				top = 4.dp,
				bottom = 4.dp
			)
			.clickable {
				flatNode.isSelected.value = !flatNode.isSelected.value
				onFileSelected(flatNode.node)
			},
		verticalAlignment = Alignment.CenterVertically
	) {
		if (flatNode.isDirectory()) {
			Text(
				text = if (flatNode.isExpanded) "[-]" else "[+]",
				modifier = Modifier
					.padding(end = 8.dp)
					.clickable {
						if (flatNode.isExpanded) {
							val childrenToRemove = collapseDirectory(flatNode, flatNodeList)
							onCollapse(childrenToRemove.map { it.node })
						} else {
							expandDirectory(flatNode, flatNodeList, lazyLoader)
						}
					},
				color = Color.Blue
			)
		}
		Text(
			text = flatNode.name(),
			modifier = Modifier.weight(1f)
		)
	}
}

fun expandDirectory(
	parentNode: FlatFileNode,
	flatNodeList: SnapshotStateList<FlatFileNode>,
	lazyLoader: LazyFileTreeLoader
) {
	val parentIndex = flatNodeList.indexOf(parentNode)
	if (parentIndex == -1) error("No node found for ${parentNode.path()}")

	lazyLoader.preloadChildren(parentNode) { newNodes, _ ->
		flatNodeList.addAll(parentIndex + 1, newNodes)
		parentNode.isExpanded = true
		parentNode.offset += newNodes.size
		LoggerProvider.logger.fine("Expanded ${parentNode.path()}: Loaded ${newNodes.size} items, full size: ${flatNodeList.size}")
	}

}

fun collapseDirectory(
	parentNode: FlatFileNode,
	flatNodeList: SnapshotStateList<FlatFileNode>
) : List<FlatFileNode>{
	val parentIndex = flatNodeList.indexOf(parentNode)
	if (parentIndex == -1) error("No node found for ${parentNode.path()}")

	val childrenToRemove = flatNodeList.subList(parentIndex + 1, flatNodeList.size)
		.takeWhile { it.path().startsWith("${parentNode.path()}/") }
	flatNodeList.removeAll(childrenToRemove)
	// reset parent node
	parentNode.isExpanded = false
	parentNode.fullyLoaded = false
	parentNode.offset = 0
	LoggerProvider.logger.fine("Collapsed ${parentNode.path()}: Removed ${childrenToRemove.size} children, full size: ${flatNodeList.size}")
	return childrenToRemove
}