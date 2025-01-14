package com.tools.remotefilelister.rpc.impl

import com.tools.remotefilelister.LoggerProvider
import com.tools.remotefilelister.rpc.FileAttributes
import com.tools.remotefilelister.rpc.FileNode
import com.tools.remotefilelister.rpc.FileService
import com.tools.remotefilelister.rpc.FileType
import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.attribute.BasicFileAttributes

class LocalFileService : FileService {

    override fun listFiles(directoryPath: String, offset: Int, limit: Int): List<FileNode> {
        LoggerProvider.logger.fine("Called with lazy load: offset=$offset, limit=$limit")
        val directory = Paths.get(directoryPath)

        if (!Files.exists(directory) || !Files.isDirectory(directory)) {
            return emptyList()
        }

        // Lazy load files using offset and limit
        return Files.list(directory)
            .skip(offset.toLong())
            .limit(limit.toLong())
            .map { path ->
                FileNode(
                    name = path.fileName.toString(),
                    path = path.toAbsolutePath().toString(),
                    isDirectory = Files.isDirectory(path),
                    size = if (Files.isRegularFile(path)) Files.size(path) else 0L
                )
            }
            .toList()
    }

    override fun getFileAttributes(filePath: String): FileAttributes {
        val path = Paths.get(filePath)
        if (!Files.exists(path)) {
            throw IllegalArgumentException("File does not exist: $filePath")
        }

        val attributes = Files.readAttributes(path, BasicFileAttributes::class.java)
        return FileAttributes(
            path = path.toAbsolutePath().toString(),
            lastModifiedTime = attributes.lastModifiedTime().toMillis(),
            lastAccessTime = attributes.lastAccessTime().toMillis(),
            creationTime = attributes.creationTime().toMillis(),
            fileType = resolveFileType(attributes)
        )
    }

    private fun resolveFileType(attributes: BasicFileAttributes): FileType {
        return when {
            attributes.isDirectory -> FileType.DIR
            attributes.isRegularFile -> FileType.FILE
            attributes.isSymbolicLink -> FileType.SYMLINK
            else -> FileType.UNKNOWN
        }
    }
}
