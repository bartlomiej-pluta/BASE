package com.bartlomiejpluta.base.editor.map.model.layer

import javafx.beans.Observable
import javafx.beans.property.StringProperty
import javafx.util.Callback

interface Layer {
   var name: String
   val nameProperty: StringProperty

   fun resize(rows: Int, columns: Int)

   companion object {
      fun extractor() = Callback<Layer, Array<Observable>> { arrayOf(it.nameProperty) }
   }
}