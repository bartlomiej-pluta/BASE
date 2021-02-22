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

         is ObjectLayer -> layer.passageMap.flatMap { it.asIterable() }
            .fold(GameMapProto.ObjectLayer.newBuilder()) { acc, passage ->
               acc.addPassageMap(
                  when (passage) {
                     PassageAbility.ALLOW -> GameMapProto.PassageAbility.ALLOW
                     PassageAbility.BLOCK -> GameMapProto.PassageAbility.BLOCK
                     PassageAbility.UP_ONLY -> GameMapProto.PassageAbility.UP_ONLY
                     PassageAbility.DOWN_ONLY -> GameMapProto.PassageAbility.DOWN_ONLY
                     PassageAbility.LEFT_ONLY -> GameMapProto.PassageAbility.LEFT_ONLY
                     PassageAbility.RIGHT_ONLY -> GameMapProto.PassageAbility.RIGHT_ONLY
                  }
               )
            }
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