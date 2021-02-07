package com.bartlomiejpluta.base.editor.view.map

import com.bartlomiejpluta.base.editor.command.context.UndoableScope
import com.bartlomiejpluta.base.editor.command.service.UndoRedoService
import com.bartlomiejpluta.base.editor.event.RedrawMapRequestEvent
import com.bartlomiejpluta.base.editor.viewmodel.map.BrushVM
import com.bartlomiejpluta.base.editor.viewmodel.map.GameMapVM
import javafx.scene.control.ToggleGroup
import org.kordamp.ikonli.javafx.FontIcon
import tornadofx.*
import kotlin.math.max

class MapToolbarView : View() {
   private val undoRedoService: UndoRedoService by di()

   override val scope = super.scope as UndoableScope

   private val mapVM = find<GameMapVM>()
   private val brushVM = find<BrushVM>()

   private val tool = ToggleGroup()

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

      togglebutton(tool) {
         graphic = FontIcon("fa-paint-brush")
      }

      togglebutton(tool) {
         graphic = FontIcon("fa-eraser")
      }

      this += FontIcon("fa-paint-brush").apply { iconSize = 10 }

      slider(1..5) {
         majorTickUnit = 1.0
         isSnapToTicks = true
         minorTickCount = 0

         valueProperty().addListener { _, _, newValue ->
            brushVM.item = brushVM.withBrushRange(newValue.toInt())
         }

         brushVM.brushRangeProperty.addListener { _, _, newValue ->
            value = newValue.toDouble()
         }
      }

      this += FontIcon("fa-paint-brush").apply { iconSize = 15 }
   }
}