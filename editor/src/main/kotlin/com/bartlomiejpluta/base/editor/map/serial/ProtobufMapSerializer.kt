package com.bartlomiejpluta.base.editor.map.serial

import com.bartlomiejpluta.base.editor.map.model.layer.Layer
import com.bartlomiejpluta.base.editor.map.model.layer.TileLayer
import com.bartlomiejpluta.base.editor.map.model.map.GameMap
import com.bartlomiejpluta.base.proto.GameMapProto
import org.springframework.stereotype.Component
import java.io.OutputStream

@Component
class ProtobufMapSerializer : MapSerializer {

   override fun serialize(item: GameMap, output: OutputStream) {
      val protoMap = GameMapProto.GameMap.newBuilder()
      protoMap.uid = item.uid
      protoMap.rows = item.rows
      protoMap.columns = item.columns
      protoMap.tileSetUID = item.tileSet.uid

      item.layers.forEach { layer -> protoMap.addLayers(serializeLayer(layer)) }

      protoMap.build().writeTo(output)
   }

   private fun serializeLayer(layer: Layer): GameMapProto.Layer {
      return when (layer) {
         is TileLayer -> layer.layer.flatMap { it.asIterable() }
            .fold(GameMapProto.TileLayer.newBuilder()) { acc, tile -> acc.addTiles((tile?.id?.plus(1)) ?: 0) }
            .build()
            .let { GameMapProto.Layer.newBuilder().setName(layer.name).setTileLayer(it).build() }

         else -> throw IllegalStateException("Not supported layer type")
      }
   }
}