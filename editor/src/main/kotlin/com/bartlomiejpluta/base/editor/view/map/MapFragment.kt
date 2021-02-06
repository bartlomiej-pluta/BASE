package com.bartlomiejpluta.base.editor.view.map

import com.bartlomiejpluta.base.editor.command.context.UndoableScope
import com.bartlomiejpluta.base.editor.command.service.UndoRedoService
import com.bartlomiejpluta.base.editor.event.RedrawMapRequestEvent
import com.bartlomiejpluta.base.editor.model.map.map.GameMap
import com.bartlomiejpluta.base.editor.viewmodel.map.GameMapVM
import org.kordamp.ikonli.javafx.FontIcon
import tornadofx.*


class MapFragment : Fragment() {
   private val undoRedoService: UndoRedoService by di()

   override val scope = super.scope as UndoableScope
   val map: GameMap by param()

   private val mapVM = find<GameMapVM>().apply { item = map }

   private val mapView = find<MapView>()
   private val layersView = find<MapLayersView>()
   private val tileSetView = find<TileSetView>()

   override val root = borderpane {
      top = toolbar {
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

         button(graphic = FontIcon("fa-plus")) {
            action {
               mapVM.rows = mapVM.rows + 1
               mapVM.commit()
               fire(RedrawMapRequestEvent)
            }
         }

         button(graphic = FontIcon("fa-plus")) {
            action {
               mapVM.columns = mapVM.columns + 1
               mapVM.commit()
               fire(RedrawMapRequestEvent)
            }
         }
      }

      center = mapView.root

      right = drawer(multiselect = true) {
         item("Layers", expanded = true) {
            this += layersView.root
         }

         item("Tile Set", expanded = true) {
            this += tileSetView.root.apply {
               maxHeightProperty().bind(this@item.heightProperty())
            }
         }
      }
   }
}
