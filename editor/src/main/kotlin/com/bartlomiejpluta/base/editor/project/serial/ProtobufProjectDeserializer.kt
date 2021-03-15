package com.bartlomiejpluta.base.editor.project.serial

import com.bartlomiejpluta.base.editor.entityset.asset.EntitySet
import com.bartlomiejpluta.base.editor.gui.font.asset.FontAsset
import com.bartlomiejpluta.base.editor.gui.widget.asset.WidgetAsset
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
      project.runner = proto.runner
      project.maps.addAll(proto.mapsList.map { deserializeMap(project, it) })
      project.tileSets.addAll(proto.tileSetsList.map { deserializeTileSet(project, it) })
      project.images.addAll(proto.imagesList.map { deserializeImage(project, it) })
      project.entitySets.addAll(proto.entitySetsList.map { deserializeEntitySet(project, it) })
      project.fonts.addAll(proto.fontsList.map { deserializeFont(project, it) })
      project.widgets.addAll(proto.widgetsList.map { deserializeWidget(project, it) })

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

   private fun deserializeEntitySet(project: Project, entitySetAsset: ProjectProto.EntitySetAsset) = EntitySet(
      project = project,
      uid = entitySetAsset.uid,
      source = entitySetAsset.source,
      name = entitySetAsset.name,
      rows = entitySetAsset.rows,
      columns = entitySetAsset.columns
   )

   private fun deserializeFont(project: Project, fontAsset: ProjectProto.FontAsset) = FontAsset(
      project = project,
      uid = fontAsset.uid,
      source = fontAsset.source,
      name = fontAsset.name
   )

   private fun deserializeWidget(project: Project, widget: ProjectProto.WidgetAsset) = WidgetAsset(
      project = project,
      uid = widget.uid,
      name = widget.name
   )
}
