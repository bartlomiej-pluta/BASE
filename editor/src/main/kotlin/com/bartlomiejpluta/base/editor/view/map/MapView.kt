package com.bartlomiejpluta.base.editor.view.map

import com.bartlomiejpluta.base.editor.command.service.UndoRedoService
import com.bartlomiejpluta.base.editor.event.RedrawMapRequestEvent
import com.bartlomiejpluta.base.editor.view.component.map.MapPane
import com.bartlomiejpluta.base.editor.viewmodel.map.BrushVM
import com.bartlomiejpluta.base.editor.viewmodel.map.GameMapVM
import javafx.beans.property.SimpleDoubleProperty
import javafx.scene.input.MouseButton
import javafx.scene.input.MouseEvent
import javafx.scene.transform.Scale
import tornadofx.View
import tornadofx.group
import tornadofx.plusAssign
import tornadofx.scrollpane


class MapView : View() {
   private val undoRedoService: UndoRedoService by di()

   val zoomProperty = SimpleDoubleProperty(1.0)
   private val zoom = Scale(1.0, 1.0, 0.0, 0.0).apply {
      xProperty().bind(zoomProperty)
      yProperty().bind(zoomProperty)
   }

   private val mapVM = find<GameMapVM>()
   private val brushVM = find<BrushVM>()

   private val mapPane = MapPane(mapVM, brushVM) { undoRedoService.push(it) }

   init {
      brushVM.item = mapVM.tileSet.baseBrush

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