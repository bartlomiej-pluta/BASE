package com.bartlomiejpluta.base.editor.main.controller

import com.bartlomiejpluta.base.editor.animation.view.importing.ImportAnimationFragment
import com.bartlomiejpluta.base.editor.animation.viewmodel.AnimationAssetDataVM
import com.bartlomiejpluta.base.editor.asset.model.Asset
import com.bartlomiejpluta.base.editor.asset.model.GraphicAsset
import com.bartlomiejpluta.base.editor.asset.view.select.SelectGraphicAssetFragment
import com.bartlomiejpluta.base.editor.asset.viewmodel.GraphicAssetVM
import com.bartlomiejpluta.base.editor.audio.view.importing.ImportSoundFragment
import com.bartlomiejpluta.base.editor.audio.viewmodel.SoundAssetDataVM
import com.bartlomiejpluta.base.editor.autotile.asset.AutoTileAsset
import com.bartlomiejpluta.base.editor.autotile.view.importing.ImportAutoTileFragment
import com.bartlomiejpluta.base.editor.autotile.viewmodel.AutoTileAssetDataVM
import com.bartlomiejpluta.base.editor.characterset.view.importing.ImportCharacterSetFragment
import com.bartlomiejpluta.base.editor.characterset.viewmodel.CharacterSetAssetDataVM
import com.bartlomiejpluta.base.editor.code.model.CodeScope
import com.bartlomiejpluta.base.editor.code.viewmodel.CodeVM
import com.bartlomiejpluta.base.editor.command.context.UndoableScope
import com.bartlomiejpluta.base.editor.database.model.data.Query
import com.bartlomiejpluta.base.editor.database.viewmodel.QueryVM
import com.bartlomiejpluta.base.editor.event.SelectMainViewTabEvent
import com.bartlomiejpluta.base.editor.file.model.FileNode
import com.bartlomiejpluta.base.editor.file.model.ScriptAssetFileNode
import com.bartlomiejpluta.base.editor.gui.font.view.importing.ImportFontFragment
import com.bartlomiejpluta.base.editor.gui.font.viewmodel.FontAssetDataVM
import com.bartlomiejpluta.base.editor.gui.widget.asset.WidgetAssetData
import com.bartlomiejpluta.base.editor.iconset.view.importing.ImportIconSetFragment
import com.bartlomiejpluta.base.editor.iconset.viewmodel.IconSetAssetDataVM
import com.bartlomiejpluta.base.editor.image.view.importing.ImportImageFragment
import com.bartlomiejpluta.base.editor.image.viewmodel.ImageAssetDataVM
import com.bartlomiejpluta.base.editor.map.asset.GameMapAsset
import com.bartlomiejpluta.base.editor.map.model.map.GameMap
import com.bartlomiejpluta.base.editor.map.view.wizard.MapCreationFragment
import com.bartlomiejpluta.base.editor.map.view.wizard.MapImportFragment
import com.bartlomiejpluta.base.editor.map.viewmodel.GameMapBuilderVM
import com.bartlomiejpluta.base.editor.map.viewmodel.GameMapVM
import com.bartlomiejpluta.base.editor.project.context.ProjectContext
import com.bartlomiejpluta.base.editor.project.model.Project
import com.bartlomiejpluta.base.editor.project.view.ProjectSettingsFragment
import com.bartlomiejpluta.base.editor.project.viewmodel.ProjectVM
import com.bartlomiejpluta.base.editor.tileset.asset.TileSetAsset
import com.bartlomiejpluta.base.editor.tileset.view.importing.ImportTileSetFragment
import com.bartlomiejpluta.base.editor.tileset.viewmodel.TileSetAssetDataVM
import javafx.scene.control.TextInputDialog
import javafx.stage.FileChooser
import javafx.stage.StageStyle
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

      find<MapCreationFragment>(scope).apply {
         onComplete {
            val map = GameMap(vm.tileWidth.toDouble(), vm.tileHeight.toDouble()).apply {
               rows = vm.rows
               columns = vm.columns
               handler = vm.handler
            }
            projectContext.importMap(vm.name, vm.handlerBaseClass?.takeIf { it.isNotEmpty() }, map)
            openItems[scope] = GameMapVM(map)
         }

         openModal(block = true, resizable = false)
      }
   }

   fun importMap() {
      val scope = UndoableScope()
      val vm = GameMapBuilderVM()
      setInScope(vm, scope)
      find<MapImportFragment>(scope).apply {
         onComplete {
            val map = projectContext.importMapFromFile(
               vm.name,
               vm.handler,
               vm.handlerBaseClass?.takeIf { it.isNotEmpty() },
               File(vm.file),
               ::askForNewTileSetAsset,
               ::askForNewAutoTileAsset
            )
            openItems[scope] = GameMapVM(map)
         }

         openModal(block = true, resizable = false)
      }
   }

   private fun askForNewTileSetAsset(name: String, uid: String): String {
      var newUid = ""

      find<SelectGraphicAssetFragment<TileSetAsset>>(
         Scope(),
         SelectGraphicAssetFragment<TileSetAsset>::assets to projectContext.project?.tileSets!!,
         SelectGraphicAssetFragment<TileSetAsset>::comment to "You are importing a tile layer which originally was defined\nwith an other Tile Set asset data with UID: [$uid].\nPlease select asset you would like to apply for layer [$name].\n".toProperty(),
         SelectGraphicAssetFragment<TileSetAsset>::cancelable to false.toProperty()
      ).apply {
         title = "Select Tile Set for layer $name"

         onComplete {
            newUid = it.uid
         }

         openModal(block = true, resizable = false, stageStyle = StageStyle.UNDECORATED)
      }

      return newUid
   }

   private fun askForNewAutoTileAsset(name: String, uid: String): String {
      var newUid = ""

      find<SelectGraphicAssetFragment<AutoTileAsset>>(
         Scope(),
         SelectGraphicAssetFragment<AutoTileAsset>::assets to projectContext.project?.autoTiles!!,
         SelectGraphicAssetFragment<AutoTileAsset>::comment to "You are importing a tile layer which originally was defined\nwith an other Auto Tile asset data with UID: [$uid].\nPlease select asset you would like to apply for layer [$name].\n".toProperty(),
         SelectGraphicAssetFragment<AutoTileAsset>::cancelable to false.toProperty()
      ).apply {
         title = "Select Auto Tile for layer $name"

         onComplete {
            newUid = it.uid
         }

         openModal(block = true, resizable = false, stageStyle = StageStyle.UNDECORATED)
      }

      return newUid
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
      fsNode: FileNode, line: Int = 1, column: Int = 1, execute: ((String) -> Unit)? = null, saveable: Boolean = true
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

   fun openGraphicAsset(asset: GraphicAsset) {
      val findItem = { a: GraphicAssetVM -> a.fileProperty.value.absolutePath == asset.file.absolutePath }
      openItem<GraphicAsset, GraphicAssetVM, Scope>(findItem) {
         val vm = GraphicAssetVM(asset)
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
      @Suppress("UNCHECKED_CAST") val pair =
         openItems.entries.filter { (_, item) -> item is VM }.map { (scope, item) -> (scope as S) to (item as VM) }
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

   fun importAutoTile() {
      val vm = AutoTileAssetDataVM()
      val scope = Scope()
      setInScope(vm, scope)

      find<ImportAutoTileFragment>(scope).apply {
         onComplete {
            projectContext.importAutoTile(it)
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

   fun importCharacterSet() {
      val vm = CharacterSetAssetDataVM()
      val scope = Scope()
      setInScope(vm, scope)

      find<ImportCharacterSetFragment>(scope).apply {
         onComplete {
            projectContext.importCharacterSet(it)
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

   fun importIconSet() {
      val vm = IconSetAssetDataVM()
      val scope = Scope()
      setInScope(vm, scope)

      find<ImportIconSetFragment>(scope).apply {
         onComplete {
            projectContext.importIconSet(it)
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
      }.showAndWait().map(::WidgetAssetData).map(projectContext::createWidget).ifPresent(this::openScript)
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