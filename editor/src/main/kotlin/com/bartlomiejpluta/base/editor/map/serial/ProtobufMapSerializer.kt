package com.bartlomiejpluta.base.editor.map.serial

import com.bartlomiejpluta.base.editor.map.model.enumeration.ImageLayerMode
import com.bartlomiejpluta.base.editor.map.model.enumeration.PassageAbility
import com.bartlomiejpluta.base.editor.map.model.layer.*
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
      protoMap.tileWidth = item.tileWidth.toInt()
      protoMap.tileHeight = item.tileHeight.toInt()
      protoMap.handler = item.handler

      item.layers.forEach { layer -> protoMap.addLayers(serializeLayer(layer)) }

      protoMap.build().writeTo(output)
   }

   private fun serializeLayer(layer: Layer): GameMapProto.Layer {
      return when (layer) {
         is TileLayer -> layer.layer.flatMap { it.asIterable() }
            .fold(GameMapProto.TileLayer.newBuilder()) { acc, tile -> acc.addTiles((tile?.id?.plus(1)) ?: 0) }
            .setTilesetUID(layer.tileSetAsset.uid)
            .build()
            .let { GameMapProto.Layer.newBuilder().setName(layer.name).setTileLayer(it).build() }

         is AutoTileLayer -> layer.layer.flatMap { it.asIterable() }
            .fold(GameMapProto.AutoTileLayer.newBuilder()) { acc, tile -> acc.addTiles(tile) }
            .setAutotileUID(layer.autoTileAsset.uid)
            .setAnimated(layer.animated)
            .setAnimationDuration(layer.animationDuration)
            .setConnect(layer.connect)
            .build()
            .let { GameMapProto.Layer.newBuilder().setName(layer.name).setAutoTileLayer(it).build() }

         is ObjectLayer -> layer.passageMap.flatMap { it.asIterable() }
            .fold(GameMapProto.ObjectLayer.newBuilder()) { acc, passage ->
               acc.addPassageMap(
                  when (passage) {
                     PassageAbility.ALLOW -> GameMapProto.PassageAbility.ALLOW
                     PassageAbility.BLOCK -> GameMapProto.PassageAbility.BLOCK
                  }
               )
            }
            .also { proto ->
               layer.objects.map {
                  proto.addObjects(GameMapProto.MapObject.newBuilder().apply {
                     x = it.x
                     y = it.y
                     code = it.code
                  })
               }
            }
            .setJavaImports(layer.javaImports)
            .build()
            .let { GameMapProto.Layer.newBuilder().setName(layer.name).setObjectLayer(it).build() }

         is ColorLayer -> GameMapProto.ColorLayer.newBuilder()
            .setRed(layer.red)
            .setGreen(layer.green)
            .setBlue(layer.blue)
            .setAlpha(layer.alpha)
            .build()
            .let { GameMapProto.Layer.newBuilder().setName(layer.name).setColorLayer(it).build() }

         is ImageLayer -> GameMapProto.ImageLayer.newBuilder()
            .setImageUID(layer.imageAsset.uid)
            .setOpacity(layer.opacity)
            .setX(layer.x)
            .setY(layer.y)
            .setScaleX(layer.scaleX)
            .setScaleY(layer.scaleY)
            .setMode(
               when (layer.mode!!) {
                  ImageLayerMode.NORMAL -> GameMapProto.ImageLayerMode.NORMAL
                  ImageLayerMode.FIT_MAP -> GameMapProto.ImageLayerMode.FIT_MAP
                  ImageLayerMode.FIT_SCREEN -> GameMapProto.ImageLayerMode.FIT_SCREEN
               }
            )
            .setParallax(layer.parallax)
            .build()
            .let { GameMapProto.Layer.newBuilder().setName(layer.name).setImageLayer(it).build() }


         else -> throw IllegalStateException("Not supported layer type")
      }
   }
}