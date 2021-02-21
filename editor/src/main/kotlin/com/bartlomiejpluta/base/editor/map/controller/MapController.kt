package com.bartlomiejpluta.base.editor.map.controller

import com.bartlomiejpluta.base.editor.map.model.map.GameMap
import com.bartlomiejpluta.base.editor.project.context.ProjectContext
import javafx.scene.image.Image
import org.springframework.stereotype.Component
import tornadofx.Controller

@Component
class MapController : Controller() {
   private val projectContext: ProjectContext by di()

   fun saveMap(map: GameMap, image: Image) {
      projectContext.saveMap(map, image)
   }
}