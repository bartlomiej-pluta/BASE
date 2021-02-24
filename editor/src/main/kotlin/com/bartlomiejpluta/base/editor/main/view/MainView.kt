package com.bartlomiejpluta.base.editor.main.view

import com.bartlomiejpluta.base.editor.asset.view.list.AssetsListView
import com.bartlomiejpluta.base.editor.code.model.Code
import com.bartlomiejpluta.base.editor.code.view.CodeEditorFragment
import com.bartlomiejpluta.base.editor.code.view.ScriptFilesView
import com.bartlomiejpluta.base.editor.code.viewmodel.CodeVM
import com.bartlomiejpluta.base.editor.main.controller.MainController
import com.bartlomiejpluta.base.editor.map.model.map.GameMap
import com.bartlomiejpluta.base.editor.map.view.editor.MapFragment
import com.bartlomiejpluta.base.editor.map.viewmodel.GameMapVM
import com.bartlomiejpluta.base.editor.project.context.ProjectContext
import javafx.collections.MapChangeListener
import javafx.scene.control.Tab
import org.kordamp.ikonli.javafx.FontIcon
import tornadofx.*


class MainView : View("BASE Game Editor") {
   private val mainController: MainController by di()
   private val projectContext: ProjectContext by di()

   private val mainMenuView = find<MainMenuView>()
   private val assetsView = find<AssetsListView>()
   private val scriptFilesView = find<ScriptFilesView>()

   private val openTabs = mutableMapOf<Scope, Tab>()

   init {
      projectContext.projectProperty.addListener { _, _, project ->
         val projectName = project?.let { " :: ${it.name} (${it.sourceDirectory.absolutePath})" } ?: ""
         title = "BASE Game Editor$projectName"
      }
   }

   override val root = borderpane {
      top = mainMenuView.root

      center = tabpane {

         // FIXME
         // For some reason the plain object binding does not work between tabs and mainController.openItems.
         // Because of that, the cache openTabs map has been created and the binding is done by following listener:
         mainController.openItems.addListener(MapChangeListener {
            when {
               it.wasAdded() -> createTab(it.key, it.valueAdded).let { tab ->
                  openTabs[it.key] = tab
                  tabs += tab
               }

               it.wasRemoved() -> {
                  val tab = openTabs[it.key]
                  tabs -= tab
                  openTabs.remove(it.key)
               }
            }
         })
      }

      left = drawer(multiselect = true) {
         item("Code", expanded = true) {
            this += scriptFilesView
         }

         item("Assets", expanded = true) {
            this += assetsView
         }
      }
   }

   private fun createTab(scope: Scope, item: Any) = when (item) {
      is GameMap -> Tab().apply {
         val vm = GameMapVM(item)
         setInScope(vm, scope)
         projectContext.project?.maps?.first { it.uid == item.uid }
            ?.let { textProperty().bindBidirectional(it.nameProperty) }
         content = find<MapFragment>(scope).root
         graphic = FontIcon("fa-map")
         setOnClosed { mainController.openItems.remove(scope) }
      }

      is Code -> Tab().apply {
         val vm = CodeVM(item)
         setInScope(vm, scope)
         content = find<CodeEditorFragment>(scope).root
         textProperty().bindBidirectional(item.fileProperty.select { it.name.toProperty() })
         graphic = FontIcon("fa-code")
         setOnClosed { mainController.openItems.remove(scope) }
      }

      else -> throw IllegalStateException("Unsupported tab item")
   }
}