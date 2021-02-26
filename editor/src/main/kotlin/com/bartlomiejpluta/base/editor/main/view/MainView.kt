package com.bartlomiejpluta.base.editor.main.view

import com.bartlomiejpluta.base.editor.asset.view.list.AssetsListView
import com.bartlomiejpluta.base.editor.code.model.Code
import com.bartlomiejpluta.base.editor.code.view.CodeEditorFragment
import com.bartlomiejpluta.base.editor.code.view.CompilerLogsView
import com.bartlomiejpluta.base.editor.code.view.ScriptFilesView
import com.bartlomiejpluta.base.editor.code.viewmodel.CodeVM
import com.bartlomiejpluta.base.editor.event.AppendCompilationLogEvent
import com.bartlomiejpluta.base.editor.event.SelectMainViewTabEvent
import com.bartlomiejpluta.base.editor.main.controller.MainController
import com.bartlomiejpluta.base.editor.map.model.map.GameMap
import com.bartlomiejpluta.base.editor.map.view.editor.MapFragment
import com.bartlomiejpluta.base.editor.map.viewmodel.GameMapVM
import com.bartlomiejpluta.base.editor.project.context.ProjectContext
import javafx.collections.MapChangeListener
import javafx.event.Event
import javafx.scene.control.Tab
import org.kordamp.ikonli.javafx.FontIcon
import tornadofx.*


class MainView : View("BASE Game Editor") {
   private val mainController: MainController by di()
   private val projectContext: ProjectContext by di()

   private val mainMenuView = find<MainMenuView>()
   private val assetsView = find<AssetsListView>()
   private val scriptFilesView = find<ScriptFilesView>()
   private val compilerLogsView = find<CompilerLogsView>()

   private val openTabs = mutableMapOf<Scope, Tab>()

   private var compilationLogItem: DrawerItem by singleAssign()

   private val tabPane = tabpane {

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
               openTabs.remove(it.key)

               // FIXME
               // This ugly hack prevents from double-firing the CLOSED EVENT
               // when user tries to close tab from the UI (cross sign).
               // When user clicks the close button, the event is fired automatically,
               // which removes the item from mainController.openItems.
               // Right here, this listener would fire the event once again,
               // so essentially we need to check if the tab exists in TabPane.tabs
               // (which means the tab wasn't be closed from UI). If so, we need to fire
               // the event manually since it won't be fired by TabPane.
               // Otherwise, if the tab does not exist in tabs anymore, the tab
               // was closed from UI and likely the event has been fired automatically.
               // The same goes for the TAB_CLOSE_REQUEST event.
               if (tab in tabs) {
                  Event.fireEvent(tab, Event(Tab.CLOSED_EVENT))
                  Event.fireEvent(tab, Event(Tab.TAB_CLOSE_REQUEST_EVENT))
               }

               tabs -= tab
            }
         }
      })
   }

   init {
      projectContext.projectProperty.addListener { _, _, project ->
         val projectName = project?.let { " :: ${it.name} (${it.sourceDirectory.absolutePath})" } ?: ""
         title = "BASE Game Editor$projectName"
      }

      subscribe<AppendCompilationLogEvent> {
         compilationLogItem.expanded = true
      }

      subscribe<SelectMainViewTabEvent> { event ->
         openTabs[event.targetScope]?.let {
            tabPane.selectionModel.select(it)
         }
      }
   }

   override val root = borderpane {
      top = mainMenuView.root

      center = tabPane

      left = drawer(multiselect = true) {
         item("Code", expanded = false) {
            this += scriptFilesView
         }

         item("Assets", expanded = false) {
            this += assetsView
         }
      }

      bottom = drawer(multiselect = true) {
         compilationLogItem = item("Compilation Log") {
            this += compilerLogsView
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
         val editor = find<CodeEditorFragment>(scope)
         content = editor.root
         graphic = FontIcon("fa-code")
         textProperty().bindBidirectional(item.fileProperty.select { it.name.toProperty() })

         setOnClosed {
            editor.shutdown()
            mainController.openItems.remove(scope)
         }
      }

      else -> throw IllegalStateException("Unsupported tab item")
   }
}