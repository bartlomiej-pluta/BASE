package com.bartlomiejpluta.base.editor.map.component

import com.bartlomiejpluta.base.editor.map.canvas.MapCanvas
import com.bartlomiejpluta.base.editor.map.canvas.MapPainter
import com.bartlomiejpluta.base.editor.map.canvas.PaintingTrace
import com.bartlomiejpluta.base.editor.map.viewmodel.BrushVM
import com.bartlomiejpluta.base.editor.map.viewmodel.EditorStateVM
import com.bartlomiejpluta.base.editor.map.viewmodel.GameMapVM
import com.bartlomiejpluta.base.editor.project.context.ProjectContext
import com.bartlomiejpluta.base.editor.render.input.MapMouseEvent
import javafx.event.EventHandler
import javafx.scene.canvas.Canvas
import javafx.scene.input.MouseEvent

class MapPane(
   projectContext: ProjectContext,
   private val mapVM: GameMapVM,
   brushVM: BrushVM,
   editorStateVM: EditorStateVM,
   paintingCallback: (PaintingTrace) -> Unit
) : Canvas(), EventHandler<MouseEvent> {
   private val painter = MapPainter(projectContext, mapVM, brushVM, editorStateVM, paintingCallback)
   private val mapCanvas = MapCanvas(mapVM, editorStateVM, painter)

   init {
      onMouseMoved = this
      onMouseDragged = this
      onMousePressed = this
      onMouseReleased = this

      widthProperty().bind(mapVM.widthProperty)
      heightProperty().bind(mapVM.heightProperty)

      mapVM.item.rowsProperty.addListener { _, _, _ -> render() }
      mapVM.item.columnsProperty.addListener { _, _, _ -> render() }

      editorStateVM.showGridProperty.addListener { _, _, _ -> render() }
      editorStateVM.selectedLayerIndexProperty.addListener { _, _, _ -> render() }
      editorStateVM.coverUnderlyingLayersProperty.addListener { _, _, _ -> render() }
      editorStateVM.renderAllLayersProperty.addListener { _, _, _ -> render() }

      render()
   }

   fun render() {
      mapCanvas.render(graphicsContext2D)
   }

   override fun handle(event: MouseEvent?) {
      if (event != null) {
         painter.handleMouseInput(MapMouseEvent.of(event, mapVM))
      }

      mapCanvas.render(graphicsContext2D)
   }
}