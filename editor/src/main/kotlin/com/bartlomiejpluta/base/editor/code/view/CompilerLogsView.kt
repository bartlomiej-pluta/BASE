package com.bartlomiejpluta.base.editor.code.view

import com.bartlomiejpluta.base.editor.code.component.CompilationLogs
import com.bartlomiejpluta.base.editor.event.UpdateCompilationLogEvent
import com.bartlomiejpluta.base.editor.main.controller.MainController
import com.bartlomiejpluta.base.editor.project.context.ProjectContext
import org.codehaus.commons.compiler.Location
import org.kordamp.ikonli.javafx.FontIcon
import tornadofx.*
import java.io.File


class CompilerLogsView : View() {
   private val projectContext: ProjectContext by di()
   private val mainController: MainController by di()

   private val compilationLogs = CompilationLogs(this::locationClick)

   init {
      subscribe<UpdateCompilationLogEvent> { event ->
         compilationLogs.setEntry(event.message, event.severity, event.location)
      }
   }

   private fun locationClick(location: Location) {
      projectContext.project?.codeFSNode?.findByFile(File(location.fileName))?.let {
         mainController.openScript(it, location.lineNumber, 1)
      }
   }

   override val root = borderpane {
      left = hbox {
         button(graphic = FontIcon("fa-trash")) {
            action { compilationLogs.clear() }
         }
      }

      center = compilationLogs
   }
}