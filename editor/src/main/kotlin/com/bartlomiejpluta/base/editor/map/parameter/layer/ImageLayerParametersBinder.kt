package com.bartlomiejpluta.base.editor.map.parameter.layer

import com.bartlomiejpluta.base.editor.common.parameter.model.GraphicAssetParameter
import com.bartlomiejpluta.base.editor.common.parameter.model.Parameter
import com.bartlomiejpluta.base.editor.image.asset.ImageAsset
import com.bartlomiejpluta.base.editor.map.model.layer.ImageLayer
import com.bartlomiejpluta.base.editor.project.model.Project
import javafx.collections.ObservableList
import org.springframework.stereotype.Component

@Component
class ImageLayerParametersBinder : LayerParametersBinder<ImageLayer> {

   override fun bind(layer: ImageLayer, parameters: ObservableList<Parameter<*>>, project: Project, onCommit: () -> Unit) {
      val image = GraphicAssetParameter("image", layer.imageAsset, true, project.images) { _, _, submit ->
         onCommit()
         submit()
      }

      image.valueProperty.bindBidirectional(layer.imageAssetProperty)

      parameters.addAll(image)
   }

   override fun unbind(layer: ImageLayer, parameters: ObservableList<Parameter<*>>) {
      (parameters[0] as GraphicAssetParameter<ImageAsset>).valueProperty.unbindBidirectional(layer.imageAssetProperty)
   }
}