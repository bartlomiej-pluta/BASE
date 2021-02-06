package com.bartlomiejpluta.base.editor.view.main

import com.bartlomiejpluta.base.editor.command.context.UndoableScope
import com.bartlomiejpluta.base.editor.controller.map.MapController
import com.bartlomiejpluta.base.editor.controller.tileset.TileSetController
import com.bartlomiejpluta.base.editor.view.map.MapFragment
import javafx.scene.control.TabPane
import org.kordamp.ikonli.javafx.FontIcon
import tornadofx.*


class MainView : View() {
   private val mapController: MapController by di()
   private val tileSetController: TileSetController by di()
   private val tabPane = TabPane()

   override val root = borderpane {
      top = toolbar {
         button(graphic = FontIcon("fa-file-o")) {
            action {
               val tileSet = tileSetController.tileset
               val map = mapController.createMap(tileSet, 25, 25)
               tabPane += find<MapFragment>(UndoableScope(), MapFragment::map to map).apply {
                  title = "Map 1"
               }
            }
         }
      }

      center = tabPane
   }
}