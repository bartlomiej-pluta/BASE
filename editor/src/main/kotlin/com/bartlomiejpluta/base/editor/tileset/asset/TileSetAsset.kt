package com.bartlomiejpluta.base.editor.tileset.asset

import com.bartlomiejpluta.base.editor.asset.model.Asset
import javafx.beans.property.SimpleStringProperty
import tornadofx.*

class TileSetAsset(
   override val uid: String,
   override val source: String,
   name: String,
   val rows: Int,
   val columns: Int
) : Asset {
   val nameProperty = SimpleStringProperty(name)
   override var name by nameProperty
}
