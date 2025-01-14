package com.tools.remotefilelister.ui.view

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.tools.remotefilelister.rpc.FileNode
import com.tools.remotefilelister.rpc.impl.LocalFileService
import com.tools.remotefilelister.ui.view.tree.FileTree
import java.nio.file.Paths

/**
 * @author pavel.arakelyan
 */
@Composable
fun FileTreeSection(
	fileService: LocalFileService,
	onFileSelected: (List<FileNode>) -> Unit,
	modifier: Modifier = Modifier
) {
	val rootDir = System.getProperty("dir") ?: error("Specify root directory via 'dir' system property")
	val rootPath = Paths.get(System.getProperty("user.home"), rootDir).toString()
	Box(
		modifier = modifier
			.padding(horizontal = 8.dp, vertical = 8.dp)
			.background(Color(0xFFF5F5F5), shape = RoundedCornerShape(8.dp))
			.border(1.dp, Color(0xFFCCCCCC), RoundedCornerShape(8.dp))
			.padding(8.dp)
	) {
		FileTree(
			fileService = fileService,
			rootPath = rootPath,
			onFileSelected = onFileSelected
		)
	}
}