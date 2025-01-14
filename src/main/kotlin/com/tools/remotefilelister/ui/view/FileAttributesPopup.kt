package com.tools.remotefilelister.ui.view

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.tools.remotefilelister.rpc.FileAttributes
import org.jetbrains.jewel.ui.component.Text

/**
 * @author pavel.arakelyan
 */
@Composable
fun FileAttributesPopup(
	fileAttributes: FileAttributes,
	onClose: () -> Unit
) {

	Box(
		modifier = Modifier
			.padding(16.dp)
			.background(Color(0xFFEEEEEE), shape = RoundedCornerShape(12.dp)) // Lighter gray background
			.border(1.dp, Color(0xFFCCCCCC), RoundedCornerShape(12.dp)) // Light border
			.sizeIn(minWidth = 200.dp, minHeight = 150.dp)
	) {
			Column(
				modifier = Modifier
					.fillMaxWidth()
					.padding(16.dp),
				verticalArrangement = Arrangement.spacedBy(8.dp),
				horizontalAlignment = Alignment.Start
			) {
				Text("Info", modifier = Modifier.padding(bottom = 8.dp))
				Text("Path: ${fileAttributes.path}")
				Text("Last accessed time: ${fileAttributes.lastAccessTime}")
				Text("Last modified time: ${fileAttributes.lastModifiedTime}")
				Text("Creation time: ${fileAttributes.creationTime}")
				Text("File type: ${fileAttributes.fileType}")

				Spacer(modifier = Modifier.height(16.dp))

				Row(
					horizontalArrangement = Arrangement.End,
					modifier = Modifier.fillMaxWidth()
				) {
					Box(
						modifier = Modifier
							.background(Color.Gray)
							.padding(horizontal = 16.dp, vertical = 8.dp)
							.clickable { onClose() }
					) {
						Text("Close", color = Color.White)
					}
				}
			}
		}
}

