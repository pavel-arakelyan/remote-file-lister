package com.tools.remotefilelister.rpc

/**
 * @author pavel.arakelyan
 */
data class FileAttributes(
	val path: String,
	val lastModifiedTime: Long,
	val lastAccessTime: Long,
	val creationTime: Long,
	val fileType: FileType
)