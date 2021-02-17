package com.bartlomiejpluta.base.editor.map.serial

import com.bartlomiejpluta.base.editor.map.model.layer.ImageLayer
import com.bartlomiejpluta.base.editor.map.model.layer.Layer
import com.bartlomiejpluta.base.editor.map.model.layer.ObjectLayer
import com.bartlomiejpluta.base.editor.map.model.layer.TileLayer
import com.bartlomiejpluta.base.editor.map.model.map.GameMap
import com.bartlomiejpluta.base.editor.project.context.ProjectContext
import com.bartlomiejpluta.base.editor.tileset.model.Tile
import com.bartlomiejpluta.base.editor.tileset.model.TileSet
import com.bartlomiejpluta.base.proto.GameMapProto
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.io.InputStream

@Component
class ProtobufMapDeserializer : MapDeserializer {

   @Autowired
   private lateinit var projectContext: ProjectContext

   override fun deserialize(input: InputStream): GameMap {
      val proto = GameMapProto.GameMap.parseFrom(input)
      val tileSet = projectContext.loadTileSet(proto.tileSetUID)
      val map = GameMap(tileSet)
      map.uid = proto.uid
      map.rows = proto.rows
      map.columns = proto.columns

      proto.layersList.forEach {
         map.layers.add(deserializeLayer(map.rows, map.columns, tileSet, it))
      }

      return map
   }

   private fun deserializeLayer(rows: Int, columns: Int, tileSet: TileSet, proto: GameMapProto.Layer): Layer {
      return when {
         proto.hasTileLayer() -> deserializeTileLayer(rows, columns, tileSet, proto)
         proto.hasObjectLayer() -> deserializeObjectLayer(proto)
         proto.hasImageLayer() -> deserializeImageLayer(proto)

         else -> throw IllegalStateException("Not supported layer type")
      }
   }

   private fun deserializeTileLayer(rows: Int, columns: Int, tileSet: TileSet, proto: GameMapProto.Layer): Layer {
      val layer: Array<Array<Tile?>> = Array(rows) { Array(columns) { null } }

      proto.tileLayer.tilesList.forEachIndexed { index, tile ->
         layer[index / columns][index % columns] = when (tile) {
            0 -> null
            else -> tileSet.getTile(tile - 1)
         }
      }

      return TileLayer(proto.name, rows, columns, layer)
   }

   private fun deserializeObjectLayer(proto: GameMapProto.Layer): Layer {
      return ObjectLayer(proto.name)
   }

   private fun deserializeImageLayer(proto: GameMapProto.Layer): Layer {
      return ImageLayer(proto.name)
   }
}