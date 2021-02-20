package com.bartlomiejpluta.base.editor.map.parameter.layer

import com.bartlomiejpluta.base.editor.common.parameter.model.IntegerParameter
import com.bartlomiejpluta.base.editor.common.parameter.model.Parameter
import com.bartlomiejpluta.base.editor.map.model.layer.ColorLayer
import com.bartlomiejpluta.base.editor.project.model.Project
import javafx.collections.ObservableList
import org.springframework.stereotype.Component

@Component
class ColorLayerParametersBinder : LayerParametersBinder<ColorLayer> {

   override fun bind(
      layer: ColorLayer,
      parameters: ObservableList<Parameter<*>>,
      project: Project,
      onCommit: () -> Unit
   ) {
      val red = IntegerParameter("red", 100, 0, 100, autocommit = true) { _, _, _ -> onCommit() }
      val green = IntegerParameter("green", 100, 0, 100, autocommit = true) { _, _, _ -> onCommit() }
      val blue = IntegerParameter("blue", 100, 0, 100, autocommit = true) { _, _, _ -> onCommit() }
      val alpha = IntegerParameter("alpha", 100, 0, 100, autocommit = true) { _, _, _ -> onCommit() }

      red.bindBidirectional(layer.redProperty)
      green.bindBidirectional(layer.greenProperty)
      blue.bindBidirectional(layer.blueProperty)
      alpha.bindBidirectional(layer.alphaProperty)

      parameters.addAll(red, green, blue, alpha)
   }
}