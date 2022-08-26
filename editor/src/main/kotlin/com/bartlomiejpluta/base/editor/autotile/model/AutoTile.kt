package com.bartlomiejpluta.base.editor.autotile.model

import javafx.beans.property.ReadOnlyStringWrapper
import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleObjectProperty
import javafx.beans.property.SimpleStringProperty
import javafx.scene.image.Image
import tornadofx.getValue

class AutoTile(uid: String, name: String, image: Image) {
   val uidProperty = ReadOnlyStringWrapper(uid)
   val uid by uidProperty

   val nameProperty = SimpleStringProperty(name)
   val name by nameProperty

   val imageProperty = SimpleObjectProperty(image)
   val image by imageProperty

   val rowsProperty = SimpleIntegerProperty(6)
   val rows by rowsProperty

   val columnsProperty = SimpleIntegerProperty(4)
   val columns by columnsProperty

   val tileWidthProperty = SimpleIntegerProperty(image.width.toInt() / columns)
   val tileWidth by tileWidthProperty

   val tileHeightProperty = SimpleIntegerProperty(image.height.toInt() / rows)
   val tileHeight by tileHeightProperty

   val widthProperty = SimpleIntegerProperty(tileWidth * columns)
   val width by widthProperty

   val heightProperty = SimpleIntegerProperty(tileHeight * rows)
   val height by heightProperty
}