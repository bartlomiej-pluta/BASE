package com.bartlomiejpluta.base.editor.project.serial

import com.bartlomiejpluta.base.editor.image.asset.ImageAsset
import com.bartlomiejpluta.base.editor.map.asset.GameMapAsset
import com.bartlomiejpluta.base.editor.project.model.Project
import com.bartlomiejpluta.base.editor.tileset.asset.TileSetAsset
import com.bartlomiejpluta.base.proto.ProjectProto
import org.springframework.stereotype.Component
import java.io.InputStream

@Component
class ProtobufProjectDeserializer : ProjectDeserializer {

   override fun deserialize(input: InputStream): Project {
      val proto = ProjectProto.Project.parseFrom(input)
      val project = Project()
      project.name = proto.name
      project.maps.addAll(proto.mapsList.map { deserializeMap(project, it) })
      project.tileSets.addAll(proto.tileSetsList.map { deserializeTileSet(project, it) })
      project.images.addAll(proto.imagesList.map { deserializeImage(project, it) })

      return project
   }

   private fun deserializeMap(project: Project, map: ProjectProto.GameMapAsset) = GameMapAsset(
      project = project,
      uid = map.uid,
      name = map.name
   )

   private fun deserializeTileSet(project: Project, tileSet: ProjectProto.TileSetAsset) = TileSetAsset(
      project = project,
      uid = tileSet.uid,
      source = tileSet.source,
      name = tileSet.name,
      rows = tileSet.rows,
      columns = tileSet.columns
   )

   private fun deserializeImage(project: Project, image: ProjectProto.ImageAsset) = ImageAsset(
      project = project,
      uid = image.uid,
      source = image.source,
      name = image.name
   )
}