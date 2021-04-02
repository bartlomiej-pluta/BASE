package com.bartlomiejpluta.base.editor.map.model.obj

import tornadofx.getValue
import tornadofx.setValue
import tornadofx.toProperty

class MapObject(x: Int, y: Int, code: String) {
   val xProperty = x.toProperty()
   var x by xProperty

   val yProperty = y.toProperty()
   var y by yProperty

   val codeProperty = code.toProperty()
   var code by codeProperty
}