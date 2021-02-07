package com.bartlomiejpluta.base.editor.main.controller

import com.bartlomiejpluta.base.editor.command.context.UndoableScope
import com.bartlomiejpluta.base.editor.map.model.map.GameMap
import com.bartlomiejpluta.base.editor.map.view.MapSettingsFragment
import com.bartlomiejpluta.base.editor.map.viewmodel.GameMapVM
import com.bartlomiejpluta.base.editor.project.model.Project
import com.bartlomiejpluta.base.editor.project.view.ProjectSettingsFragment
import com.bartlomiejpluta.base.editor.project.viewmodel.ProjectVM
import com.bartlomiejpluta.base.editor.tileset.model.TileSet
import javafx.beans.property.SimpleObjectProperty
import org.springframework.stereotype.Component
import tornadofx.Controller
import tornadofx.Scope
import tornadofx.find
import tornadofx.observableMapOf
import kotlin.collections.set

@Component
class MainController : Controller() {
   // In the future it'll be pulled from TileSetService or something like that
   private val tileset = TileSet(resources.image("/textures/tileset.png"), 160, 8)

   val openProject = SimpleObjectProperty<Project?>()
   val openMaps = observableMapOf<Scope, GameMap>()

   fun createEmptyProject() {
      val project = Project()
      val vm = ProjectVM(project)

      setInScope(vm)
      val modal = find<ProjectSettingsFragment>().apply { openModal(block = true, resizable = false) }

      if(modal.result) {
         openProject.value = project
      }
   }

   fun createEmptyMap() {
      val map = GameMap(tileset)
      val scope = UndoableScope()
      val vm = GameMapVM(map)
      setInScope(vm, scope)

      val modal = find<MapSettingsFragment>(scope).apply { openModal(block = true, resizable = false) }

      if (modal.result) {
         openMaps[scope] = map
      }
   }
}