package com.bartlomiejpluta.base.editor.map.asset

import com.bartlomiejpluta.base.editor.asset.model.Asset
import javafx.beans.property.SimpleStringProperty
import tornadofx.getValue
import tornadofx.setValue

class GameMapAsset(override val uid: String, name: String) : Asset {
   override val source = "$uid.dat"

   val nameProperty = SimpleStringProperty(name)
   override var name by nameProperty
}