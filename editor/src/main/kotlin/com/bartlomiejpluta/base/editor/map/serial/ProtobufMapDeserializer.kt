package com.bartlomiejpluta.base.editor.map.serial

import com.bartlomiejpluta.base.editor.map.model.enumeration.ImageLayerMode
import com.bartlomiejpluta.base.editor.map.model.enumeration.PassageAbility
import com.bartlomiejpluta.base.editor.map.model.layer.*
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
      map.handler = proto.handler

      proto.layersList.forEach {
         map.layers.add(deserializeLayer(map.rows, map.columns, tileSet, it))
      }

      return map
   }

   private fun deserializeLayer(rows: Int, columns: Int, tileSet: TileSet, proto: GameMapProto.Layer): Layer {
      return when {
         proto.hasTileLayer() -> deserializeTileLayer(rows, columns, tileSet, proto)
         proto.hasObjectLayer() -> deserializeObjectLayer(rows, columns, proto)
         proto.hasColorLayer() -> deserializeColorLayer(proto)
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

   private fun deserializeObjectLayer(rows: Int, columns: Int, proto: GameMapProto.Layer): Layer {
      val passageMap: Array<Array<PassageAbility>> = Array(rows) { Array(columns) { PassageAbility.ALLOW } }

      proto.objectLayer.passageMapList.forEachIndexed { index, passage ->
         passageMap[index / columns][index % columns] = when (passage!!) {
            GameMapProto.PassageAbility.ALLOW -> PassageAbility.ALLOW
            GameMapProto.PassageAbility.BLOCK -> PassageAbility.BLOCK
         }
      }

      return ObjectLayer(proto.name, rows, columns, passageMap)
   }

   private fun deserializeColorLayer(proto: GameMapProto.Layer): Layer {
      return ColorLayer(
         name = proto.name,
         red = proto.colorLayer.red,
         green = proto.colorLayer.green,
         blue = proto.colorLayer.blue,
         alpha = proto.colorLayer.alpha
      )
   }

   private fun deserializeImageLayer(proto: GameMapProto.Layer): Layer {
      return ImageLayer(
         name = proto.name,
         imageAsset = projectContext.findImageAsset(proto.imageLayer.imageUID),
         opacity = proto.imageLayer.opacity,
         x = proto.imageLayer.x,
         y = proto.imageLayer.y,
         scaleX = proto.imageLayer.scaleX,
         scaleY = proto.imageLayer.scaleY,
         mode = when (proto.imageLayer.mode!!) {
            GameMapProto.ImageLayerMode.NORMAL -> ImageLayerMode.NORMAL
            GameMapProto.ImageLayerMode.FIT_MAP -> ImageLayerMode.FIT_MAP
            GameMapProto.ImageLayerMode.FIT_SCREEN -> ImageLayerMode.FIT_SCREEN
         },
         parallax = proto.imageLayer.parallax
      )
   }
}
