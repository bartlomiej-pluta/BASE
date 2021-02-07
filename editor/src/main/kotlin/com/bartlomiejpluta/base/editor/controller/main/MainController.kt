package com.bartlomiejpluta.base.editor.controller.main

import com.bartlomiejpluta.base.editor.command.context.UndoableScope
import com.bartlomiejpluta.base.editor.model.map.map.GameMap
import com.bartlomiejpluta.base.editor.model.tileset.TileSet
import com.bartlomiejpluta.base.editor.view.map.MapSettingsFragment
import com.bartlomiejpluta.base.editor.viewmodel.map.GameMapVM
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

   val openMaps = observableMapOf<Scope, GameMap>()

   fun createEmptyMap() {
      val map = GameMap(tileset).apply { name = "Unnamed" }
      val scope = UndoableScope()
      val vm = GameMapVM(map)
      setInScope(vm, scope)

      val modal = find<MapSettingsFragment>(scope).apply { openModal(block = true, resizable = false) }

      if (modal.result) {
         openMaps[scope] = map
      }
   }
}