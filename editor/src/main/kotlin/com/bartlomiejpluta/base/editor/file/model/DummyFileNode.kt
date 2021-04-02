package com.bartlomiejpluta.base.editor.file.model

import javafx.beans.value.ObservableLongValue
import javafx.beans.value.ObservableValue

class DummyFileNode : FileNode {
   override val nameProperty: ObservableValue<String>
      get() = throw UnsupportedOperationException()

   override val name: String
      get() = throw UnsupportedOperationException()

   override val extensionProperty: ObservableValue<String>
      get() = throw UnsupportedOperationException()

   override val extension: String
      get() = throw UnsupportedOperationException()

   override val nameWithoutExtensionProperty: ObservableValue<String>
      get() = throw UnsupportedOperationException()

   override val nameWithoutExtension: String
      get() = throw UnsupportedOperationException()

   override val absolutePathProperty: ObservableValue<String>
      get() = throw UnsupportedOperationException()

   override val absolutePath: String
      get() = throw UnsupportedOperationException()

   override val type: FileType
      get() = throw UnsupportedOperationException()

   override val parent: FileNode
      get() = throw UnsupportedOperationException()

   override val children: Iterable<FileNode>
      get() = throw UnsupportedOperationException()

   override val lastModifiedProperty: ObservableLongValue
      get() = throw UnsupportedOperationException()

   override val lastModified: Long
      get() = throw UnsupportedOperationException()
}