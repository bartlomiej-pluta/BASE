package com.bartlomiejpluta.base.editor.main.view

import com.bartlomiejpluta.base.editor.asset.component.GraphicAssetPreviewFragment
import com.bartlomiejpluta.base.editor.asset.view.list.AssetsListView
import com.bartlomiejpluta.base.editor.asset.viewmodel.GraphicAssetVM
import com.bartlomiejpluta.base.editor.code.view.build.BuildLogsView
import com.bartlomiejpluta.base.editor.code.view.editor.CodeEditorFragment
import com.bartlomiejpluta.base.editor.code.view.list.ScriptFilesView
import com.bartlomiejpluta.base.editor.code.viewmodel.CodeVM
import com.bartlomiejpluta.base.editor.database.view.list.TablesListView
import com.bartlomiejpluta.base.editor.database.view.query.QueryResultFragment
import com.bartlomiejpluta.base.editor.database.viewmodel.QueryVM
import com.bartlomiejpluta.base.editor.event.SelectMainViewTabEvent
import com.bartlomiejpluta.base.editor.main.component.EditorTab
import com.bartlomiejpluta.base.editor.main.controller.MainController
import com.bartlomiejpluta.base.editor.map.view.editor.MapFragment
import com.bartlomiejpluta.base.editor.map.viewmodel.GameMapVM
import com.bartlomiejpluta.base.editor.process.view.ProcessLogsView
import com.bartlomiejpluta.base.editor.project.context.ProjectContext
import com.bartlomiejpluta.base.editor.project.view.ProjectParametersView
import javafx.beans.binding.Bindings.createStringBinding
import javafx.collections.MapChangeListener
import javafx.event.Event
import javafx.scene.control.Tab
import javafx.scene.input.KeyEvent
import org.kordamp.ikonli.javafx.FontIcon
import tornadofx.*


class MainView : View("BASE Game Editor") {
   private val mainController: MainController by di()
   private val projectContext: ProjectContext by di()

   private val mainMenuView = find<MainMenuView>()
   private val assetsView = find<AssetsListView>()
   private val scriptFilesView = find<ScriptFilesView>()
   private val buildLogsView = find<BuildLogsView>()
   private val processLogsView = find<ProcessLogsView>()
   private val projectPropertiesView = find<ProjectParametersView>()
   private val databaseTablesListView = find<TablesListView>()

   private val openTabs = mutableMapOf<Scope, Tab>()

   private var buildLogItem: DrawerItem by singleAssign()
   private var processLogItem: DrawerItem by singleAssign()

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
         when (project) {
            null -> "BASE Game Editor".toProperty()
            else -> createStringBinding(
               { "BASE Game Editor :: ${project.name} (${project.sourceDirectory.absolutePath})" },
               project.nameProperty, project.sourceDirectoryProperty
            )
         }.let { titleProperty.bind(it) }
      }

      subscribe<SelectMainViewTabEvent> { event ->
         openTabs[event.targetScope]?.let {
            tabPane.selectionModel.select(it)
         }
      }
   }

   override val root = borderpane {
      addEventHandler(KeyEvent.KEY_PRESSED, this@MainView::handleShortcut)

      top = mainMenuView.root

      center = tabPane

      left = drawer(multiselect = true) {
         item("Code", expanded = false) {
            this += scriptFilesView
         }

         item("Assets", expanded = false) {
            this += assetsView
         }

         item("Database", expanded = false) {
            this += databaseTablesListView
         }

         item("Project Parameters") {
            enableWhen(projectContext.projectProperty.isNotNull)
            this += projectPropertiesView
         }
      }

      bottom = drawer {
         buildLogItem = item("Build Log") {
            this += buildLogsView
         }

         processLogItem = item("Process Log") {
            this += processLogsView
         }
      }
   }

   private fun createTab(scope: Scope, vm: Any) = when (vm) {
      is GameMapVM -> {
         setInScope(vm, scope)

         EditorTab(find<MapFragment>(scope), FontIcon("fa-map")).apply {
            projectContext.project
               ?.maps
               ?.first { it.uid == vm.uid }
               ?.let { textProperty().bindBidirectional(it.nameProperty) }

            setOnClosed { mainController.openItems.remove(scope) }
         }
      }

      is CodeVM -> {
         setInScope(vm, scope)

         EditorTab(find<CodeEditorFragment>(scope), FontIcon("fa-code")).apply {
            textProperty().bind(vm.fileNode.nameProperty)

            setOnClosed {
               fragment.shutdown()
               mainController.openItems.remove(scope)
            }
         }
      }

      is QueryVM -> {
         setInScope(vm, scope)

         EditorTab(find<QueryResultFragment>(scope), FontIcon("fa-table")).apply {
            textProperty().bind(vm.nameProperty)

            setOnClosed { mainController.openItems.remove(scope) }
         }
      }

      is GraphicAssetVM -> {
         setInScope(vm, scope)

         EditorTab(find<GraphicAssetPreviewFragment>(scope), FontIcon("fa-file-image-o")).apply {
            textProperty().bind(vm.nameProperty)

            setOnClosed {
               mainController.openItems.remove(scope)
            }
         }
      }

      else -> throw IllegalStateException("Unsupported tab item")
   }

   private fun handleShortcut(event: KeyEvent) {
      val currentTab = tabPane.selectionModel.selectedItem

      if (currentTab is EditorTab<*>) {
         currentTab.handleShortcut(event)
      }
   }
}