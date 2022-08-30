package com.bartlomiejpluta.base.editor.map.model.layer

import com.bartlomiejpluta.base.editor.map.model.enumeration.PassageAbility
import com.bartlomiejpluta.base.editor.map.model.obj.MapObject
import javafx.beans.property.SimpleStringProperty
import tornadofx.asObservable
import tornadofx.getValue
import tornadofx.setValue
import tornadofx.toProperty

class ObjectLayer(
   name: String,
   rows: Int,
   columns: Int,
   objects: List<MapObject> = mutableListOf(),
   javaImports: String = "",
   passageMap: Array<Array<PassageAbility>> = Array(rows) { Array(columns) { PassageAbility.ALLOW } }
) : Layer {
   var passageMap = passageMap
      private set

   val objects = objects.asObservable()

   override val nameProperty = SimpleStringProperty(name)

   val javaImportsProperty = javaImports.toProperty()
   var javaImports by javaImportsProperty

   override fun resize(rows: Int, columns: Int) {
      passageMap = Array(rows) { row ->
         Array(columns) { column ->
            passageMap.getOrNull(row)?.getOrNull(column) ?: PassageAbility.ALLOW
         }
      }
   }

   override var name by nameProperty
}