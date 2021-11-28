package com.bartlomiejpluta.base.editor.main.controller

import com.bartlomiejpluta.base.editor.animation.view.importing.ImportAnimationFragment
import com.bartlomiejpluta.base.editor.animation.viewmodel.AnimationAssetDataVM
import com.bartlomiejpluta.base.editor.asset.model.Asset
import com.bartlomiejpluta.base.editor.audio.view.importing.ImportSoundFragment
import com.bartlomiejpluta.base.editor.audio.viewmodel.SoundAssetDataVM
import com.bartlomiejpluta.base.editor.code.model.CodeScope
import com.bartlomiejpluta.base.editor.code.viewmodel.CodeVM
import com.bartlomiejpluta.base.editor.command.context.UndoableScope
import com.bartlomiejpluta.base.editor.database.model.data.Query
import com.bartlomiejpluta.base.editor.database.viewmodel.QueryVM
import com.bartlomiejpluta.base.editor.entityset.view.importing.ImportEntitySetFragment
import com.bartlomiejpluta.base.editor.entityset.viewmodel.EntitySetAssetDataVM
import com.bartlomiejpluta.base.editor.event.SelectMainViewTabEvent
import com.bartlomiejpluta.base.editor.file.model.FileNode
import com.bartlomiejpluta.base.editor.file.model.ScriptAssetFileNode
import com.bartlomiejpluta.base.editor.gui.font.view.importing.ImportFontFragment
import com.bartlomiejpluta.base.editor.gui.font.viewmodel.FontAssetDataVM
import com.bartlomiejpluta.base.editor.gui.widget.asset.WidgetAssetData
import com.bartlomiejpluta.base.editor.image.view.importing.ImportImageFragment
import com.bartlomiejpluta.base.editor.image.viewmodel.ImageAssetDataVM
import com.bartlomiejpluta.base.editor.map.asset.GameMapAsset
import com.bartlomiejpluta.base.editor.map.model.map.GameMap
import com.bartlomiejpluta.base.editor.map.view.wizard.MapCreationWizard
import com.bartlomiejpluta.base.editor.map.view.wizard.MapImportWizard
import com.bartlomiejpluta.base.editor.map.viewmodel.GameMapBuilderVM
import com.bartlomiejpluta.base.editor.map.viewmodel.GameMapVM
import com.bartlomiejpluta.base.editor.project.context.ProjectContext
import com.bartlomiejpluta.base.editor.project.model.Project
import com.bartlomiejpluta.base.editor.project.view.ProjectSettingsFragment
import com.bartlomiejpluta.base.editor.project.viewmodel.ProjectVM
import com.bartlomiejpluta.base.editor.tileset.view.importing.ImportTileSetFragment
import com.bartlomiejpluta.base.editor.tileset.viewmodel.TileSetAssetDataVM
import javafx.scene.control.TextInputDialog
import javafx.stage.FileChooser
import org.springframework.stereotype.Component
import tornadofx.*
import java.io.File
import kotlin.collections.set

@Component
class MainController : Controller() {
   private val projectContext: ProjectContext by di()

   val openItems = observableMapOf<Scope, ItemViewModel<*>>()

   fun createEmptyProject() {
      val vm = ProjectVM(Project())

      setInScope(vm)
      find<ProjectSettingsFragment>().apply {
         onComplete {
            openItems.clear()
            projectContext.createNewProject(it)
         }

         openModal(block = true, resizable = false)
      }
   }

   fun createEmptyMap() {
      val scope = UndoableScope()
      val vm = GameMapBuilderVM()
      setInScope(vm, scope)

      find<MapCreationWizard>(scope).apply {
         onComplete {
            val tileSet = projectContext.loadTileSet(vm.tileSetAsset.uid)
            val map = GameMap(tileSet).apply {
               rows = vm.rows
               columns = vm.columns
               handler = vm.handler
            }
            projectContext.importMap(vm.name, map)
            openItems[scope] = GameMapVM(map)
         }

         openModal(block = true, resizable = false)
      }
   }

   fun importMap() {
      val scope = UndoableScope()
      val vm = GameMapBuilderVM()
      setInScope(vm, scope)
      find<MapImportWizard>(scope).apply {
         onComplete {
            val tileSet = projectContext.loadTileSet(vm.tileSetAsset.uid)
            val map = projectContext.importMapFromFile(vm.name, vm.handler, File(vm.file), tileSet)
            openItems[scope] = GameMapVM(map)
         }

         openModal(block = true, resizable = false)
      }
   }

   fun openProject() {
      chooseFile(
         title = "Load Project",
         filters = arrayOf(FileChooser.ExtensionFilter("BASE Editor Project (*.bep)", "*.bep")),
      ).getOrNull(0)?.let {
         clearResources()
         projectContext.open(it)
      }
   }

   fun openMap(uid: String) {
      openItem<GameMap, GameMapVM, UndoableScope>({ it.uid == uid }) {
         val map = projectContext.loadMap(uid)
         val vm = GameMapVM(map)
         val scope = UndoableScope()
         setInScope(vm, scope)

         scope to vm
      }
   }

   fun openScript(
      fsNode: FileNode,
      line: Int = 1,
      column: Int = 1,
      execute: ((String) -> Unit)? = null,
      saveable: Boolean = true
   ) {
      val findScript = { script: CodeVM -> script.fileNode.absolutePath == fsNode.absolutePath }
      val updateExistingScope = { scope: CodeScope -> scope.setCaretPosition(line, column) }

      openItem(findScript, updateExistingScope) {
         val code = projectContext.loadScript(fsNode, execute, saveable)
         val vm = CodeVM(code)
         val scope = CodeScope(line, column)
         setInScope(vm, scope)

         scope to vm
      }
   }

   fun openQuery(query: Query) {
      val findQuery = { q: QueryVM -> q.name == query.name }
      val updateModel = { vm: QueryVM -> vm.item = query }

      openItem<Query, QueryVM, Scope>(findQuery, updateViewModel = updateModel) {
         val vm = QueryVM(query)
         val scope = Scope()
         setInScope(vm, scope)

         scope to vm
      }
   }

   private inline fun <reified T, reified VM : ItemViewModel<T>, S : Scope> openItem(
      findItem: (item: VM) -> Boolean,
      updateExistingScope: (S) -> Unit = {},
      updateViewModel: (VM) -> Unit = {},
      createItem: () -> Pair<Scope, VM>
   ) {
      @Suppress("UNCHECKED_CAST")
      val pair = openItems.entries
         .filter { (_, item) -> item is VM }
         .map { (scope, item) -> (scope as S) to (item as VM) }
         .firstOrNull { (_, item) -> findItem(item) }

      if (pair == null) {
         val (scope, item) = createItem()
         openItems[scope] = item
         fire(SelectMainViewTabEvent(scope))
      } else {
         val scope = pair.first
         updateExistingScope(scope)
         updateViewModel(pair.second)
         fire(SelectMainViewTabEvent(scope))
      }
   }

   fun importTileSet() {
      val vm = TileSetAssetDataVM()
      val scope = Scope()
      setInScope(vm, scope)

      find<ImportTileSetFragment>(scope).apply {
         onComplete {
            projectContext.importTileSet(it)
         }

         openModal(block = true, resizable = false)
      }
   }

   fun importImage() {
      val vm = ImageAssetDataVM()
      val scope = Scope()
      setInScope(vm, scope)

      find<ImportImageFragment>(scope).apply {
         onComplete {
            projectContext.importImage(it)
         }

         openModal(block = true, resizable = false)
      }
   }

   fun importEntitySet() {
      val vm = EntitySetAssetDataVM()
      val scope = Scope()
      setInScope(vm, scope)

      find<ImportEntitySetFragment>(scope).apply {
         onComplete {
            projectContext.importEntitySet(it)
         }

         openModal(block = true, resizable = false)
      }
   }

   fun importAnimation() {
      val vm = AnimationAssetDataVM()
      val scope = Scope()
      setInScope(vm, scope)

      find<ImportAnimationFragment>(scope).apply {
         onComplete {
            projectContext.importAnimation(it)
         }

         openModal(block = true, resizable = false)
      }
   }

   fun importFont() {
      val vm = FontAssetDataVM()
      val scope = Scope()
      setInScope(vm, scope)

      find<ImportFontFragment>(scope).apply {
         onComplete {
            projectContext.importFont(it)
         }

         openModal(block = true, resizable = false)
      }
   }

   fun createEmptyWidget() {
      TextInputDialog().apply {
         width = 300.0
         contentText = "Widget name"
         title = "New Widget"
      }
         .showAndWait()
         .map(::WidgetAssetData)
         .map(projectContext::createWidget)
         .ifPresent(this::openScript)
   }

   fun importSound() {
      val vm = SoundAssetDataVM()
      val scope = Scope()
      setInScope(vm, scope)

      find<ImportSoundFragment>(scope).apply {
         onComplete {
            projectContext.importSound(it)
         }

         openModal(block = true, resizable = true)
      }
   }

   fun closeAsset(asset: Asset) {
      when (asset) {
         is GameMapAsset -> openItems.entries.firstOrNull { (_, item) -> item is GameMapVM && item.uid == asset.uid }?.key?.let {
            openItems.remove(it)
         }

         is ScriptAssetFileNode -> closeScript(asset)
      }
   }


   fun closeScript(fsNode: FileNode) {
      openItems.entries.firstOrNull { (_, item) -> item is CodeVM && item.fileNode.absolutePath == fsNode.absolutePath }?.key?.let {
         openItems.remove(it)
      }

      fsNode.allChildren.forEach { child ->
         openItems.entries.firstOrNull { (_, item) -> item is CodeVM && item.fileNode.absolutePath == child.absolutePath }?.key?.let {
            openItems.remove(it)
         }
      }
   }

   fun clearResources() {
      openItems.clear()
   }
}