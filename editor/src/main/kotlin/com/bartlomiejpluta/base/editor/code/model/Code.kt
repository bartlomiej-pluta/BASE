package com.bartlomiejpluta.base.editor.code.model

import tornadofx.getValue
import tornadofx.setValue
import tornadofx.toProperty
import java.io.File

class Code(file: File) {
   val fileProperty = file.toProperty()
   val file by fileProperty

   val typeProperty = deduceCodeType(file).toProperty()
   val type by typeProperty

   val codeProperty = file.readText().toProperty()
   var code by codeProperty

   companion object {
      private fun deduceCodeType(file: File): CodeType {
         return when (file.extension.toLowerCase()) {
            "java" -> CodeType.JAVA
            else -> throw IllegalStateException("Unsupported script type")
         }
      }
   }
}