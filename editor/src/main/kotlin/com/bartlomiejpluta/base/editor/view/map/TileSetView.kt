package com.bartlomiejpluta.base.editor.view.map

import com.bartlomiejpluta.base.editor.view.component.tileset.TileSetPane
import com.bartlomiejpluta.base.editor.viewmodel.map.BrushVM
import com.bartlomiejpluta.base.editor.viewmodel.map.GameMapVM
import com.bartlomiejpluta.base.editor.viewmodel.map.TileSetVM
import tornadofx.View
import tornadofx.plusAssign
import tornadofx.scrollpane

class TileSetView : View() {
   private val gameMapVM = find<GameMapVM>()
   private val tileSetVM = find<TileSetVM>()
   private val brushVM = find<BrushVM>()

   private val tileSetPane = TileSetPane(tileSetVM, brushVM)

   init {
      tileSetVM.itemProperty.bind(gameMapVM.tileSetProperty)
   }

   override val root = scrollpane {
      this += tileSetPane
   }
}