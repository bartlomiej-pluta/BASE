package com.bartlomiejpluta.base.editor.render.input

import com.bartlomiejpluta.base.editor.tileset.model.TileSet
import javafx.scene.input.MouseEvent

class MapMouseEvent(val row: Int, val column: Int, val event: MouseEvent) {
   val type = event.eventType
   val button = event.button

   companion object {
      fun of(event: MouseEvent, tileSet: TileSet) = MapMouseEvent(
         (event.y / tileSet.tileHeight).toInt(),
         (event.x / tileSet.tileWidth).toInt(),
         event
      )
   }
}