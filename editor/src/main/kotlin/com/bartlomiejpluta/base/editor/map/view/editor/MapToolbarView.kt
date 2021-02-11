package com.bartlomiejpluta.base.editor.map.view.editor

import com.bartlomiejpluta.base.editor.command.context.UndoableScope
import com.bartlomiejpluta.base.editor.command.service.UndoRedoService
import com.bartlomiejpluta.base.editor.event.RedrawMapRequestEvent
import com.bartlomiejpluta.base.editor.map.model.brush.BrushMode
import com.bartlomiejpluta.base.editor.map.viewmodel.BrushVM
import com.bartlomiejpluta.base.editor.map.viewmodel.EditorStateVM
import com.bartlomiejpluta.base.editor.map.viewmodel.GameMapVM
import javafx.scene.control.ToggleGroup
import org.kordamp.ikonli.javafx.FontIcon
import tornadofx.*

class MapToolbarView : View() {
   private val undoRedoService: UndoRedoService by di()

   override val scope = super.scope as UndoableScope

   private val mapVM = find<GameMapVM>()
   private val brushVM = find<BrushVM>()
   private val editorStateVM = find<EditorStateVM>()

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

      togglebutton {
         graphic = FontIcon("fa-window-restore")

         action {
            editorStateVM.coverUnderlyingLayers = isSelected
         }
      }

      togglebutton {
         graphic = FontIcon("fa-th")

         action {
            editorStateVM.showGrid = isSelected
         }
      }

      togglebutton(value = BrushMode.PAINTING_MODE, group = brushMode) {
         graphic = FontIcon("fa-paint-brush")

         action {
            brushVM.item = brushVM.withMode(BrushMode.PAINTING_MODE)
            brushVM.commit()
         }
      }

      togglebutton(value = BrushMode.ERASING_MODE, group = brushMode) {
         graphic = FontIcon("fa-eraser")

         action {
            brushVM.item = brushVM.withMode(BrushMode.ERASING_MODE)
            brushVM.commit()
         }
      }

      this += FontIcon("fa-paint-brush").apply { iconSize = 10 }

      slider(1..5) {
         majorTickUnit = 1.0
         isSnapToTicks = true
         minorTickCount = 0

         valueProperty().addListener { _, _, newValue ->
            brushVM.item = brushVM.withRange(newValue.toInt())
            brushVM.commit()
         }

         brushVM.itemProperty.addListener { _, _, brush ->
            value = brush.range.toDouble()
         }
      }

      this += FontIcon("fa-paint-brush").apply { iconSize = 15 }

      button(graphic = FontIcon("fa-sliders")) {
         action {
            find<MapSettingsFragment>().openModal(block = true, resizable = false)
         }
      }
   }
}