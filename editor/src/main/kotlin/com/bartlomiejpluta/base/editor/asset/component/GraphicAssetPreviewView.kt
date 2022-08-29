package com.bartlomiejpluta.base.editor.asset.component

import com.bartlomiejpluta.base.editor.asset.viewmodel.GraphicAssetVM
import javafx.beans.binding.Bindings
import javafx.beans.property.SimpleIntegerProperty
import javafx.scene.image.Image
import javafx.scene.input.MouseEvent
import tornadofx.*
import kotlin.math.max

class GraphicAssetPreviewView : View() {
   private val vm = find<GraphicAssetVM>()
   private val image = Image(vm.fileProperty.value.inputStream())
   private val mouseColumn = SimpleIntegerProperty(0)
   private val mouseRow = SimpleIntegerProperty(0)

   private val canvas = GraphicAssetViewCanvas(vm).apply {
      addEventHandler(MouseEvent.ANY) {
         mouseColumn.value = max(0, (it.x / this.chunkWidth).toInt() - 1)
         mouseRow.value = max(0, (it.y / this.chunkHeight).toInt() - 1)
      }
   }

   override val root = borderpane {
      center = scrollpane {
         this += canvas
      }

      bottom = label(
         if (canvas.singleChunk) Bindings.format(
            "Width: %d, Height: %d",
            image.width.toInt(),
            image.height.toInt()
         )
         else Bindings.format(
            "Rows: %d, Columns: %d, Chunk width: %d, Chunk height: %d, Cursor: %d, %d",
            canvas.rows,
            canvas.columns,
            canvas.chunkWidth,
            canvas.chunkHeight,
            mouseRow,
            mouseColumn
         )
      )
   }
}