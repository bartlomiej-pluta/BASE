package com.bartlomiejpluta.base.editor.main.view

import com.bartlomiejpluta.base.editor.main.controller.MainController
import com.bartlomiejpluta.base.editor.map.view.editor.MapFragment
import com.bartlomiejpluta.base.editor.map.viewmodel.GameMapVM
import com.bartlomiejpluta.base.editor.project.context.ProjectContext
import javafx.scene.control.Tab
import tornadofx.*


class MainView : View("BASE Game Editor") {
   private val mainController: MainController by di()
   private val projectContext: ProjectContext by di()

   private val mainMenuView = find<MainMenuView>()

   init {
      projectContext.projectProperty.addListener { _, _, project ->
         val projectName = project?.let { " :: ${it.name} (${it.sourceDirectory.absolutePath})" } ?: ""
         title = "BASE Game Editor$projectName"
      }
   }

   override val root = borderpane {
      top = mainMenuView.root

      center = tabpane {
         tabs.bind(mainController.openMaps) { scope, map ->
            Tab().apply {
               val vm = GameMapVM(map)
               setInScope(vm, scope)
               projectContext.project?.maps?.get(map.uid)?.let { textProperty().bindBidirectional(it.nameProperty) }
               content = find<MapFragment>(scope).root
               setOnClosed { mainController.openMaps.remove(scope) }
            }
         }
      }
   }
}