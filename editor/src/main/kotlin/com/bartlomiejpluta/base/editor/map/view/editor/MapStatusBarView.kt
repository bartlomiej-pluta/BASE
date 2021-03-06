package com.bartlomiejpluta.base.editor.map.view.editor

import com.bartlomiejpluta.base.editor.map.viewmodel.EditorStateVM
import javafx.beans.binding.Bindings
import org.kordamp.ikonli.javafx.FontIcon
import tornadofx.*

class MapStatusBarView : View() {

   private val editorStateVM = find<EditorStateVM>()

   override val root = hbox {
      spacing = 50.0
      paddingAll = 5.0

      hbox {
         this += FontIcon("fa-search-minus")

         slider(0.5..5.0) {
            bind(editorStateVM.zoomProperty)
         }

         this += FontIcon("fa-search-plus")
      }

      label(
         Bindings.format(
            "Cursor: %d, %d",
            editorStateVM.cursorColumnProperty,
            editorStateVM.cursorRowProperty
         )
      )
   }
}