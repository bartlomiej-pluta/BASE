package com.bartlomiejpluta.base.editor.tileset.component

import com.bartlomiejpluta.base.editor.render.input.MapMouseEvent
import com.bartlomiejpluta.base.editor.tileset.canvas.TileSetCanvas
import com.bartlomiejpluta.base.editor.tileset.canvas.TileSetSelection
import com.bartlomiejpluta.base.editor.map.viewmodel.BrushVM
import com.bartlomiejpluta.base.editor.map.viewmodel.GameMapVM
import javafx.event.EventHandler
import javafx.scene.canvas.Canvas
import javafx.scene.input.MouseEvent

class TileSetPane(private val gameMapVM: GameMapVM, brushVM: BrushVM) : Canvas(), EventHandler<MouseEvent> {
   private val selection = TileSetSelection(gameMapVM, brushVM)
   private val tileSetCanvas = TileSetCanvas(gameMapVM, selection)

   init {
      onMouseMoved = this
      onMouseDragged = this
      onMousePressed = this
      onMouseReleased = this

      width = gameMapVM.tileSet.width.toDouble()
      height = gameMapVM.tileSet.height.toDouble()


      brushVM.itemProperty.addListener { _, _, _ -> render() }

      render()
   }

   private fun render() {
      tileSetCanvas.render(graphicsContext2D)
   }

   override fun handle(event: MouseEvent?) {
      if (event != null) {
         tileSetCanvas.handleMouseInput(MapMouseEvent.of(event, gameMapVM.tileSet))
      }

      tileSetCanvas.render(graphicsContext2D)
   }
}