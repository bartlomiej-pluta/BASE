package com.bartlomiejpluta.base.editor.map.model.layer

import javafx.beans.property.StringProperty

interface Layer {
   var name: String
   val nameProperty: StringProperty

   fun resize(rows: Int, columns: Int)

   fun clone(): Layer
}