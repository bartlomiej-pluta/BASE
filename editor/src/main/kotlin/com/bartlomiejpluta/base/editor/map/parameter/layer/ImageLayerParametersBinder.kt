package com.bartlomiejpluta.base.editor.map.parameter.layer

import com.bartlomiejpluta.base.editor.common.parameter.model.*
import com.bartlomiejpluta.base.editor.map.model.enumeration.ImageLayerMode
import com.bartlomiejpluta.base.editor.map.model.layer.ImageLayer
import com.bartlomiejpluta.base.editor.project.model.Project
import javafx.beans.binding.Bindings.createBooleanBinding
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

      val x = IntegerParameter("x", 0, autocommit = true) { _, _, _ ->
         onCommit()
      }

      val y = IntegerParameter("y", 0, autocommit = true) { _, _, _ ->
         onCommit()
      }

      val scaleX = DoubleParameter("scaleX", 1.0, 0.0, Double.MAX_VALUE, 0.01, autocommit = true) { _, _, _ ->
         onCommit()
      }

      val scaleY = DoubleParameter("scaleY", 1.0, 0.0, Double.MAX_VALUE, 0.01, autocommit = true) { _, _, _ ->
         onCommit()
      }

      val mode = EnumParameter("mode", ImageLayerMode.NORMAL, autocommit = true) { _, _, _ ->
         onCommit()
      }

      val parallax = BooleanParameter("parallax") { _, _, _ ->
         onCommit()
      }

      image.bindBidirectional(layer.imageAssetProperty)
      opacity.bindBidirectional(layer.opacityProperty)
      x.bindBidirectional(layer.xProperty)
      y.bindBidirectional(layer.yProperty)
      scaleX.bindBidirectional(layer.scaleXProperty)
      scaleY.bindBidirectional(layer.scaleYProperty)
      mode.bindBidirectional(layer.modeProperty)
      parallax.bindBidirectional(layer.parallaxProperty)

      val isNormalMode = createBooleanBinding({ mode.value == ImageLayerMode.NORMAL }, mode.valueProperty).apply {
         addListener { _, _, value ->
            if (!value) {
               scaleX.value = 1.0
               scaleY.value = 1.0
            }
         }
      }

      scaleX.editableProperty.bind(isNormalMode)
      scaleY.editableProperty.bind(isNormalMode)

      parameters.addAll(image, opacity, x, y, scaleX, scaleY, mode, parallax)
   }
}