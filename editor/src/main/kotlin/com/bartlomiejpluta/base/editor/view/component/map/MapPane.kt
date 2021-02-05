package com.bartlomiejpluta.base.editor.view.component.map

import com.bartlomiejpluta.base.editor.render.canvas.input.MapMouseEvent
import com.bartlomiejpluta.base.editor.render.canvas.map.MapCanvas
import com.bartlomiejpluta.base.editor.render.canvas.map.MapPainter
import com.bartlomiejpluta.base.editor.render.canvas.map.MapPaintingTrace
import com.bartlomiejpluta.base.editor.viewmodel.map.BrushVM
import com.bartlomiejpluta.base.editor.viewmodel.map.GameMapVM
import javafx.event.EventHandler
import javafx.scene.canvas.Canvas
import javafx.scene.input.MouseEvent


class MapPane(private val mapVM: GameMapVM,
   brushVM: BrushVM,
   paintingCallback: (MapPaintingTrace) -> Unit
) : Canvas(), EventHandler<MouseEvent> {
   private val painter = MapPainter(mapVM, brushVM, paintingCallback)
   private val mapCanvas = MapCanvas(mapVM, painter)

   init {
      onMouseMoved = this
      onMouseDragged = this
      onMousePressed = this
      onMouseReleased = this

      width = mapVM.width.toDouble()
      height = mapVM.height.toDouble()

      render()
   }

   fun render() {
      mapCanvas.render(graphicsContext2D)
   }

   override fun handle(event: MouseEvent?) {
      if (event != null) {
         painter.handleMouseInput(MapMouseEvent.of(event, mapVM.tileSet))
      }

      mapCanvas.render(graphicsContext2D)
   }
}