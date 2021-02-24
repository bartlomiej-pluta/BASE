package com.bartlomiejpluta.base.editor.main.controller

import com.bartlomiejpluta.base.editor.asset.model.Asset
import com.bartlomiejpluta.base.editor.code.model.Code
import com.bartlomiejpluta.base.editor.code.model.FileSystemNode
import com.bartlomiejpluta.base.editor.code.viewmodel.CodeVM
import com.bartlomiejpluta.base.editor.command.context.UndoableScope
import com.bartlomiejpluta.base.editor.image.view.importing.ImportImageFragment
import com.bartlomiejpluta.base.editor.image.viewmodel.ImageAssetDataVM
import com.bartlomiejpluta.base.editor.map.asset.GameMapAsset
import com.bartlomiejpluta.base.editor.map.model.map.GameMap
import com.bartlomiejpluta.base.editor.map.view.wizard.MapCreationWizard
import com.bartlomiejpluta.base.editor.map.viewmodel.GameMapBuilderVM
import com.bartlomiejpluta.base.editor.map.viewmodel.GameMapVM
import com.bartlomiejpluta.base.editor.project.context.ProjectContext
import com.bartlomiejpluta.base.editor.project.model.Project
import com.bartlomiejpluta.base.editor.project.view.ProjectSettingsFragment
import com.bartlomiejpluta.base.editor.project.viewmodel.ProjectVM
import com.bartlomiejpluta.base.editor.tileset.view.importing.ImportTileSetFragment
import com.bartlomiejpluta.base.editor.tileset.viewmodel.TileSetAssetDataVM
import javafx.stage.FileChooser
import org.springframework.stereotype.Component
import tornadofx.*
import kotlin.collections.set

@Component
class MainController : Controller() {
   private val projectContext: ProjectContext by di()

   val openItems = observableMapOf<Scope, Any>()

   fun createEmptyProject() {
      val vm = ProjectVM(Project())

      setInScope(vm)
      find<ProjectSettingsFragment>().apply {
         onComplete {
            openItems.clear()
            projectContext.project = it
            projectContext.save()
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
            }
            projectContext.importMap(vm.name, map)
            openItems[scope] = map
         }

         openModal(block = true, resizable = false)
      }
   }

   fun openProject() {
      chooseFile(
         title = "Load Project",
         filters = arrayOf(FileChooser.ExtensionFilter("BASE Editor Project (*.bep)", "*.bep")),
      ).getOrNull(0)?.let {
         openItems.clear()
         projectContext.open(it)
      }
   }

   fun openMap(uid: String) {
      if (openItems.count { (_, item) -> item is GameMap && item.uid == uid } == 0) {
         val map = projectContext.loadMap(uid)
         val vm = GameMapVM(map)
         val scope = UndoableScope()
         setInScope(vm, scope)

         openItems[scope] = map
      }
   }

   fun openScript(fsNode: FileSystemNode) {
      if (openItems.count { (_, item) -> item is Code && item.file.absolutePath == fsNode.file.absolutePath } == 0) {
         val code = projectContext.loadScript(fsNode.fileProperty)
         val vm = CodeVM(code)
         val scope = UndoableScope()
         setInScope(vm, scope)

         openItems[scope] = code
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

   fun closeScript(fsNode: FileSystemNode) {
      openItems.entries.firstOrNull { (_, item) -> item is Code && item.file.absolutePath == fsNode.file.absolutePath }?.key?.let {
         openItems.remove(it)
      }

      fsNode.allChildren.forEach { child ->
         openItems.entries.firstOrNull { (_, item) -> item is Code && item.file.absolutePath == child.file.absolutePath }?.key?.let {
            openItems.remove(it)
         }
      }
   }

   fun closeAsset(asset: Asset) {
      when (asset) {
         is GameMapAsset -> openItems.entries.firstOrNull { (_, item) -> item is GameMap && item.uid == asset.uid }?.key?.let {
            openItems.remove(it)
         }
      }
   }
}