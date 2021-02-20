package com.bartlomiejpluta.base.editor.map.parameter.layer

import com.bartlomiejpluta.base.editor.common.parameter.model.GraphicAssetParameter
import com.bartlomiejpluta.base.editor.common.parameter.model.IntegerParameter
import com.bartlomiejpluta.base.editor.common.parameter.model.Parameter
import com.bartlomiejpluta.base.editor.image.asset.ImageAsset
import com.bartlomiejpluta.base.editor.map.model.layer.ImageLayer
import com.bartlomiejpluta.base.editor.project.model.Project
import javafx.collections.ObservableList
import org.springframework.stereotype.Component

@Component
class ImageLayerParametersBinder : LayerParametersBinder<ImageLayer> {
   private var image: GraphicAssetParameter<ImageAsset>? = null
   private var opacity: IntegerParameter? = null

   override fun bind(
      layer: ImageLayer,
      parameters: ObservableList<Parameter<*>>,
      project: Project,
      onCommit: () -> Unit
   ) {
      image = GraphicAssetParameter("image", layer.imageAsset, true, project.images) { _, _, submit ->
         onCommit()
         submit()
      }.apply { valueProperty.bindBidirectional(layer.imageAssetProperty) }

      opacity = IntegerParameter("opacity", 100, 0, 100, autocommit = true) { _, _, _ ->
         onCommit()
      }.apply { valueProperty.bindBidirectional(layer.opacityProperty) }

      parameters.addAll(image, opacity)
   }

   override fun unbind(layer: ImageLayer, parameters: ObservableList<Parameter<*>>) {
      image?.valueProperty?.unbindBidirectional(layer.imageAssetProperty)
      opacity?.valueProperty?.unbindBidirectional(layer.opacityProperty)
   }
}