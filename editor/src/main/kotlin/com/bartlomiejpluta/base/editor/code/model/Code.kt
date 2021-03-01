package com.bartlomiejpluta.base.editor.code.model

import com.bartlomiejpluta.base.editor.file.model.FileNode
import javafx.beans.property.Property
import tornadofx.getValue
import tornadofx.setValue
import tornadofx.toProperty

class Code(fileNode: FileNode, val typeProperty: Property<CodeType>, code: String) {
   val fileNodeProperty = fileNode.toProperty()
   val fileNode by fileNodeProperty

   val type by typeProperty

   val codeProperty = code.toProperty()
   var code by codeProperty
}