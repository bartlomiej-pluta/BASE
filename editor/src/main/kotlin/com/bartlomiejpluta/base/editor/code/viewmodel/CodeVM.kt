package com.bartlomiejpluta.base.editor.code.viewmodel

import com.bartlomiejpluta.base.editor.code.model.Code
import tornadofx.ItemViewModel
import tornadofx.getValue
import tornadofx.setValue

class CodeVM(code: Code) : ItemViewModel<Code>(code) {
   val typeProperty = bind(Code::typeProperty)
   val type by typeProperty

   val codeProperty = bind(Code::codeProperty)
   var code by codeProperty
}