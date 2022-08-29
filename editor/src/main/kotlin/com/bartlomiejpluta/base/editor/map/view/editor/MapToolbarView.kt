package com.bartlomiejpluta.base.editor.map.view.editor

import com.bartlomiejpluta.base.editor.command.context.UndoableScope
import com.bartlomiejpluta.base.editor.command.service.UndoRedoService
import com.bartlomiejpluta.base.editor.event.RedrawMapRequestEvent
import com.bartlomiejpluta.base.editor.map.controller.MapController
import com.bartlomiejpluta.base.editor.map.model.brush.BrushMode
import com.bartlomiejpluta.base.editor.map.model.brush.BrushTool
import com.bartlomiejpluta.base.editor.map.model.layer.AutoTileLayer
import com.bartlomiejpluta.base.editor.map.model.layer.ObjectLayer
import com.bartlomiejpluta.base.editor.map.model.layer.TileLayer
import com.bartlomiejpluta.base.editor.map.viewmodel.BrushVM
import com.bartlomiejpluta.base.editor.map.viewmodel.EditorStateVM
import com.bartlomiejpluta.base.editor.map.viewmodel.GameMapVM
import javafx.beans.binding.Bindings
import javafx.scene.control.ToggleGroup
import org.kordamp.ikonli.javafx.FontIcon
import tornadofx.*

class MapToolbarView : View() {
   private val undoRedoService: UndoRedoService by di()
   private val mapController: MapController by di()

   override val scope = super.scope as UndoableScope

   private val mapVM = find<GameMapVM>()
   private val brushVM = find<BrushVM>()
   private val editorStateVM = find<EditorStateVM>()

   private val brushMode = ToggleGroup()

   private val objectLayerTool = ToggleGroup()

   private val isTileLayerSelected = Bindings.createBooleanBinding(
      { editorStateVM.selectedLayer is TileLayer },
      editorStateVM.selectedLayerProperty
   )

   private val isAutoTileLayerSelected = Bindings.createBooleanBinding(
      { editorStateVM.selectedLayer is AutoTileLayer },
      editorStateVM.selectedLayerProperty
   )
   
   private val isObjectLayerSelected = Bindings.createBooleanBinding(
      { editorStateVM.selectedLayer is ObjectLayer },
      editorStateVM.selectedLayerProperty
   )

   init {
      brushVM.itemProperty.addListener { _, _, brush ->
         brushMode.selectedValueProperty<BrushMode>().value = brush?.mode ?: BrushMode.PAINTING_MODE
         objectLayerTool.selectedValueProperty<BrushTool>().value = brush?.tool ?: BrushTool.DEFAULT
      }

      editorStateVM.selectedLayerProperty.addListener { _, _, _ ->
         brushVM.tool = BrushTool.DEFAULT
      }
   }

   override val root = toolbar {
      button(graphic = FontIcon("fa-floppy-o")) {
         action {
            mapController.saveMap(mapVM.item)
         }
      }

      button(graphic = FontIcon("fa-undo")) {
         action {
            undoRedoService.undo(scope)
            fire(RedrawMapRequestEvent)
         }
      }

      button(graphic = FontIcon("fa-repeat")) {
         action {
            undoRedoService.redo(scope)
            fire(RedrawMapRequestEvent)
         }
      }

      separator()

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

      separator()

      togglebutton(value = BrushMode.PAINTING_MODE, group = brushMode) {
         graphic = FontIcon("fa-paint-brush")

         enableWhen(isTileLayerSelected.or(isAutoTileLayerSelected).or(isObjectLayerSelected))

         action {
            brushVM.mode = BrushMode.PAINTING_MODE
            brushVM.commit()
         }
      }

      togglebutton(value = BrushMode.ERASING_MODE, group = brushMode) {
         graphic = FontIcon("fa-eraser")

         enableWhen(isTileLayerSelected.or(isAutoTileLayerSelected).or(isObjectLayerSelected))

         action {
            brushVM.mode = BrushMode.ERASING_MODE
            brushVM.commit()
         }
      }

      this += FontIcon("fa-paint-brush").apply { iconSize = 10 }

      slider(1..15) {
         majorTickUnit = 1.0
         isSnapToTicks = true
         minorTickCount = 0

         enableWhen(isTileLayerSelected.or(isAutoTileLayerSelected).or(isObjectLayerSelected))

         valueProperty().addListener { _, _, newValue ->
            brushVM.range = newValue.toInt()
            brushVM.commit()
         }

         brushVM.itemProperty.addListener { _, _, brush ->
            value = brush?.range?.toDouble() ?: 1.0
         }
      }

      this += FontIcon("fa-paint-brush").apply { iconSize = 15 }

      separator {
         visibleWhen(isObjectLayerSelected)
      }

      togglebutton(value = BrushTool.DEFAULT, group = objectLayerTool) {
         graphic = FontIcon("fa-cube")

         visibleWhen(isObjectLayerSelected)

         action {
            brushVM.tool = BrushTool.DEFAULT
            brushVM.commit()
         }
      }

      togglebutton(value = BrushTool.PASSAGE, group = objectLayerTool) {
         graphic = FontIcon("fa-minus-circle")

         visibleWhen(isObjectLayerSelected)

         action {
            brushVM.tool = BrushTool.PASSAGE
            brushVM.commit()
         }
      }
   }
}