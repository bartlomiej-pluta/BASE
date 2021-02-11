package com.bartlomiejpluta.base.editor.main.controller

import com.bartlomiejpluta.base.editor.command.context.UndoableScope
import com.bartlomiejpluta.base.editor.map.model.map.GameMap
import com.bartlomiejpluta.base.editor.map.view.wizard.MapCreationWizard
import com.bartlomiejpluta.base.editor.map.viewmodel.GameMapBuilderVM
import com.bartlomiejpluta.base.editor.project.context.ProjectContext
import com.bartlomiejpluta.base.editor.project.model.Project
import com.bartlomiejpluta.base.editor.project.view.ProjectSettingsFragment
import com.bartlomiejpluta.base.editor.project.viewmodel.ProjectVM
import javafx.stage.FileChooser
import org.springframework.stereotype.Component
import tornadofx.*
import kotlin.collections.set

@Component
class MainController : Controller() {
   private val projectContext: ProjectContext by di()

   val openMaps = observableMapOf<Scope, GameMap>()

   fun createEmptyProject() {
      val project = Project()
      val vm = ProjectVM(project)

      setInScope(vm)
      val modal = find<ProjectSettingsFragment>().apply { openModal(block = true, resizable = false) }

      if (modal.result) {
         projectContext.project = project
         projectContext.save()
      }
   }

   fun createEmptyMap() {
      val scope = UndoableScope()
      val vm = GameMapBuilderVM()
      setInScope(vm, scope)

      find<MapCreationWizard>(scope).apply {
         onComplete {
            vm.item.build().let { map ->
               projectContext.importMap(vm.name, map)
               openMaps[scope] = map
            }
         }

         openModal(block = true)
      }
   }

   fun loadProject() {
      chooseFile(
         title = "Load Project",
         filters = arrayOf(FileChooser.ExtensionFilter("BASE Editor Project (*.bep)", "*.bep")),
      ).getOrNull(0)?.let { projectContext.open(it) }
   }
}