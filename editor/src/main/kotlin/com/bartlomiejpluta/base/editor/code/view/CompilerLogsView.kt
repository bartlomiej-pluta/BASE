package com.bartlomiejpluta.base.editor.code.view

import com.bartlomiejpluta.base.editor.event.UpdateCompilationLogEvent
import com.bartlomiejpluta.base.editor.main.controller.MainController
import com.bartlomiejpluta.base.editor.project.context.ProjectContext
import javafx.scene.Cursor
import javafx.scene.paint.Color
import javafx.scene.text.Text
import org.codehaus.commons.compiler.Location
import org.fxmisc.richtext.StyledTextArea
import org.kordamp.ikonli.javafx.FontIcon
import tornadofx.*
import java.io.File


class CompilerLogsView : View() {
   private val projectContext: ProjectContext by di()
   private val mainController: MainController by di()

   private val editor = StyledTextArea(
      null,
      { _, _ -> },
      LocationRef.NO_LINK,
      { text, style -> style.apply(text) }
   ).apply { isEditable = false }

   init {
      subscribe<UpdateCompilationLogEvent> { event ->
         editor.clear()

         val locationRef = LocationRef(event.location) { loc ->
            projectContext.project?.codeFSNode?.findByFile(File(loc.fileName))?.let {
               mainController.openScript(it, loc.lineNumber, 1)
            }
         }

         editor.insert(editor.length, event.location?.toString() ?: "", locationRef)
         editor.insert(editor.length, event.message, LocationRef.NO_LINK)
      }
   }

   override val root = borderpane {
      left = hbox {
         button(graphic = FontIcon("fa-trash")) {
            action { editor.clear() }
         }
      }

      center = editor
   }

   class LocationRef(private val location: Location?, private val onClick: (Location) -> Unit = {}) {
      private constructor() : this(null)

      fun apply(text: Text) = location?.let { loc ->
         text.cursor = Cursor.HAND
         text.fill = Color.BLUE
         text.isUnderline = true
         text.setOnMouseClicked {
            onClick(loc)
         }
      }

      companion object {
         val NO_LINK = LocationRef()
      }
   }
}