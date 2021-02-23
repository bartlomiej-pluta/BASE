package com.bartlomiejpluta.base.editor.code.model

import tornadofx.getValue
import tornadofx.setValue
import tornadofx.toProperty

class Code(codeType: CodeType, code: String) {
   val typeProperty = codeType.toProperty()
   var type by typeProperty

   val codeProperty = code.toProperty()
   var code by codeProperty
}