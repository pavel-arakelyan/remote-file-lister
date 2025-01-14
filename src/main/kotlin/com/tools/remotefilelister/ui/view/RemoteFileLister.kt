package com.tools.remotefilelister.ui.view

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.tools.remotefilelister.rpc.FileNode
import com.tools.remotefilelister.rpc.impl.LocalFileService

/**
 * @author pavel.arakelyan
 */
@Composable
fun RemoteFileLister() {
	val fileService = remember { LocalFileService() }
	var selectedFiles by remember { mutableStateOf<List<FileNode>>(emptyList()) }
	var statusMessage by remember { mutableStateOf("Select a file to see details.") }
	var showPopup by remember { mutableStateOf(false) }

	Box(
		modifier = Modifier.fillMaxSize()
	) {
		Column(modifier = Modifier.fillMaxSize()) {
			// File Tree Section
			FileTreeSection(
				fileService = fileService,
				onFileSelected = { files ->
					selectedFiles = files
					statusMessage = when {
						files.isEmpty() -> "No file selected."
						files.size == 1 -> "${files.first().path} - ${files.first().size} bytes"
						else -> "${files.size} files selected"
					}
					if (files.size > 1 || files.isEmpty()) {
						showPopup = false
					}
				},
				modifier = Modifier.weight(1f)
			)

			// Status Bar
			StatusBar(
				statusMessage = statusMessage,
				selectedFiles = selectedFiles,
				onInfoClick = { showPopup = true },
				modifier = Modifier.wrapContentHeight()
			)
		}

		// Popup for file attributes
		if (showPopup && selectedFiles.size == 1) {
			val fileAttributes = fileService.getFileAttributes(selectedFiles.first().path)
			FileAttributesPopup(
				fileAttributes = fileAttributes,
				onClose = { showPopup = false }
			)
		}
	}
}




