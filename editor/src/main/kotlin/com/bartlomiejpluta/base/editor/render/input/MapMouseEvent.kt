package com.bartlomiejpluta.base.editor.render.input

import com.bartlomiejpluta.base.editor.map.viewmodel.GameMapVM
import javafx.scene.input.MouseEvent

class MapMouseEvent(val row: Int, val column: Int, val event: MouseEvent) {
   val type = event.eventType
   val button = event.button

   companion object {
      fun of(event: MouseEvent, mapVM: GameMapVM) = MapMouseEvent(
         (event.y / mapVM.tileHeight).toInt(),
         (event.x / mapVM.tileWidth).toInt(),
         event
      )
   }
}