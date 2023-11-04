package com.bartlomiejpluta.base.editor.map.serial

import com.bartlomiejpluta.base.editor.map.model.map.GameMap
import com.bartlomiejpluta.base.proto.GameMapProto
import com.google.protobuf.util.JsonFormat
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.io.InputStream

@Component
class JsonProtobufMapDeserializer : TextMapDeserializer {

   @Autowired
   private lateinit var deserializer: ProtobufMapDeserializer

   override fun deserialize(input: InputStream) = deserialize(input, { _, uid -> uid }, { _, uid -> uid })

   override fun deserialize(input: InputStream, replaceTileSet: (String, String) -> String, replaceAutoTile: (String, String) -> String): GameMap =
      input.bufferedReader().use { reader ->
         val builder = GameMapProto.GameMap.newBuilder()
         JsonFormat.parser().merge(reader, builder)
         deserializer.buildObject(builder.build(), replaceTileSet, replaceAutoTile)
      }
}