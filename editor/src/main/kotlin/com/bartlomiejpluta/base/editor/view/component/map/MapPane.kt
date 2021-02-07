package com.bartlomiejpluta.base.editor.view.component.map

import com.bartlomiejpluta.base.editor.render.canvas.input.MapMouseEvent
import com.bartlomiejpluta.base.editor.render.canvas.map.MapCanvas
import com.bartlomiejpluta.base.editor.render.canvas.map.MapPainter
import com.bartlomiejpluta.base.editor.render.canvas.map.MapPaintingTrace
import com.bartlomiejpluta.base.editor.viewmodel.map.BrushVM
import com.bartlomiejpluta.base.editor.viewmodel.map.EditorStateVM
import com.bartlomiejpluta.base.editor.viewmodel.map.GameMapVM
import javafx.event.EventHandler
import javafx.scene.canvas.Canvas
import javafx.scene.input.MouseEvent


class MapPane(
   private val mapVM: GameMapVM,
   brushVM: BrushVM,
   editorStateVM: EditorStateVM,
   paintingCallback: (MapPaintingTrace) -> Unit
) : Canvas(), EventHandler<MouseEvent> {
   private val painter = MapPainter(mapVM, brushVM, editorStateVM, paintingCallback)
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
      editorStateVM.selectedLayerProperty.addListener { _, _, _ -> render() }
      editorStateVM.coverUnderlyingLayersProperty.addListener { _, _, _ -> render() }

      render()
   }

   fun render() {
      mapCanvas.render(graphicsContext2D)
   }

   override fun handle(event: MouseEvent?) {
      if (event != null) {
         painter.handleMouseInput(MapMouseEvent.of(event, mapVM.tileSet))
      }

      mapCanvas.render(graphicsContext2D)
   }
}