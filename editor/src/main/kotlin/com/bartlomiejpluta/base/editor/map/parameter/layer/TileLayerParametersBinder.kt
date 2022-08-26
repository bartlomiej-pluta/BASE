package com.bartlomiejpluta.base.editor.map.parameter.layer

import com.bartlomiejpluta.base.editor.common.parameter.model.GraphicAssetParameter
import com.bartlomiejpluta.base.editor.common.parameter.model.Parameter
import com.bartlomiejpluta.base.editor.map.model.layer.TileLayer
import com.bartlomiejpluta.base.editor.project.model.Project
import javafx.collections.ObservableList
import org.springframework.stereotype.Component

@Component
class TileLayerParametersBinder : LayerParametersBinder<TileLayer> {
   override fun bind(
      layer: TileLayer,
      parameters: ObservableList<Parameter<*>>,
      project: Project,
      onCommit: () -> Unit
   ) {
      val tileSet = GraphicAssetParameter("tileSet", layer.tileSetAsset, true, project.tileSets) { _, _, submit ->
         onCommit()
         submit()
      }

      tileSet.bindBidirectional(layer.tileSetAssetProperty)

      parameters.addAll(tileSet)
   }
}