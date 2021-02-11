package com.bartlomiejpluta.base.editor.project.serial

import com.bartlomiejpluta.base.editor.map.asset.GameMapAsset
import com.bartlomiejpluta.base.editor.project.model.Project
import com.bartlomiejpluta.base.proto.ProjectProto
import org.springframework.stereotype.Component
import java.io.File
import java.io.InputStream

@Component
class ProtobufProjectDeserializer : ProjectDeserializer {

   override fun deserialize(input: InputStream): Project {
      val proto = ProjectProto.Project.parseFrom(input)
      val project = Project()
      project.name = proto.name
      project.maps.putAll(proto.mapsList.map { it.uid to deserializeMap(it) })

      return project
   }

   private fun deserializeMap(map: ProjectProto.GameMapAsset) = GameMapAsset(
      uid = map.uid,
      name = map.name,
      rows = map.rows,
      columns = map.columns
   )
}