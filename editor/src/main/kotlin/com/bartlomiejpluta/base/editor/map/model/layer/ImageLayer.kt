package com.bartlomiejpluta.base.editor.map.model.layer

import javafx.beans.property.SimpleObjectProperty
import javafx.beans.property.SimpleStringProperty
import tornadofx.getValue
import tornadofx.setValue

class ImageLayer(name: String) : Layer {
   override val nameProperty = SimpleStringProperty(name)

   override var name by nameProperty

   val redProperty = SimpleObjectProperty(100)
   var red by redProperty

   val greenProperty = SimpleObjectProperty(100)
   var green by greenProperty

   val blueProperty = SimpleObjectProperty(100)
   var blue by blueProperty

   val alphaProperty = SimpleObjectProperty(100)
   var alpha by alphaProperty

   override fun resize(rows: Int, columns: Int) {
      // We essentially need to do nothing
   }
}