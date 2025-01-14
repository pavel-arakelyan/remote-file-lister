package com.tools.remotefilelister.ui.view.tree

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.tools.remotefilelister.rpc.FileNode

/**
 * @author pavel.arakelyan
 */
data class FlatFileNode(
	val node: FileNode,
	val parentNode: FlatFileNode?,
	val depth: Int,
	var isExpanded: Boolean = false,
	var fullyLoaded: Boolean = false,
	var offset: Int = 0, // used to track offset for nested dir
	val isSelected: MutableState<Boolean> = mutableStateOf(false)
) {
	fun parentPath(): String {
		return node.path.substringBeforeLast("/")
	}

	fun name(): String {
		return this.node.name
	}

	fun path(): String {
		return this.node.path
	}

	fun isDirectory(): Boolean {
		return this.node.isDirectory
	}
}
