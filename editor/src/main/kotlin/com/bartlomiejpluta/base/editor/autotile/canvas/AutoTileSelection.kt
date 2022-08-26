package com.bartlomiejpluta.base.editor.autotile.canvas

import com.bartlomiejpluta.base.editor.map.model.brush.AutoTileBrush
import com.bartlomiejpluta.base.editor.map.model.layer.AutoTileLayer
import com.bartlomiejpluta.base.editor.map.model.layer.TileLayer
import com.bartlomiejpluta.base.editor.map.viewmodel.BrushVM
import com.bartlomiejpluta.base.editor.map.viewmodel.EditorStateVM
import com.bartlomiejpluta.base.editor.map.viewmodel.GameMapVM
import com.bartlomiejpluta.base.editor.render.model.Renderable
import javafx.scene.canvas.GraphicsContext
import javafx.scene.paint.Color

class AutoTileSelection(private val editorStateVM: EditorStateVM, private val gameMapVM: GameMapVM, private val brushVM: BrushVM) :
   Renderable {
   private var x = 0.0
   private var y = 0.0
   private var width = gameMapVM.tileWidth.toDouble()
   private var height = gameMapVM.tileHeight.toDouble()


   fun select(row: Double, column: Double) {
      x = column * width
      y = row * height

      if (editorStateVM.selectedLayer is AutoTileLayer) {
         val autoTile = (editorStateVM.selectedLayer as AutoTileLayer).autoTile
         (brushVM.item as AutoTileBrush).id = (1 + row * autoTile.columns + column).toInt() //((column % autoTile.columns).toInt()) + row.toInt() + 1
      }
   }

   override fun render(gc: GraphicsContext) {
      gc.fill = SELECTION_FILL_COLOR
      gc.fillRect(x, y, width, height)

      gc.stroke = SELECTION_STROKE_COLOR
      gc.strokeRect(x, y, width, height)
   }

   companion object {
      private val SELECTION_FILL_COLOR = Color.color(0.0, 0.7, 1.0, 0.4)
      private val SELECTION_STROKE_COLOR = Color.color(0.0, 0.7, 1.0, 1.0)
   }
}