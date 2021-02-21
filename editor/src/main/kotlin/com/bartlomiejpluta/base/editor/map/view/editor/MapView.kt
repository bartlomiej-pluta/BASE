package com.bartlomiejpluta.base.editor.map.view.editor

import com.bartlomiejpluta.base.editor.command.context.UndoableScope
import com.bartlomiejpluta.base.editor.command.service.UndoRedoService
import com.bartlomiejpluta.base.editor.event.RedrawMapRequestEvent
import com.bartlomiejpluta.base.editor.map.component.MapPane
import com.bartlomiejpluta.base.editor.map.viewmodel.BrushVM
import com.bartlomiejpluta.base.editor.map.viewmodel.EditorStateVM
import com.bartlomiejpluta.base.editor.map.viewmodel.GameMapVM
import javafx.scene.image.Image
import javafx.scene.input.MouseButton
import javafx.scene.input.MouseEvent
import javafx.scene.transform.Scale
import tornadofx.View
import tornadofx.group
import tornadofx.plusAssign
import tornadofx.scrollpane


class MapView : View() {
   private val undoRedoService: UndoRedoService by di()

   override val scope = super.scope as UndoableScope

   private val mapVM = find<GameMapVM>()

   private val brushVM = find<BrushVM>()

   private val editorStateVM = find<EditorStateVM>()

   private val mapPane = MapPane(mapVM, brushVM, editorStateVM) { undoRedoService.push(it, scope) }

   private val zoom = Scale(1.0, 1.0, 0.0, 0.0).apply {
      xProperty().bind(editorStateVM.zoomProperty)
      yProperty().bind(editorStateVM.zoomProperty)
   }

   init {
      brushVM.item = mapVM.tileSet.baseBrush
      brushVM.commit()

      subscribe<RedrawMapRequestEvent> { mapPane.render() }
   }

   fun snapshot(): Image {
      editorStateVM.takingSnapshot = true
      mapPane.render()

      return mapPane.snapshot(null, null).also {
         editorStateVM.takingSnapshot = false
         mapPane.render()
      }
   }

   override val root = scrollpane {
      prefWidth = 640.0
      prefHeight = 480.0
      isPannable = true

      group {

         // Let the ScrollPane.viewRect only pan on middle button.
         addEventHandler(MouseEvent.ANY) {
            if (it.button != MouseButton.MIDDLE) {
               it.consume()
            }
         }

         group {
            this += mapPane
            transforms += zoom
         }
      }
   }
}