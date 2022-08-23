package com.bartlomiejpluta.base.editor.asset.model

import javafx.beans.property.ObjectProperty
import javafx.beans.property.SimpleIntegerProperty
import java.io.File

abstract class GraphicAsset(
   directory: ObjectProperty<File>,
   uid: String,
   source: String,
   name: String,
   val rows: Int,
   val columns: Int
) : Asset(directory, uid, source, name) {
   val rowsProperty = SimpleIntegerProperty(rows)
   val columnsProperty = SimpleIntegerProperty(columns)
}