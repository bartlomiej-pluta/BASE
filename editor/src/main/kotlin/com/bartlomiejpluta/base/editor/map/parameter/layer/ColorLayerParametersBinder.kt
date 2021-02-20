package com.bartlomiejpluta.base.editor.map.parameter.layer

import com.bartlomiejpluta.base.editor.common.parameter.model.IntegerParameter
import com.bartlomiejpluta.base.editor.common.parameter.model.Parameter
import com.bartlomiejpluta.base.editor.map.model.layer.ColorLayer
import com.bartlomiejpluta.base.editor.project.model.Project
import javafx.collections.ObservableList
import org.springframework.stereotype.Component

@Component
class ColorLayerParametersBinder : LayerParametersBinder<ColorLayer> {
   private var red: IntegerParameter? = null
   private var green: IntegerParameter? = null
   private var blue: IntegerParameter? = null
   private var alpha: IntegerParameter? = null

   override fun bind(
      layer: ColorLayer,
      parameters: ObservableList<Parameter<*>>,
      project: Project,
      onCommit: () -> Unit
   ) {
      val red = IntegerParameter("red", 100, 0, 100, autocommit = true) { _, _, _ -> onCommit() }
         .apply { valueProperty.bindBidirectional(layer.redProperty) }

      val green = IntegerParameter("green", 100, 0, 100, autocommit = true) { _, _, _ -> onCommit() }
         .apply { valueProperty.bindBidirectional(layer.greenProperty) }

      val blue = IntegerParameter("blue", 100, 0, 100, autocommit = true) { _, _, _ -> onCommit() }
         .apply { valueProperty.bindBidirectional(layer.blueProperty) }

      val alpha = IntegerParameter("alpha", 100, 0, 100, autocommit = true) { _, _, _ -> onCommit() }
         .apply { valueProperty.bindBidirectional(layer.alphaProperty) }

      parameters.addAll(red, green, blue, alpha)
   }

   override fun unbind(layer: ColorLayer, parameters: ObservableList<Parameter<*>>) {
      red?.valueProperty?.unbindBidirectional(layer.redProperty)
      green?.valueProperty?.unbindBidirectional(layer.greenProperty)
      blue?.valueProperty?.unbindBidirectional(layer.blueProperty)
      alpha?.valueProperty?.unbindBidirectional(layer.alphaProperty)
   }
}