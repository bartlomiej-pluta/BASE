package com.bartlomiejpluta.base.editor.map.view.wizard

import tornadofx.View
import tornadofx.hbox
import tornadofx.listview

class MapTileSetSelectionView : View("Tile Set") {

   // TODO(Implement tileset selection)
   override val root = hbox {
      listview<String> {
         items.add("TileSet 1")
         items.add("TileSet 2")
         items.add("TileSet 3")
         items.add("TileSet 4")
         items.add("TileSet 5")
         items.add("TileSet 6")
         items.add("TileSet 7")
         items.add("TileSet 8")
         items.add("TileSet 9")
         items.add("TileSet 10")
         items.add("TileSet 11")
      }
   }
}