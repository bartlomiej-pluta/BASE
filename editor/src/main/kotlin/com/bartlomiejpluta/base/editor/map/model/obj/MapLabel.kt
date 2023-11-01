package com.bartlomiejpluta.base.editor.map.model.obj

import tornadofx.getValue
import tornadofx.setValue
import tornadofx.toProperty

class MapLabel(x: Int, y: Int, label: String) {
   val xProperty = x.toProperty()
   var x by xProperty

   val yProperty = y.toProperty()
   var y by yProperty

   val labelProperty = label.toProperty()
   var label by labelProperty
}