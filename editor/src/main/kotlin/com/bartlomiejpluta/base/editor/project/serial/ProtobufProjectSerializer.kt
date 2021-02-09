package com.bartlomiejpluta.base.editor.project.serial

import com.bartlomiejpluta.base.editor.project.model.Project
import com.bartlomiejpluta.base.proto.ProjectProto
import org.springframework.stereotype.Component
import java.io.OutputStream

@Component
class ProtobufProjectSerializer : ProjectSerializer {

   override fun serialize(item: Project, output: OutputStream) {
      val proto = ProjectProto.Project.newBuilder()
      proto.name = item.name
      proto.build().writeTo(output)
   }
}