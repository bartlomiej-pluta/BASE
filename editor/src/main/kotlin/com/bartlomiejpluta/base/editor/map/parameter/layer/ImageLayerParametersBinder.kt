package com.bartlomiejpluta.base.editor.map.parameter.layer

import com.bartlomiejpluta.base.editor.common.parameter.model.EnumParameter
import com.bartlomiejpluta.base.editor.common.parameter.model.GraphicAssetParameter
import com.bartlomiejpluta.base.editor.common.parameter.model.IntegerParameter
import com.bartlomiejpluta.base.editor.common.parameter.model.Parameter
import com.bartlomiejpluta.base.editor.map.model.enumeration.ImageLayerMode
import com.bartlomiejpluta.base.editor.map.model.layer.ImageLayer
import com.bartlomiejpluta.base.editor.project.model.Project
import javafx.collections.ObservableList
import org.springframework.stereotype.Component

@Component
class ImageLayerParametersBinder : LayerParametersBinder<ImageLayer> {

   override fun bind(
      layer: ImageLayer,
      parameters: ObservableList<Parameter<*>>,
      project: Project,
      onCommit: () -> Unit
   ) {
      val image = GraphicAssetParameter("image", layer.imageAsset, true, project.images) { _, _, submit ->
         onCommit()
         submit()
      }

      val opacity = IntegerParameter("opacity", 100, 0, 100, autocommit = true) { _, _, _ ->
         onCommit()
      }

      val mode = EnumParameter("mode", ImageLayerMode.NORMAL, autocommit = true) { _, _, _ ->
         onCommit()
      }

      image.bindBidirectional(layer.imageAssetProperty)
      opacity.bindBidirectional(layer.opacityProperty)
      mode.bindBidirectional(layer.modeProperty)

      parameters.addAll(image, opacity, mode)
   }
}