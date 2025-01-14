package com.tools.remotefilelister.rpc

/**
 * @author pavel.arakelyan
 */
interface FileService {

	fun listFiles(directoryPath: String, offset: Int, limit: Int): List<FileNode>

	fun getFileAttributes(filePath: String): FileAttributes
}