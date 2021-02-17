package com.bartlomiejpluta.base.editor.map.model.layer

import javafx.beans.property.SimpleStringProperty
import tornadofx.getValue
import tornadofx.setValue

class ImageLayer(name: String) : Layer {
   override val nameProperty = SimpleStringProperty(name)

   override fun resize(rows: Int, columns: Int) {
      // We essentially need to do nothing
   }

   override var name by nameProperty
}