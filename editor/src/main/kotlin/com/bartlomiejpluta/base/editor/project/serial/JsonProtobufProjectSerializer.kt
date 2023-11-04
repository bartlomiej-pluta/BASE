package com.bartlomiejpluta.base.editor.project.serial

import com.bartlomiejpluta.base.editor.project.model.Project
import com.google.protobuf.util.JsonFormat
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.io.OutputStream
import java.io.PrintWriter

@Component
class JsonProtobufProjectSerializer : TextProjectSerializer {

   @Autowired
   private lateinit var serializer: ProtobufProjectSerializer
   override fun serialize(item: Project, output: OutputStream) {
      output.bufferedWriter().let(::PrintWriter).use { out ->
         JsonFormat.printer().print(serializer.buildProto(item)).lines().forEach { line ->
            out.println(line)
         }
      }
   }
}