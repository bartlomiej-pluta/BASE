package com.bartlomiejpluta.base.editor.view.map

import com.bartlomiejpluta.base.editor.command.context.UndoableScope
import com.bartlomiejpluta.base.editor.command.service.UndoRedoService
import com.bartlomiejpluta.base.editor.event.RedrawMapRequestEvent
import com.bartlomiejpluta.base.editor.view.component.map.MapPane
import com.bartlomiejpluta.base.editor.viewmodel.map.BrushVM
import com.bartlomiejpluta.base.editor.viewmodel.map.EditorStateVM
import com.bartlomiejpluta.base.editor.viewmodel.map.GameMapVM
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

   private val editorOptionsVM = find<EditorStateVM>()

   private val mapPane = MapPane(mapVM, brushVM, editorOptionsVM) { undoRedoService.push(it, scope) }

   private val zoom = Scale(1.0, 1.0, 0.0, 0.0).apply {
      xProperty().bind(editorOptionsVM.zoomProperty)
      yProperty().bind(editorOptionsVM.zoomProperty)
   }

   init {
      brushVM.item = mapVM.tileSet.baseBrush
      brushVM.commit()

      subscribe<RedrawMapRequestEvent> { mapPane.render() }
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