package com.tools.remotefilelister

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import com.tools.remotefilelister.ui.view.RemoteFileLister
import org.jetbrains.jewel.foundation.theme.JewelTheme
import org.jetbrains.jewel.intui.standalone.theme.*
import org.jetbrains.jewel.ui.ComponentStyling
import java.util.logging.ConsoleHandler
import java.util.logging.Level
import java.util.logging.Logger
import java.util.logging.SimpleFormatter

fun main() {
	LoggerProvider.logger.info("Starting...")
	application {
		val textStyle = JewelTheme.createDefaultTextStyle()
		val editorStyle = JewelTheme.createEditorTextStyle()

		val themeDefinition = JewelTheme.lightThemeDefinition(defaultTextStyle = textStyle, editorTextStyle = editorStyle)

		IntUiTheme(
			theme = themeDefinition,
			styling = ComponentStyling.default(),
			swingCompatMode = true,
		) {
			Window(
				onCloseRequest = { exitApplication() },
				title = "Remote File Lister",
				state = rememberWindowState(size = DpSize(800.dp, 550.dp)),
				resizable = false
			) {
				IntUiTheme { // Apply the JetBrains Jewel Theme
					Box(
						modifier = Modifier
							.fillMaxSize()
							.padding(16.dp)
					) {
						RemoteFileLister()
					}
				}
			}
		}
	}
}

object LoggerProvider {
	val logger: Logger = Logger.getLogger("RemoteFileLister").apply {
		useParentHandlers = false // Disable parent handlers to avoid duplicate logs

		// Configure a ConsoleHandler
		val consoleHandler = ConsoleHandler().apply {
			level = Level.ALL // Handle all levels; actual filtering is done by the logger
			formatter = SimpleFormatter()
		}

		if (handlers.isEmpty()) {
			addHandler(consoleHandler)
		}

		level = Level.INFO // Default to INFO
	}
}