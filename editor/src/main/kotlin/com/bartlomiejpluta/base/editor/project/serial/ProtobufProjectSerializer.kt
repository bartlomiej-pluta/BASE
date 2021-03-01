package com.bartlomiejpluta.base.editor.project.serial

import com.bartlomiejpluta.base.editor.entityset.asset.EntitySet
import com.bartlomiejpluta.base.editor.image.asset.ImageAsset
import com.bartlomiejpluta.base.editor.map.asset.GameMapAsset
import com.bartlomiejpluta.base.editor.project.model.Project
import com.bartlomiejpluta.base.editor.tileset.asset.TileSetAsset
import com.bartlomiejpluta.base.proto.ProjectProto
import org.springframework.stereotype.Component
import java.io.OutputStream

@Component
class ProtobufProjectSerializer : ProjectSerializer {

   override fun serialize(item: Project, output: OutputStream) {
      val proto = ProjectProto.Project.newBuilder()
      proto.name = item.name
      proto.runner = item.runner
      proto.addAllMaps(item.maps.map(this::serializeMap))
      proto.addAllTileSets(item.tileSets.map(this::serializeTileSet))
      proto.addAllImages(item.images.map(this::serializeImage))
      proto.addAllEntitySets(item.entitySets.map(this::serializeEntitySet))
      proto.build().writeTo(output)
   }

   private fun serializeMap(map: GameMapAsset) = ProjectProto.GameMapAsset.newBuilder()
      .setUid(map.uid)
      .setSource(map.source)
      .setName(map.name)
      .build()

   private fun serializeTileSet(tileSet: TileSetAsset) = ProjectProto.TileSetAsset.newBuilder()
      .setUid(tileSet.uid)
      .setSource(tileSet.source)
      .setName(tileSet.name)
      .setRows(tileSet.rows)
      .setColumns(tileSet.columns)
      .build()

   private fun serializeImage(image: ImageAsset) = ProjectProto.ImageAsset.newBuilder()
      .setUid(image.uid)
      .setSource(image.source)
      .setName(image.name)
      .build()

   private fun serializeEntitySet(entitySet: EntitySet) = ProjectProto.EntitySetAsset.newBuilder()
      .setUid(entitySet.uid)
      .setSource(entitySet.source)
      .setName(entitySet.name)
      .setRows(entitySet.rows)
      .setColumns(entitySet.columns)
      .build()
}