package com.bartlomiejpluta.base.editor.main.view

import com.bartlomiejpluta.base.editor.main.controller.MainController
import com.bartlomiejpluta.base.editor.map.view.MapFragment
import javafx.scene.control.Tab
import tornadofx.*


class MainView : View("BASE Game Editor") {
   private val mainController: MainController by di()

   private val mainMenuView = find<MainMenuView>()

   init {
      mainController.openProject.addListener { _, _, project ->
         val projectName = project?.let { " :: ${it.name} (${it.sourceDirectory.absolutePath})" } ?: ""
         title = "BASE Game Editor$projectName"
      }
   }

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