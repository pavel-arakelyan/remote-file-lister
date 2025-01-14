package com.tools.remotefilelister.ui.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tools.remotefilelister.rpc.FileNode
import org.jetbrains.jewel.ui.component.Text

/**
 * @author pavel.arakelyan
 */
@Composable
fun StatusBar(
	statusMessage: String,
	selectedFiles: List<FileNode>,
	onInfoClick: () -> Unit,
	modifier: Modifier = Modifier
) {
	Row(
		modifier = modifier
			.fillMaxWidth()
			.padding(horizontal = 16.dp, vertical = 8.dp)
			.background(Color(0xFFEEEEEE))
			.padding(horizontal = 16.dp, vertical = 8.dp),
		horizontalArrangement = Arrangement.SpaceBetween,
		verticalAlignment = Alignment.CenterVertically
	) {
		Text(
			text = statusMessage,
			color = Color.DarkGray,
			fontSize = 14.sp,
			modifier = Modifier.weight(1f)
		)

		if (selectedFiles.size == 1) {
			Box(
				modifier = Modifier
					.background(Color.Gray)
					.padding(horizontal = 8.dp, vertical = 4.dp)
					.clickable { onInfoClick() }
			) {
				Text(text = "Info", color = Color.White)
			}
		}
	}
}