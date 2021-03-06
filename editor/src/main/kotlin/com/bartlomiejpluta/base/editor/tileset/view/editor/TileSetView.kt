package com.bartlomiejpluta.base.editor.tileset.view.editor

import com.bartlomiejpluta.base.editor.map.viewmodel.BrushVM
import com.bartlomiejpluta.base.editor.map.viewmodel.GameMapVM
import com.bartlomiejpluta.base.editor.tileset.component.TileSetPane
import tornadofx.View
import tornadofx.plusAssign
import tornadofx.scrollpane

class TileSetView : View() {
   private val gameMapVM = find<GameMapVM>()
   private val brushVM = find<BrushVM>()

   private val tileSetPane = TileSetPane(gameMapVM, brushVM)

   override val root = scrollpane {
      this += tileSetPane
   }
}