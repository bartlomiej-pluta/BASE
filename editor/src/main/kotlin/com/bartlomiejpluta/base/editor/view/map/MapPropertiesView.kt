package com.bartlomiejpluta.base.editor.view.map

import com.bartlomiejpluta.base.editor.model.map.map.MapProperty
import com.bartlomiejpluta.base.editor.viewmodel.map.GameMapVM
import tornadofx.*

class MapPropertiesView : View() {
   private val mapVM = find<GameMapVM>()

   override val root = tableview(mapVM.mapProperties) {
      column("Key", MapProperty::keyProperty)
      column("Value", MapProperty::valueProperty)
   }
}