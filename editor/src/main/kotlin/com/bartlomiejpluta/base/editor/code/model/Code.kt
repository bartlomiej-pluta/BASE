package com.bartlomiejpluta.base.editor.code.model

import tornadofx.getValue
import tornadofx.setValue
import tornadofx.toProperty

class Code(code: String) {
   val codeProperty = code.toProperty()
   var code by codeProperty
}