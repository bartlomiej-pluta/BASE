package com.bartlomiejpluta.base.editor.map.parameter.layer

import com.bartlomiejpluta.base.editor.common.parameter.model.GraphicAssetParameter
import com.bartlomiejpluta.base.editor.common.parameter.model.Parameter
import com.bartlomiejpluta.base.editor.map.model.layer.AutoTileLayer
import com.bartlomiejpluta.base.editor.project.model.Project
import javafx.collections.ObservableList
import org.springframework.stereotype.Component

@Component
class AutoTileLayerParametersBinder : LayerParametersBinder<AutoTileLayer> {
   override fun bind(
      layer: AutoTileLayer,
      parameters: ObservableList<Parameter<*>>,
      project: Project,
      onCommit: () -> Unit
   ) {
      val autoTile = GraphicAssetParameter("autoTile", layer.autoTileAsset, true, project.autoTiles) { _, _, submit ->
         onCommit()
         submit()
      }

      autoTile.bindBidirectional(layer.autoTileAssetProperty)

      parameters.addAll(autoTile)
   }
}