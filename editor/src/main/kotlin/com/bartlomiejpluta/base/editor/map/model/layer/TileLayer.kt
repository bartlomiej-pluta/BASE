package com.bartlomiejpluta.base.editor.map.model.layer

import com.bartlomiejpluta.base.editor.tileset.asset.TileSetAsset
import com.bartlomiejpluta.base.editor.tileset.model.Tile
import com.bartlomiejpluta.base.editor.tileset.model.TileSet
import javafx.beans.binding.Bindings
import javafx.beans.property.SimpleStringProperty
import javafx.scene.image.Image
import tornadofx.getValue
import tornadofx.setValue
import tornadofx.toProperty

class TileLayer(
   name: String,
   rows: Int,
   columns: Int,
   tileSetAsset: TileSetAsset,
   layer: Array<Array<Tile?>> = Array(rows) { Array(columns) { null } }
) : Layer {
   var layer = layer
      private set

   override val nameProperty = SimpleStringProperty(name)
   override var name: String by nameProperty

   val tileSetAssetProperty = tileSetAsset.toProperty()
   var tileSetAsset by tileSetAssetProperty

   val tileSetProperty = Bindings.createObjectBinding({
      tileSetAssetProperty.value.file.inputStream().use { fis ->
         TileSet(
            tileSetAsset.uid,
            tileSetAsset.name,
            Image(fis),
            tileSetAsset.rows,
            tileSetAsset.columns
         )
      }
   }, tileSetAssetProperty)

   override fun resize(rows: Int, columns: Int) {
      layer = Array(rows) { row ->
         Array(columns) { column ->
            layer.getOrNull(row)?.getOrNull(column)
         }
      }
   }
}