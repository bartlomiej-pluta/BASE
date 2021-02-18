package com.bartlomiejpluta.base.editor.map.model.layer

import javafx.beans.property.SimpleObjectProperty
import javafx.beans.property.SimpleStringProperty
import tornadofx.getValue
import tornadofx.setValue

class ColorLayer(name: String, red: Int, green: Int, blue: Int, alpha: Int) : Layer {
   override val nameProperty = SimpleStringProperty(name)

   override var name by nameProperty

   val redProperty = SimpleObjectProperty(red)
   var red by redProperty

   val greenProperty = SimpleObjectProperty(green)
   var green by greenProperty

   val blueProperty = SimpleObjectProperty(blue)
   var blue by blueProperty

   val alphaProperty = SimpleObjectProperty(alpha)
   var alpha by alphaProperty

   override fun resize(rows: Int, columns: Int) {
      // We essentially need to do nothing
   }
}