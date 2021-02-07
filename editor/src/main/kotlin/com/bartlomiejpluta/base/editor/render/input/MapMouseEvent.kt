package com.bartlomiejpluta.base.editor.render.input

import com.bartlomiejpluta.base.editor.tileset.model.TileSet
import javafx.event.EventType
import javafx.scene.input.MouseButton
import javafx.scene.input.MouseEvent

class MapMouseEvent(val row: Int, val column: Int, val type: EventType<out MouseEvent>, val button: MouseButton) {

   companion object {
      fun of(event: MouseEvent, tileSet: TileSet) = MapMouseEvent(
         (event.y / tileSet.tileHeight).toInt(),
         (event.x / tileSet.tileWidth).toInt(),
         event.eventType,
         event.button
      )
   }
}