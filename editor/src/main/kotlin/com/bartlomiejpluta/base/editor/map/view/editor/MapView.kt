package com.bartlomiejpluta.base.editor.map.view.editor

import com.bartlomiejpluta.base.editor.command.context.UndoableScope
import com.bartlomiejpluta.base.editor.command.service.UndoRedoService
import com.bartlomiejpluta.base.editor.event.RedrawMapRequestEvent
import com.bartlomiejpluta.base.editor.map.canvas.PaintingTrace
import com.bartlomiejpluta.base.editor.map.component.MapPane
import com.bartlomiejpluta.base.editor.map.model.brush.*
import com.bartlomiejpluta.base.editor.map.model.layer.AutoTileLayer
import com.bartlomiejpluta.base.editor.map.model.layer.Layer
import com.bartlomiejpluta.base.editor.map.model.layer.ObjectLayer
import com.bartlomiejpluta.base.editor.map.model.layer.TileLayer
import com.bartlomiejpluta.base.editor.map.viewmodel.BrushVM
import com.bartlomiejpluta.base.editor.map.viewmodel.EditorStateVM
import com.bartlomiejpluta.base.editor.map.viewmodel.GameMapVM
import com.bartlomiejpluta.base.editor.project.context.ProjectContext
import javafx.scene.input.MouseButton
import javafx.scene.input.MouseEvent
import javafx.scene.transform.Scale
import tornadofx.View
import tornadofx.group
import tornadofx.plusAssign
import tornadofx.scrollpane


class MapView : View() {
   private val undoRedoService: UndoRedoService by di()

   private val projectContext: ProjectContext by di()

   override val scope = super.scope as UndoableScope

   private val mapVM = find<GameMapVM>()

   private val brushVM = find<BrushVM>()

   private val editorStateVM = find<EditorStateVM>()

   private val mapPane =
      MapPane(projectContext, mapVM, brushVM, editorStateVM, this::pushPaintingTraceToUndoRedoService)

   private val zoom = Scale(1.0, 1.0, 0.0, 0.0).apply {
      xProperty().bind(editorStateVM.zoomProperty)
      yProperty().bind(editorStateVM.zoomProperty)
   }

   init {
      brushVM.item = EmptyBrush()
      brushVM.commit()

      editorStateVM.selectedLayerProperty.addListener { _, _, layer ->
         brushVM.item = determineBrush(layer)
         brushVM.commit()
      }

      subscribe<RedrawMapRequestEvent> { mapPane.render() }
   }

   private fun determineBrush(layer: Layer?) : Brush? = when (layer) {
      is TileLayer -> TileBrush((arrayOf(arrayOf(layer.tileSetProperty.value.tiles[0]))))
      is AutoTileLayer -> AutoTileBrush(1)
      is ObjectLayer -> PassageAbilityBrush()
      else -> null
   }

   private fun pushPaintingTraceToUndoRedoService(trace: PaintingTrace) {
      if (trace.executed) {
         undoRedoService.push(trace, scope)
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