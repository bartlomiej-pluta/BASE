package com.bartlomiejpluta.base.editor.map.canvas

import com.bartlomiejpluta.base.editor.code.model.Code
import com.bartlomiejpluta.base.editor.code.model.CodeScope
import com.bartlomiejpluta.base.editor.code.model.CodeType
import com.bartlomiejpluta.base.editor.code.view.editor.CodeSnippetFragment
import com.bartlomiejpluta.base.editor.code.viewmodel.CodeVM
import com.bartlomiejpluta.base.editor.file.model.DummyFileNode
import com.bartlomiejpluta.base.editor.map.model.brush.BrushMode
import com.bartlomiejpluta.base.editor.map.model.layer.ObjectLayer
import com.bartlomiejpluta.base.editor.map.model.obj.MapObject
import com.bartlomiejpluta.base.editor.map.viewmodel.BrushVM
import com.bartlomiejpluta.base.editor.map.viewmodel.EditorStateVM
import com.bartlomiejpluta.base.editor.map.viewmodel.GameMapVM
import com.bartlomiejpluta.base.editor.project.context.ProjectContext
import com.bartlomiejpluta.base.editor.render.input.MapMouseEvent
import javafx.collections.ObservableList
import javafx.scene.input.MouseButton
import tornadofx.find
import tornadofx.setInScope
import tornadofx.toProperty

class ObjectPaintingTrace(
   private val projectContext: ProjectContext,
   private val map: GameMapVM,
   override val commandName: String
) : PaintingTrace {
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
      showCodeDialog(formerObject?.code ?: initialCode)?.let {
         newObject = MapObject(x, y, it)
         objects.remove(formerObject)
         objects.add(newObject)
         executed = true
      }
   }

   private fun showCodeDialog(initialContent: String): String? {
      val scope = CodeScope(1, 1)
      val code = Code(DummyFileNode(), CodeType.JAVA.toProperty(), initialContent)
      val vm = CodeVM(code)
      setInScope(vm, scope)

      var content: String? = null

      find<CodeSnippetFragment>(scope).apply {
         title = "Define object"

         onComplete {
            content = it
         }

         openModal(block = true)
      }

      return content
   }

   private val initialCode: String
      get() = """
         /*  
          *  Following final parameters are available to use:
          *  x: int - the x coordinate of tile the object has been created on
          *  y: int - the y coordinate of tile the object has been created on 
          *  layer: ObjectLayer - current object layer
          *  map: GameMap - current map
          *  handler: ${className(map.handler)} - current map handler
          *  runner: ${className(projectContext.project?.runner)} - the game runner of the project
          *  context: Context - the game context
          */
         
      """.trimIndent()

   private fun className(canonical: String?) = canonical?.substringAfterLast(".") ?: ""

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