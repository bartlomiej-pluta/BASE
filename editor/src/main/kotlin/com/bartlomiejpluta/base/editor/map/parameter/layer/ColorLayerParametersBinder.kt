package com.bartlomiejpluta.base.editor.map.parameter.layer

import com.bartlomiejpluta.base.editor.common.parameter.model.IntegerParameter
import com.bartlomiejpluta.base.editor.common.parameter.model.Parameter
import com.bartlomiejpluta.base.editor.map.model.layer.ColorLayer
import javafx.collections.ObservableList
import org.springframework.stereotype.Component

@Component
class ColorLayerParametersBinder : LayerParametersBinder<ColorLayer> {
   override fun bind(layer: ColorLayer, parameters: ObservableList<Parameter<*>>, onCommit: () -> Unit) {
      val red = IntegerParameter("red", 100, 0, 100, autocommit = true) { _, _, _ -> onCommit() }
      val green = IntegerParameter("green", 100, 0, 100, autocommit = true) { _, _, _ -> onCommit() }
      val blue = IntegerParameter("blue", 100, 0, 100, autocommit = true) { _, _, _ -> onCommit() }
      val alpha = IntegerParameter("alpha", 100, 0, 100, autocommit = true) { _, _, _ -> onCommit() }

      red.valueProperty.bindBidirectional(layer.redProperty)
      green.valueProperty.bindBidirectional(layer.greenProperty)
      blue.valueProperty.bindBidirectional(layer.blueProperty)
      alpha.valueProperty.bindBidirectional(layer.alphaProperty)

      parameters.addAll(red, green, blue, alpha)
   }

   override fun unbind(layer: ColorLayer, parameters: ObservableList<Parameter<*>>) {
      (parameters[0] as IntegerParameter).valueProperty.unbindBidirectional(layer.redProperty)
      (parameters[1] as IntegerParameter).valueProperty.unbindBidirectional(layer.greenProperty)
      (parameters[2] as IntegerParameter).valueProperty.unbindBidirectional(layer.blueProperty)
      (parameters[3] as IntegerParameter).valueProperty.unbindBidirectional(layer.alphaProperty)
   }
}