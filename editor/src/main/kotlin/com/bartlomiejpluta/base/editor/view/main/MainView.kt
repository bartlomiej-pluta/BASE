package com.bartlomiejpluta.base.editor.view.main

import com.bartlomiejpluta.base.editor.controller.main.MainController
import com.bartlomiejpluta.base.editor.view.map.MapFragment
import javafx.scene.control.Tab
import tornadofx.*


class MainView : View("BASE Game Editor") {
   private val mainController: MainController by di()

   private val mainMenuView = find<MainMenuView>()

   override val root = borderpane {
      top = mainMenuView.root

      center = tabpane {
         tabs.bind(mainController.openMaps) { scope, map ->
            Tab().apply {
               textProperty().bindBidirectional(map.nameProperty)
               content = find<MapFragment>(scope).root
               setOnClosed { mainController.openMaps.remove(scope) }
            }
         }
      }
   }
}