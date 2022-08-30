package com.bartlomiejpluta.base.editor.map.serial

import com.bartlomiejpluta.base.editor.map.model.enumeration.ImageLayerMode
import com.bartlomiejpluta.base.editor.map.model.enumeration.PassageAbility
import com.bartlomiejpluta.base.editor.map.model.layer.*
import com.bartlomiejpluta.base.editor.map.model.map.GameMap
import com.bartlomiejpluta.base.editor.map.model.obj.MapObject
import com.bartlomiejpluta.base.editor.project.context.ProjectContext
import com.bartlomiejpluta.base.editor.tileset.model.Tile
import com.bartlomiejpluta.base.proto.GameMapProto
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationContext
import org.springframework.stereotype.Component
import java.io.InputStream

@Component
class ProtobufMapDeserializer : MapDeserializer {

   @Autowired
   private lateinit var appContext: ApplicationContext

   private val projectContext: ProjectContext by lazy {
      appContext.getBean(ProjectContext::class.java)
   }

   override fun deserialize(input: InputStream) = deserialize(input, { _, uid -> uid }, { _, uid -> uid })

   override fun deserialize(
      input: InputStream,
      replaceTileSet: (String, String) -> String,
      replaceAutoTile: (String, String) -> String
   ): GameMap {
      val proto = GameMapProto.GameMap.parseFrom(input)
      val map = GameMap(proto.tileWidth.toDouble(), proto.tileHeight.toDouble())
      map.uid = proto.uid
      map.rows = proto.rows
      map.columns = proto.columns
      map.handler = proto.handler

      proto.layersList
         .filter { it.hasTileLayer() || it.hasAutoTileLayer() || it.hasObjectLayer() || it.hasColorLayer() }
         .forEach { map.layers.add(deserializeLayer(map.rows, map.columns, it, replaceTileSet, replaceAutoTile)) }

      return map
   }

   private fun deserializeLayer(
      rows: Int,
      columns: Int,
      proto: GameMapProto.Layer,
      replaceTileSet: (String, String) -> String,
      replaceAutoTile: (String, String) -> String
   ): Layer {
      return when {
         proto.hasTileLayer() -> deserializeTileLayer(rows, columns, proto, replaceTileSet)
         proto.hasAutoTileLayer() -> deserializeAutoTileLayer(rows, columns, proto, replaceAutoTile)
         proto.hasObjectLayer() -> deserializeObjectLayer(rows, columns, proto)
         proto.hasColorLayer() -> deserializeColorLayer(proto)
         proto.hasImageLayer() -> deserializeImageLayer(proto)

         else -> throw IllegalStateException("Not supported layer type")
      }
   }

   private fun deserializeTileLayer(
      rows: Int,
      columns: Int,
      proto: GameMapProto.Layer,
      replaceTileSet: (String, String) -> String
   ): Layer {
      val layer: Array<Array<Tile?>> = Array(rows) { Array(columns) { null } }
      val tileSetAsset = projectContext.findTileSetAsset(replaceTileSet(proto.name, proto.tileLayer.tilesetUID))
      val tileSet = projectContext.loadTileSet(tileSetAsset.uid)

      proto.tileLayer.tilesList.forEachIndexed { index, tile ->
         layer[index / columns][index % columns] = when (tile) {
            0 -> null
            else -> tileSet.getTile(tile - 1)
         }
      }

      return TileLayer(proto.name, rows, columns, tileSetAsset, layer)
   }

   private fun deserializeAutoTileLayer(
      rows: Int,
      columns: Int,
      proto: GameMapProto.Layer,
      replaceTileSet: (String, String) -> String
   ): AutoTileLayer {
      val layer: Array<Array<Int>> = Array(rows) { Array(columns) { 0 } }
      val autoTile = projectContext.findAutoTileAsset(replaceTileSet(proto.name, proto.autoTileLayer.autotileUID))

      proto.autoTileLayer.tilesList.forEachIndexed { index, tile ->
         layer[index / columns][index % columns] = tile
      }

      return AutoTileLayer(
         proto.name,
         rows,
         columns,
         autoTile,
         proto.autoTileLayer.animated,
         proto.autoTileLayer.animationDuration,
         proto.autoTileLayer.connect,
         layer
      )
   }

   private fun deserializeObjectLayer(rows: Int, columns: Int, proto: GameMapProto.Layer): Layer {
      val passageMap: Array<Array<PassageAbility>> = Array(rows) { Array(columns) { PassageAbility.ALLOW } }

      proto.objectLayer.passageMapList.forEachIndexed { index, passage ->
         passageMap[index / columns][index % columns] = when (passage!!) {
            GameMapProto.PassageAbility.ALLOW -> PassageAbility.ALLOW
            GameMapProto.PassageAbility.BLOCK -> PassageAbility.BLOCK
         }
      }

      val objects = proto.objectLayer.objectsList.map {
         MapObject(it.x, it.y, it.code)
      }

      return ObjectLayer(proto.name, rows, columns, objects, passageMap)
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
