package com.bartlomiejpluta.base.editor.autotile.view.editor

import com.bartlomiejpluta.base.editor.map.viewmodel.BrushVM
import com.bartlomiejpluta.base.editor.map.viewmodel.EditorStateVM
import com.bartlomiejpluta.base.editor.map.viewmodel.GameMapVM
import com.bartlomiejpluta.base.editor.autotile.component.AutoTilePane
import tornadofx.View
import tornadofx.plusAssign
import tornadofx.scrollpane

class AutoTileView : View() {
   private val gameMapVM = find<GameMapVM>()
   private val brushVM = find<BrushVM>()
   private val editorStateVM = find<EditorStateVM>()

   private val autoTilePane = AutoTilePane(editorStateVM, gameMapVM, brushVM)

   override val root = scrollpane {
      this += autoTilePane
   }
}