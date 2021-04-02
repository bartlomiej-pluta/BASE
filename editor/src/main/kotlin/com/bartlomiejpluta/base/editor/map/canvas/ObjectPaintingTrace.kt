package com.bartlomiejpluta.base.editor.map.canvas

import com.bartlomiejpluta.base.editor.map.model.brush.BrushMode
import com.bartlomiejpluta.base.editor.map.model.layer.ObjectLayer
import com.bartlomiejpluta.base.editor.map.model.obj.MapObject
import com.bartlomiejpluta.base.editor.map.viewmodel.BrushVM
import com.bartlomiejpluta.base.editor.map.viewmodel.EditorStateVM
import com.bartlomiejpluta.base.editor.map.viewmodel.GameMapVM
import com.bartlomiejpluta.base.editor.render.input.MapMouseEvent
import javafx.collections.ObservableList
import javafx.scene.input.MouseButton

class ObjectPaintingTrace(val map: GameMapVM, override val commandName: String) : PaintingTrace {
   private lateinit var objects: ObservableList<MapObject>
   private lateinit var event: MapMouseEvent

   private var newObject: MapObject? = null
   private var formerObject: MapObject? = null
   private var x = 0
   private var y = 0

   override var executed = false
      private set

   override fun beginTrace(editorStateVM: EditorStateVM, brushVM: BrushVM, mouseEvent: MapMouseEvent) {
      x = editorStateVM.cursorColumn
      y = editorStateVM.cursorRow

      if (y >= map.rows || x >= map.columns || y < 0 || x < 0 || editorStateVM.selectedLayerIndex < 0) {
         return
      }

      objects = (editorStateVM.selectedLayer as ObjectLayer).objects
      formerObject = objects.firstOrNull { it.x == x && it.y == y }

      event = mouseEvent
   }

   private fun createOrUpdateObject() {
      newObject = MapObject(x, y, "")
      objects.remove(formerObject)
      objects.add(newObject)
      executed = true
   }

   private fun moveObject(newX: Int, newY: Int) {
      if (newY >= map.rows || newX >= map.columns || newY < 0 || newX < 0) {
         return
      }

      formerObject?.let {
         newObject = MapObject(newX, newY, it.code)
         objects.remove(formerObject)
         objects.add(newObject)
         executed = true
      }
   }

   private fun removeObject() {
      objects.remove(formerObject)
      executed = true
   }

   override fun proceedTrace(editorStateVM: EditorStateVM, brushVM: BrushVM, mouseEvent: MapMouseEvent) {
   }

   override fun commitTrace(editorStateVM: EditorStateVM, brushVM: BrushVM, mouseEvent: MapMouseEvent) {
      val newX = editorStateVM.cursorColumn
      val newY = editorStateVM.cursorRow
      val dx = newX - x
      val dy = newY - y

      // Moving
      if (brushVM.mode == BrushMode.PAINTING_MODE && (dx != 0 || dy != 0)) {
         moveObject(newX, newY)
         return
      }

      // Creating new or updating existing one or removing
      if (event.event.clickCount > 1 && brushVM.mode == BrushMode.PAINTING_MODE) {
         when (event.button) {
            MouseButton.PRIMARY -> createOrUpdateObject()
            MouseButton.SECONDARY -> removeObject()
            else -> {
            }
         }

         return
      }

      // Removing
      if (brushVM.mode == BrushMode.ERASING_MODE) {
         removeObject()
      }
   }

   override fun undo() {
      objects.remove(newObject)
      formerObject?.let(objects::add)
   }

   override fun redo() {
      objects.remove(formerObject)
      newObject?.let(objects::add)
   }

   override val supportedButtons = arrayOf(MouseButton.PRIMARY, MouseButton.SECONDARY)
}