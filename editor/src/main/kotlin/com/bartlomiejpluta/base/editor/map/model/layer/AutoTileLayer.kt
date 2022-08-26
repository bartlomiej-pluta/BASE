package com.bartlomiejpluta.base.editor.map.model.layer

import com.bartlomiejpluta.base.editor.autotile.asset.AutoTileAsset
import com.bartlomiejpluta.base.editor.autotile.model.AutoTile
import javafx.beans.binding.Bindings
import javafx.beans.property.SimpleStringProperty
import javafx.scene.image.Image
import tornadofx.getValue
import tornadofx.setValue
import tornadofx.toProperty

class AutoTileLayer(
   name: String,
   rows: Int,
   columns: Int,
   autoTileAsset: AutoTileAsset,
   layer: Array<Array<Boolean>> = Array(rows) { Array(columns) { false } }
) : Layer {
   var layer = layer
      private set

   var rows = rows
      private set

   var columns = columns
      private set

   val autoTileAssetProperty = autoTileAsset.toProperty()
   var autoTileAsset by autoTileAssetProperty

   val autoTileProperty = Bindings.createObjectBinding({
      autoTileAsset.file.inputStream().use { fis -> AutoTile(autoTileAsset.uid, autoTileAsset.name, Image(fis)) }
   }, autoTileAssetProperty)
   val autoTile by autoTileProperty

   override val nameProperty = SimpleStringProperty(name)

   override fun resize(rows: Int, columns: Int) {
      this.rows = rows
      this.columns = columns

      layer = Array(rows) { row ->
         Array(columns) { column ->
            layer.getOrNull(row)?.getOrNull(column) ?: false
         }
      }
   }

   override var name: String by nameProperty
}