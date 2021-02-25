package com.bartlomiejpluta.base.editor.code.view

import com.bartlomiejpluta.base.editor.event.UpdateCompilationLogEvent
import com.bartlomiejpluta.base.editor.main.controller.MainController
import javafx.scene.Cursor
import javafx.scene.paint.Color
import javafx.scene.text.Text
import org.codehaus.commons.compiler.Location
import org.fxmisc.richtext.StyledTextArea
import tornadofx.View


class CompilerLogsView : View() {
   private val mainController: MainController by di()
   private val editor = StyledTextArea(null, { _, _ -> }, LocationRef.NO_LINK, { text, style -> style.apply(text) })

   init {
      subscribe<UpdateCompilationLogEvent> {
         editor.clear()

         val locationRef = LocationRef(it.location) { loc ->
            // TODO(mainController.openScript(getFileSystemNodeFromSomewhere(loc)))
         }

         editor.insert(editor.length, it.location.toString(), locationRef)
         editor.insert(editor.length, it.message, LocationRef.NO_LINK)
      }
   }

   override val root = editor

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