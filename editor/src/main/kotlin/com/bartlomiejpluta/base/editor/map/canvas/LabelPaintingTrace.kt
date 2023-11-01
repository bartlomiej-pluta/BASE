package com.bartlomiejpluta.base.editor.map.canvas

import com.bartlomiejpluta.base.editor.common.view.StringInputFragment
import com.bartlomiejpluta.base.editor.common.viewmodel.StringVM
import com.bartlomiejpluta.base.editor.map.model.brush.BrushMode
import com.bartlomiejpluta.base.editor.map.model.layer.ObjectLayer
import com.bartlomiejpluta.base.editor.map.model.obj.MapLabel
import com.bartlomiejpluta.base.editor.map.viewmodel.BrushVM
import com.bartlomiejpluta.base.editor.map.viewmodel.EditorStateVM
import com.bartlomiejpluta.base.editor.map.viewmodel.GameMapVM
import com.bartlomiejpluta.base.editor.project.context.ProjectContext
import com.bartlomiejpluta.base.editor.render.input.MapMouseEvent
import javafx.collections.ObservableList
import javafx.scene.input.MouseButton
import tornadofx.Scope
import tornadofx.find
import tornadofx.setInScope

class LabelPaintingTrace(
   private val projectContext: ProjectContext,
   private val map: GameMapVM,
   override val commandName: String
) : PaintingTrace {
   private lateinit var labels: ObservableList<MapLabel>
   private lateinit var event: MapMouseEvent

   private var newLabel: MapLabel? = null
   private var formerLabel: MapLabel? = null
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

      labels = (editorStateVM.selectedLayer as ObjectLayer).labels
      formerLabel = labels.firstOrNull { it.x == x && it.y == y }

      event = mouseEvent
   }

   private fun createOrUpdateLabel() {
      showCodeDialog(formerLabel?.label ?: "")?.let {
         newLabel = MapLabel(x, y, it)
         labels.remove(formerLabel)
         labels.add(newLabel)
         executed = true
      }
   }

   private fun showCodeDialog(initialContent: String): String? {
      val scope = Scope()
      val vm = StringVM(initialContent)
      setInScope(vm, scope)

      var content: String? = null

      find<StringInputFragment>(scope).apply {
         title = "Set label"

         onComplete {
            content = it
         }

         openModal(block = true)
      }

      return content
   }

   private fun moveLabel(newX: Int, newY: Int) {
      if (newY >= map.rows || newX >= map.columns || newY < 0 || newX < 0) {
         return
      }

      formerLabel?.let {
         newLabel = MapLabel(newX, newY, it.label)
         labels.remove(formerLabel)
         labels.add(newLabel)
         executed = true
      }
   }

   private fun removeLabel() {
      labels.remove(formerLabel)
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
         moveLabel(newX, newY)
         return
      }

      // Creating new or updating existing one or removing
      if (event.event.clickCount > 1 && brushVM.mode == BrushMode.PAINTING_MODE) {
         when (event.button) {
            MouseButton.PRIMARY -> createOrUpdateLabel()
            MouseButton.SECONDARY -> removeLabel()
            else -> {
            }
         }

         return
      }

      // Removing
      if (brushVM.mode == BrushMode.ERASING_MODE) {
         removeLabel()
      }
   }

   override fun undo() {
      labels.remove(newLabel)
      formerLabel?.let(labels::add)
   }

   override fun redo() {
      labels.remove(formerLabel)
      newLabel?.let(labels::add)
   }

   override val supportedButtons = arrayOf(MouseButton.PRIMARY, MouseButton.SECONDARY)
}