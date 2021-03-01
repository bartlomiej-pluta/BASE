package com.bartlomiejpluta.base.editor.file.model

import javafx.beans.property.StringProperty
import org.springframework.core.io.ClassPathResource
import org.springframework.core.io.Resource
import tornadofx.getValue
import tornadofx.observableListOf
import tornadofx.toProperty
import java.nio.file.Path

class ResourceFileNode private constructor(
   private val resource: Resource? = null,
   override val parent: ResourceFileNode? = null,
   override val type: FileType = FileType.FILE,
   name: String? = null
) : FileNode {

   override val nameProperty = (resource?.filename ?: name ?: "").toProperty()
   override val name by nameProperty

   override val extensionProperty = (resource?.filename?.substringAfter(".") ?: "").toProperty()
   override val extension by extensionProperty

   override val absolutePathProperty: StringProperty =
      ((parent?.absolutePath ?: "") + "/${nameProperty.value}").toProperty()
   override val absolutePath by absolutePathProperty

   override val children = observableListOf<ResourceFileNode>()

   override val lastModifiedProperty = (resource?.lastModified() ?: 0L).toProperty()
   override val lastModified by lastModifiedProperty

   override fun createNode(path: String): ResourceFileNode {
      val segments = Path.of(path.replace("..", "."))

      return segments.foldIndexed(this) { index, parent, segment ->
         when (val child = parent.children.firstOrNull { it.name == segment.toString() }) {
            null -> when (index < segments.count() - 1) {
               true -> directory(segment.toString(), parent)
               false -> resource(ClassPathResource("$absolutePath/$path"), parent)
            }.also { parent.children += it }
            else -> child
         }
      }
   }

   override fun inputStream() = resource
      ?.inputStream
      ?: throw IllegalStateException("Attempt to open input stream of resource directory")

   companion object {
      fun resource(resource: Resource, parent: ResourceFileNode) = ResourceFileNode(resource, parent)
      fun directory(name: String, parent: ResourceFileNode) = ResourceFileNode(null, parent, FileType.DIRECTORY, name)
      fun root(name: String) = ResourceFileNode(null, null, FileType.DIRECTORY, name)
   }
}