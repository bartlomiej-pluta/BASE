package com.bartlomiejpluta.base.editor.map.serial

import com.bartlomiejpluta.base.editor.map.model.map.GameMap
import com.google.protobuf.util.JsonFormat
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.io.OutputStream
import java.io.PrintWriter

@Component
class JsonProtobufMapSerializer : TextMapSerializer {

   @Autowired
   private lateinit var serializer: ProtobufMapSerializer
   override fun serialize(item: GameMap, output: OutputStream) {
      output.bufferedWriter().let(::PrintWriter).use { out ->
         JsonFormat.printer().print(serializer.buildProto(item)).lines().forEach { line ->
            out.println(line)
         }
      }
   }
}