package com.bartlomiejpluta.base.editor.code.view.build

import com.bartlomiejpluta.base.editor.common.logs.component.LogsPane
import com.bartlomiejpluta.base.editor.event.AppendBuildLogsEvent
import com.bartlomiejpluta.base.editor.event.ClearBuildLogsEvent
import com.bartlomiejpluta.base.editor.main.controller.MainController
import com.bartlomiejpluta.base.editor.project.context.ProjectContext
import org.codehaus.commons.compiler.Location
import org.kordamp.ikonli.javafx.FontIcon
import tornadofx.*
import java.io.File

class BuildLogsView : View() {
   private val projectContext: ProjectContext by di()
   private val mainController: MainController by di()

   private val followCaret = true.toProperty()

   private val buildLogs = LogsPane(this::locationClick)

   init {
      subscribe<AppendBuildLogsEvent> { event ->
         buildLogs.appendEntry(event.message, event.severity, followCaret.value, event.location, event.tag)
      }

      subscribe<ClearBuildLogsEvent> {
         buildLogs.clear()
      }
   }

   private fun locationClick(location: Location) {
      projectContext.project?.codeFSNode?.findByFile(File(location.fileName))?.let {
         mainController.openScript(it, location.lineNumber, 1)
      }
   }

   override val root = borderpane {
      left = vbox {
         button(graphic = FontIcon("fa-trash")) {
            action { buildLogs.clear() }
         }

         togglebutton {
            followCaret.bind(selectedProperty())
            graphic = FontIcon("fa-angle-double-down")
         }
      }

      center = buildLogs
   }
}