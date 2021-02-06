package com.bartlomiejpluta.base.editor.model.map.layer

import javafx.beans.property.StringProperty

interface Layer {
   var name: String
   val nameProperty: StringProperty

   fun resize(rows: Int, columns: Int)
}