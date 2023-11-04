package com.bartlomiejpluta.base.editor.project.serial

import com.bartlomiejpluta.base.editor.project.model.Project
import com.bartlomiejpluta.base.proto.ProjectProto
import com.google.protobuf.util.JsonFormat
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.io.InputStream

@Component
class JsonProtobufProjectDeserializer : TextProjectDeserializer {

   @Autowired
   private lateinit var deserializer: ProtobufProjectDeserializer
   override fun deserialize(input: InputStream): Project = input.bufferedReader().use { reader ->
      val builder = ProjectProto.Project.newBuilder()
      JsonFormat.parser().merge(reader, builder)
      deserializer.buildObject(builder.build())
   }
}