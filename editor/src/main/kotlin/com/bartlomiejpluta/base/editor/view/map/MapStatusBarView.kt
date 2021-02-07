package com.bartlomiejpluta.base.editor.view.map

import com.bartlomiejpluta.base.editor.viewmodel.map.EditorStateVM
import javafx.beans.binding.Bindings
import org.kordamp.ikonli.javafx.FontIcon
import tornadofx.*

class MapStatusBarView : View() {

   private val editorOptionsVM = find<EditorStateVM>()

   override val root = hbox {
      spacing = 1.0
      paddingAll = 5.0

      this += FontIcon("fa-search-minus")

      slider(0.5..5.0) {
         bind(editorOptionsVM.zoomProperty)
      }

      this += FontIcon("fa-search-plus")
   }
}