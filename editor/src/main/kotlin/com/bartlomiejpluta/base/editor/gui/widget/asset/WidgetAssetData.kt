package com.bartlomiejpluta.base.editor.gui.widget.asset

import tornadofx.getValue
import tornadofx.setValue
import tornadofx.toProperty

class WidgetAssetData(name: String) {
   val nameProperty = name.toProperty()
   var name by nameProperty
}