package com.bartlomiejpluta.base.editor.map.canvas

import com.bartlomiejpluta.base.editor.map.model.brush.BrushMode
import com.bartlomiejpluta.base.editor.map.model.enumeration.PassageAbility
import com.bartlomiejpluta.base.editor.map.model.layer.ObjectLayer
import com.bartlomiejpluta.base.editor.map.viewmodel.BrushVM
import com.bartlomiejpluta.base.editor.map.viewmodel.EditorStateVM
import com.bartlomiejpluta.base.editor.map.viewmodel.GameMapVM
import com.bartlomiejpluta.base.editor.render.input.MapMouseEvent

class ObjectPaintingTrace(val map: GameMapVM, override val commandName: String) : PaintingTrace {
   private var layerIndex = 0
   private var row = 0
   private var column = 0
   private lateinit var layer: ObjectLayer
   private lateinit var formerPassageAbility: PassageAbility
   private lateinit var passageAbility: PassageAbility


   override fun beginTrace(editorStateVM: EditorStateVM, brushVM: BrushVM, mouseEvent: MapMouseEvent) {
      // Do nothing
   }

   override fun proceedTrace(editorStateVM: EditorStateVM, brushVM: BrushVM, mouseEvent: MapMouseEvent) {
      // Do nothing
   }

   override fun commitTrace(editorStateVM: EditorStateVM, brushVM: BrushVM, mouseEvent: MapMouseEvent) {
      this.layerIndex = editorStateVM.selectedLayerIndex
      this.row = editorStateVM.cursorRow
      this.column = editorStateVM.cursorColumn

      if (row >= map.rows || column >= map.columns || row < 0 || column < 0 || layerIndex < 0) {
         return
      }

      this.layer = (map.layers[layerIndex] as ObjectLayer)

      formerPassageAbility = layer.passageMap[row][column]

      passageAbility = when (brushVM.mode!!) {
         BrushMode.PAINTING_MODE -> PassageAbility.values()[(formerPassageAbility.ordinal + 1) % PassageAbility.values().size]
         BrushMode.ERASING_MODE -> PassageAbility.ALLOW
      }

      layer.passageMap[row][column] = passageAbility
   }

   override fun undo() {
      layer.passageMap[row][column] = formerPassageAbility
   }

   override fun redo() {
      layer.passageMap[row][column] = passageAbility
   }
}