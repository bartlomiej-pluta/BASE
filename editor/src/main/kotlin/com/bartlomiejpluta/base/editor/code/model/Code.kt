package com.bartlomiejpluta.base.editor.code.model

import javafx.beans.property.Property
import tornadofx.getValue
import tornadofx.setValue
import tornadofx.toProperty
import java.io.File

class Code(val fileProperty: Property<File>, val typeProperty: Property<CodeType>, code: String) {
   val file by fileProperty

   val type by typeProperty

   val codeProperty = code.toProperty()
   var code by codeProperty
}