package com.bartlomiejpluta.base.editor.map.viewmodel

import com.bartlomiejpluta.base.editor.map.model.brush.Brush
import tornadofx.*

class BrushVM : ItemViewModel<Brush>(Brush.of(arrayOf(arrayOf()))) {
   val modeProperty = bind(Brush::modeProperty)
   var mode by modeProperty

   val toolProperty = bind(Brush::toolProperty)
   var tool by toolProperty

   val rangeProperty = bind(Brush::rangeProperty)
   var range by rangeProperty
}