package com.bartlomiejpluta.base.editor.map.serial

import com.bartlomiejpluta.base.editor.map.model.layer.Layer
import com.bartlomiejpluta.base.editor.map.model.layer.TileLayer
import com.bartlomiejpluta.base.editor.map.model.map.GameMap
import com.bartlomiejpluta.base.editor.tileset.model.Tile
import com.bartlomiejpluta.base.editor.tileset.model.TileSet
import com.bartlomiejpluta.base.proto.GameMapProto
import org.springframework.stereotype.Component
import tornadofx.ResourceLookup
import java.io.InputStream

@Component
class ProtobufMapDeserializer : MapDeserializer {
   private val resources = ResourceLookup(this)
   private val tileset = TileSet("Test TileSet", resources.image("/textures/tileset.png"), 160, 8)

   override fun deserialize(input: InputStream): GameMap {
      val map = GameMap(tileset)
      val proto = GameMapProto.GameMap.parseFrom(input)
      map.uid = proto.uid
      map.rows = proto.rows
      map.columns = proto.columns

      proto.layersList.forEach {
         map.layers.add(deserializeLayer(map.rows, map.columns, tileset, it))
      }

      return map
   }

   private fun deserializeLayer(rows: Int, columns: Int, tileSet: TileSet, proto: GameMapProto.Layer): Layer {
      return when {
         proto.hasTileLayer() -> deserializeTileLayer(rows, columns, tileSet, proto)

         else -> throw IllegalStateException("Not supported layer type")
      }
   }

   private fun deserializeTileLayer(rows: Int, columns: Int, tileSet: TileSet, proto: GameMapProto.Layer): Layer {
      val layer: Array<Array<Tile?>> = Array(rows) { Array(columns) { null } }

      proto.tileLayer.tilesList.forEachIndexed { index, tile ->
         layer[index / columns][index % columns] = when(tile) {
            0 -> null
            else -> tileSet.getTile(tile-1)
         }
      }

      return TileLayer(proto.name, rows, columns, layer)
   }
}