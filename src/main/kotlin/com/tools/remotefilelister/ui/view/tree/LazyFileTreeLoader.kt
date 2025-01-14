package com.tools.remotefilelister.ui.view.tree

import androidx.compose.runtime.snapshots.SnapshotStateList
import com.tools.remotefilelister.LoggerProvider
import com.tools.remotefilelister.rpc.FileService

/**
 * Note! This lazy loader loads only forward
 *
 * @author pavel.arakelyan
 */
class LazyFileTreeLoader(
	private val fileService: FileService,
	private val rootPath: String,
) {

	fun preloadForward(
		flatNodeList: SnapshotStateList<FlatFileNode>,
		firstVisibleIndex: Int,
		rootForwardOffset: Int,
		onLoaded: (Int) -> Unit
	) {
		val firstVisibleNode = flatNodeList[firstVisibleIndex]
		val parentPath = firstVisibleNode.parentPath()
		LoggerProvider.logger.fine("Current dir: $parentPath")
		if (rootPath == parentPath) {
			if (offsetInVisibleBuffer(rootForwardOffset, firstVisibleIndex)) {
				preloadForRoot(
					flatNodeList,
					rootForwardOffset,
					parentPath,
					onLoaded
				)
			}
		} else {
			val parentNode = firstVisibleNode.parentNode!!
			val parentIndex = flatNodeList.indexOf(parentNode)
			if (parentIndex == -1 || parentNode.fullyLoaded) return
			if (offsetInVisibleBuffer(parentNode.offset, firstVisibleIndex)) {
				preloadChildren(
					parentNode,
				) { newNodes, isFullyLoaded ->
					if (!isFullyLoaded) {
						flatNodeList.addAll(parentIndex + parentNode.offset, newNodes)
					}
					parentNode.offset += newNodes.size
					parentNode.fullyLoaded = isFullyLoaded
				}
			}
		}
	}

	/**
	 * Used to preload in nested dirs
	 */
	fun preloadChildren(
		parentNode: FlatFileNode,
		onLoaded: (List<FlatFileNode>, Boolean) -> Unit
	) {
		var isFullyLoaded = false
		val newFiles = fileService.listFiles(
			parentNode.node.path,
			offset = parentNode.offset,
			limit = PageProperties.BUFFER_SIZE
		)
		if (newFiles.size < PageProperties.BUFFER_SIZE) {
			isFullyLoaded = true
		}
		if (newFiles.isEmpty()) {
			onLoaded(emptyList(), true)
			return
		}
		val newNodes = newFiles.map { FlatFileNode(it, parentNode,  depth = parentNode.depth + 1, offset = 0) }
		onLoaded(newNodes, isFullyLoaded)
	}

	private fun preloadForRoot(
		flatNodeList: SnapshotStateList<FlatFileNode>,
		rootForwardOffset: Int,
		parentPath: String,
		onLoaded: (Int) -> Unit
	) {
		val newFiles = fileService.listFiles(parentPath, rootForwardOffset, PageProperties.BUFFER_SIZE)
		flatNodeList.addAll(
			newFiles.map { FlatFileNode(it, null, depth = 1, offset = 0) }
		)
		LoggerProvider.logger.fine("Loaded ${newFiles.size} files, all files: ${flatNodeList.size}")
		onLoaded(newFiles.size)
	}

	private fun offsetInVisibleBuffer(offset: Int, firstVisibleIndex: Int) : Boolean {
		return offset < (firstVisibleIndex + PageProperties.ITEMS_PER_PAGE + PageProperties.BUFFER_SIZE)
	}

}

object PageProperties {
	const val ITEMS_PER_PAGE = 15
	const val BUFFER_SIZE = ITEMS_PER_PAGE * 2
}