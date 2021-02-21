package com.bartlomiejpluta.base.editor.asset.model

import javafx.beans.binding.Bindings.createObjectBinding
import javafx.beans.property.ObjectProperty
import tornadofx.getValue
import java.io.File

abstract class GraphicAsset(
   assetDirectory: ObjectProperty<File>,
   graphicDirectory: ObjectProperty<File>,
   uid: String,
   assetSource: String,
   val graphicSource: String,
   name: String
) : Asset(assetDirectory, uid, assetSource, name) {

   constructor(directory: ObjectProperty<File>, uid: String, source: String, name: String)
         : this(directory, directory, uid, source, source, name)

   val graphicFileProperty = createObjectBinding({ File(graphicDirectory.value, graphicSource) }, graphicDirectory)
   val graphicFile by graphicFileProperty

   override fun delete() {
      super.delete()
      graphicFile.delete()
   }
}