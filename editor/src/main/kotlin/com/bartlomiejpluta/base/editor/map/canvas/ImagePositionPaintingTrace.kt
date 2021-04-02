package com.bartlomiejpluta.base.editor.map.canvas

import com.bartlomiejpluta.base.editor.map.model.layer.ImageLayer
import com.bartlomiejpluta.base.editor.map.viewmodel.BrushVM
import com.bartlomiejpluta.base.editor.map.viewmodel.EditorStateVM
import com.bartlomiejpluta.base.editor.map.viewmodel.GameMapVM
import com.bartlomiejpluta.base.editor.render.input.MapMouseEvent
import javafx.scene.input.MouseButton

class ImagePositionPaintingTrace(val map: GameMapVM, override val commandName: String) : PaintingTrace {
   private var layerIndex = 0
   private var originX = 0.0
   private var originY = 0.0
   private var x = 0.0
   private var y = 0.0
   private var dx = 0.0
   private var dy = 0.0
   private var newX = 0.0
   private var newY = 0.0
   private lateinit var layer: ImageLayer

   override var executed = false
      private set

   override fun beginTrace(editorStateVM: EditorStateVM, brushVM: BrushVM, mouseEvent: MapMouseEvent) {
      this.layerIndex = editorStateVM.selectedLayerIndex

      if (layerIndex < 0) {
         return
      }

      this.layer = (map.layers[layerIndex] as ImageLayer)

      originX = layer.x.toDouble()
      originY = layer.y.toDouble()

      x = mouseEvent.event.x
      y = mouseEvent.event.y
   }

   override fun proceedTrace(editorStateVM: EditorStateVM, brushVM: BrushVM, mouseEvent: MapMouseEvent) {
      dx = mouseEvent.event.x - x
      dy = mouseEvent.event.y - y

      newX = originX + dx
      newY = originY + dy

      layer.x = newX.toInt()
      layer.y = newY.toInt()

      if (dx != 0.0 || dy != 0.0) {
         executed = true
      }
   }

   override fun commitTrace(editorStateVM: EditorStateVM, brushVM: BrushVM, mouseEvent: MapMouseEvent) {
      dx = mouseEvent.event.x - x
      dy = mouseEvent.event.y - y

      newX = originX + dx
      newY = originY + dy

      layer.x = newX.toInt()
      layer.y = newY.toInt()

      if (dx != 0.0 || dy != 0.0) {
         executed = true
      }
   }

   override fun undo() {
      layer.x = originX.toInt()
      layer.y = originY.toInt()
   }

   override fun redo() {
      layer.x = newX.toInt()
      layer.y = newY.toInt()
   }

   override val supportedButtons = arrayOf(MouseButton.PRIMARY)
}
