package com.tools.remotefilelister.rpc

/**
 * @author pavel.arakelyan
 */
data class FileNode(
	val name: String,
	val path: String,
	val isDirectory: Boolean,
	val size: Long
)
