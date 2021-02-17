package com.bartlomiejpluta.base.editor.map.model.layer

import com.bartlomiejpluta.base.editor.map.model.enumeration.PassageAbility
import javafx.beans.property.SimpleStringProperty
import tornadofx.getValue
import tornadofx.setValue

class ObjectLayer(
   name: String,
   rows: Int,
   columns: Int,
   passageMap: Array<Array<PassageAbility>> = Array(rows) { Array(columns) { PassageAbility.ALLOW } }
) : Layer {
   var passageMap = passageMap
      private set

   override val nameProperty = SimpleStringProperty(name)

   override fun resize(rows: Int, columns: Int) {
      passageMap = Array(rows) { row ->
         Array(columns) { column ->
            passageMap.getOrNull(row)?.getOrNull(column) ?: PassageAbility.ALLOW
         }
      }
   }

   override var name by nameProperty
}