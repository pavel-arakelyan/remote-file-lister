package com.tools.remotefilelister.ui.view.tree

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tools.remotefilelister.LoggerProvider
import com.tools.remotefilelister.rpc.FileNode
import com.tools.remotefilelister.rpc.FileService
import com.tools.remotefilelister.ui.view.tree.PageProperties.BUFFER_SIZE
import com.tools.remotefilelister.ui.view.tree.PageProperties.ITEMS_PER_PAGE
import org.jetbrains.jewel.foundation.lazy.SelectableLazyColumn
import org.jetbrains.jewel.foundation.lazy.SelectionMode
import org.jetbrains.jewel.foundation.lazy.items
import org.jetbrains.jewel.foundation.lazy.rememberSelectableLazyListState

/**
 * @author pavel.arakelyan
 */
@Composable
fun FileTree(
	fileService: FileService,
	rootPath: String,
	onFileSelected: (List<FileNode>) -> Unit
) {
	val lazyLoader = remember(fileService, rootPath) { LazyFileTreeLoader(fileService, rootPath) }

	val flatNodeList = remember { mutableStateListOf<FlatFileNode>() }
	val lazyListState = rememberSelectableLazyListState()
	val selectedItems = remember { mutableStateMapOf<String, FileNode>() } // Track selected items by path

	var rootForwardOffset by remember { mutableStateOf(0) }

	// Initialize the root directory
	LaunchedEffect(Unit) {
		LoggerProvider.logger.fine("Initializing root node for $rootPath")
		val rootNodes = fileService.listFiles(rootPath, offset = 0, limit = BUFFER_SIZE)
		flatNodeList.clear()
		flatNodeList.addAll(rootNodes.map { FlatFileNode(it, null, depth = 1, offset = 0) })
		rootForwardOffset += BUFFER_SIZE
		LoggerProvider.logger.fine("Loaded ${rootNodes.size} root files.")
	}

	LaunchedEffect(lazyListState.firstVisibleItemIndex) {
		val firstVisibleIndex = lazyListState.firstVisibleItemIndex

		LoggerProvider.logger.fine("FirsVisibleIndex: $firstVisibleIndex")

		if (firstVisibleIndex % BUFFER_SIZE == ITEMS_PER_PAGE) {
			lazyLoader.preloadForward(
				flatNodeList,
				firstVisibleIndex,
				rootForwardOffset
			) { count ->
				rootForwardOffset += count
			}
		}
	}

	SelectableLazyColumn(
		state = lazyListState,
		modifier = Modifier
			.fillMaxSize()
			.padding(8.dp),
		selectionMode = SelectionMode.Multiple,
		verticalArrangement = Arrangement.spacedBy(4.dp)
	) {
		items(flatNodeList.toList(), key = { node -> node.path() }) { flatNode ->
			FileTreeNodeRow(
				flatNode = flatNode,
				flatNodeList = flatNodeList,
				lazyLoader = lazyLoader,
				onFileSelected = { selectedNode ->
					if (selectedItems.containsKey(selectedNode.path)) {
						selectedItems.remove(selectedNode.path)
					} else {
						selectedItems[selectedNode.path] = selectedNode
					}
					onFileSelected(selectedItems.values.toList())
				},
				onCollapse = { removedNodes ->
					removedNodes.forEach {
						selectedItems.remove(it.path)
					}
					onFileSelected(selectedItems.values.toList())
				}
			)
		}
	}
}