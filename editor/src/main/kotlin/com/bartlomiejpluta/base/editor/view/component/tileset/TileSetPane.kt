package com.bartlomiejpluta.base.editor.view.component.tileset

import com.bartlomiejpluta.base.editor.render.canvas.input.MapMouseEvent
import com.bartlomiejpluta.base.editor.render.canvas.tileset.TileSetCanvas
import com.bartlomiejpluta.base.editor.viewmodel.map.BrushVM
import com.bartlomiejpluta.base.editor.viewmodel.map.TileSetVM
import javafx.event.EventHandler
import javafx.scene.canvas.Canvas
import javafx.scene.input.MouseEvent

class TileSetPane(private val tileSetVM: TileSetVM, brushVM: BrushVM) : Canvas(), EventHandler<MouseEvent> {
   private val tileSetCanvas = TileSetCanvas(tileSetVM, brushVM)

   init {
      onMouseMoved = this
      onMouseDragged = this
      onMousePressed = this
      onMouseReleased = this

      widthProperty().bind(tileSetVM.widthProperty)
      heightProperty().bind(tileSetVM.heightProperty)

      tileSetVM.itemProperty.addListener { _, _, _ -> render() }
   }

   private fun render() {
      tileSetCanvas.render(graphicsContext2D)
   }

   override fun handle(event: MouseEvent?) {
      if (event != null) {
         tileSetCanvas.handleMouseInput(MapMouseEvent.of(event, tileSetVM))
      }

      tileSetCanvas.render(graphicsContext2D)
   }
}