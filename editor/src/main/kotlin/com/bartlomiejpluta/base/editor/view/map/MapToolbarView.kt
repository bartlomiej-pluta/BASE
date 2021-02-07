package com.bartlomiejpluta.base.editor.view.map

import com.bartlomiejpluta.base.editor.command.context.UndoableScope
import com.bartlomiejpluta.base.editor.command.service.UndoRedoService
import com.bartlomiejpluta.base.editor.event.RedrawMapRequestEvent
import com.bartlomiejpluta.base.editor.model.map.brush.BrushMode
import com.bartlomiejpluta.base.editor.viewmodel.map.BrushVM
import com.bartlomiejpluta.base.editor.viewmodel.map.GameMapVM
import javafx.scene.control.ToggleGroup
import org.kordamp.ikonli.javafx.FontIcon
import org.slf4j.LoggerFactory
import tornadofx.*
import java.util.*
import kotlin.math.max

class MapToolbarView : View() {
   private val undoRedoService: UndoRedoService by di()

   override val scope = super.scope as UndoableScope

   private val mapVM = find<GameMapVM>()
   private val brushVM = find<BrushVM>()

   private val brushMode = ToggleGroup().apply {
      brushVM.itemProperty.addListener { _, _, brush ->
         selectedValueProperty<BrushMode>().value = brush.mode
      }
   }

   override val root = toolbar {
      button(graphic = FontIcon("fa-undo")) {
         shortcut("Ctrl+Z")
         action {
            undoRedoService.undo(scope)
            fire(RedrawMapRequestEvent)
         }
      }

      button(graphic = FontIcon("fa-repeat")) {
         shortcut("Ctrl+Shift+Z")
         action {
            undoRedoService.redo(scope)
            fire(RedrawMapRequestEvent)
         }
      }

      button(text = "Rows", graphic = FontIcon("fa-minus")) {
         action {
            mapVM.rows = max(mapVM.rows - 1, 1)
            mapVM.commit()
            fire(RedrawMapRequestEvent)
         }
      }

      button(text = "Columns", graphic = FontIcon("fa-minus")) {
         action {
            mapVM.columns = max(mapVM.columns - 1, 1)
            mapVM.commit()
            fire(RedrawMapRequestEvent)
         }
      }

      button(text = "Rows", graphic = FontIcon("fa-plus")) {
         action {
            ++mapVM.rows
            mapVM.commit()
            fire(RedrawMapRequestEvent)
         }
      }

      button(text = "Columns", graphic = FontIcon("fa-plus")) {
         action {
            ++mapVM.columns
            mapVM.commit()
            fire(RedrawMapRequestEvent)
         }
      }

      togglebutton(value = BrushMode.PAINTING_MODE, group = brushMode) {
         graphic = FontIcon("fa-paint-brush")

         action {
            brushVM.item = brushVM.withPaintingMode()
         }
      }

      togglebutton(value = BrushMode.ERASING_MODE, group = brushMode) {
         graphic = FontIcon("fa-eraser")

         action {
            brushVM.item = brushVM.withErasingMode()
         }
      }

      this += FontIcon("fa-paint-brush").apply { iconSize = 10 }

      slider(1..5) {
         majorTickUnit = 1.0
         isSnapToTicks = true
         minorTickCount = 0

         valueProperty().addListener { _, _, newValue ->
            brushVM.item = brushVM.withBrushRange(newValue.toInt())
         }

         brushVM.itemProperty.addListener { _, _, brush ->
            value = brush.brushRange.toDouble()
         }
      }

      this += FontIcon("fa-paint-brush").apply { iconSize = 15 }
   }
}