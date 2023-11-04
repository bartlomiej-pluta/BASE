package com.bartlomiejpluta.base.editor.code.build.asset

import com.bartlomiejpluta.base.editor.map.asset.GameMapAsset
import com.bartlomiejpluta.base.editor.map.serial.BinaryMapSerializer
import com.bartlomiejpluta.base.editor.project.context.ProjectContext
import com.bartlomiejpluta.base.editor.project.model.Project
import com.bartlomiejpluta.base.editor.project.serial.BinaryProjectSerializer
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class DefaultAssetSerializer : AssetSerializer {

   @Autowired
   private lateinit var projectContext: ProjectContext

   @Autowired
   private lateinit var projectSerializer: BinaryProjectSerializer

   @Autowired
   private lateinit var mapSerializer: BinaryMapSerializer

   override fun serializeAssets(project: Project) {
      project.buildAssetsDir.mkdirs()
      serializeProject(project)

      project.buildAssetsMapsDir.mkdirs()
      serializeMaps(project)
   }

   private fun serializeProject(project: Project) {
      project.binaryProjectFile.outputStream().use {
         projectSerializer.serialize(project, it)
      }
   }

   private fun serializeMaps(project: Project) = project.maps.forEach {
      serializeMap(it)
   }

   private fun serializeMap(asset: GameMapAsset) {
      val map = projectContext.loadMap(asset.uid)
      asset.binaryFile.outputStream().use {
         mapSerializer.serialize(map, it)
      }
   }
}