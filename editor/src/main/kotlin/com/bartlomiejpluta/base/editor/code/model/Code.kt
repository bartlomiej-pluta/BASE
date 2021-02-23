package com.bartlomiejpluta.base.editor.code.model

import tornadofx.getValue
import tornadofx.setValue
import tornadofx.toProperty
import java.io.File

class Code(file: File, type: CodeType, code: String) {
   val fileProperty = file.toProperty()
   val file by fileProperty

   val typeProperty = type.toProperty()
   val type by typeProperty

   val codeProperty = code.toProperty()
   var code by codeProperty
}