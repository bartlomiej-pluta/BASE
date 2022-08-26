package com.bartlomiejpluta.base.editor.map.canvas

import com.bartlomiejpluta.base.editor.map.model.brush.BrushTool
import com.bartlomiejpluta.base.editor.map.model.layer.ImageLayer
import com.bartlomiejpluta.base.editor.map.model.layer.ObjectLayer
import com.bartlomiejpluta.base.editor.map.model.layer.TileLayer
import com.bartlomiejpluta.base.editor.map.viewmodel.BrushVM
import com.bartlomiejpluta.base.editor.map.viewmodel.EditorStateVM
import com.bartlomiejpluta.base.editor.map.viewmodel.GameMapVM
import com.bartlomiejpluta.base.editor.project.context.ProjectContext
import com.bartlomiejpluta.base.editor.render.input.MapMouseEvent
import com.bartlomiejpluta.base.editor.render.input.MapMouseEventHandler
import com.bartlomiejpluta.base.editor.render.model.Renderable
import javafx.scene.canvas.GraphicsContext
import javafx.scene.input.MouseEvent

class MapPainter(
   private val projectContext: ProjectContext,
   private val mapVM: GameMapVM,
   private val brushVM: BrushVM,
   private val editorStateVM: EditorStateVM,
   private val paintingCallback: (PaintingTrace) -> Unit
) : Renderable, MapMouseEventHandler {
   private val tileWidth = mapVM.tileWidth.toDouble()
   private val tileHeight = mapVM.tileHeight.toDouble()

   private var cursor: PaintingCursor? = null
   private var currentTrace: PaintingTrace? = null

   init {
      editorStateVM.selectedLayerProperty.addListener { _, _, _ -> updateCursor() }
      brushVM.toolProperty.addListener { _, _, _ -> updateCursor() }
   }

   private fun updateCursor() {
      cursor = when (editorStateVM.selectedLayer) {
         is TileLayer -> TilePaintingCursor(tileWidth, tileHeight, editorStateVM, brushVM)
         is ObjectLayer -> when (brushVM.tool) {
            BrushTool.PASSAGE -> PassageAbilityPaintingCursor(tileWidth, tileHeight, editorStateVM, brushVM)
            else -> null
         }
         else -> null
      }
   }

   override fun render(gc: GraphicsContext) {
      val alpha = gc.globalAlpha
      gc.globalAlpha = 0.4

      cursor?.render(gc)

      gc.globalAlpha = alpha
   }

   override fun handleMouseInput(event: MapMouseEvent) {
      editorStateVM.cursorRowProperty.value = event.row
      editorStateVM.cursorColumnProperty.value = event.column

      when (event.type) {
         MouseEvent.MOUSE_PRESSED -> beginTrace(event)
         MouseEvent.MOUSE_DRAGGED -> proceedTrace(event)
         MouseEvent.MOUSE_RELEASED -> commitTrace(event)
      }
   }

   private fun beginTrace(event: MapMouseEvent) {
      if (currentTrace == null && editorStateVM.selectedLayerIndex >= 0) {
         currentTrace = when (editorStateVM.selectedLayer) {
            is TileLayer -> TilePaintingTrace(mapVM, "Paint trace")
            is ObjectLayer -> when (brushVM.tool) {
               BrushTool.DEFAULT -> ObjectPaintingTrace(projectContext, mapVM, "Update object")
               else -> PassageAbilityPaintingTrace(mapVM, "Toggle passage")
            }
            is ImageLayer -> ImagePositionPaintingTrace(mapVM, "Move Image Layer")
            else -> null
         }
            ?.takeIf { event.button in it.supportedButtons }
            ?.apply { beginTrace(editorStateVM, brushVM, event) }
      }
   }

   private fun proceedTrace(event: MapMouseEvent) = currentTrace
      ?.takeIf { event.button in it.supportedButtons }
      ?.proceedTrace(editorStateVM, brushVM, event)

   private fun commitTrace(event: MapMouseEvent) = currentTrace
      ?.takeIf { event.button in it.supportedButtons }
      ?.let {
         it.commitTrace(editorStateVM, brushVM, event)
         paintingCallback(it)
         currentTrace = null
      }
}