package com.bartlomiejpluta.base.editor.map.view.editor

import com.bartlomiejpluta.base.editor.common.parameter.model.Parameter
import com.bartlomiejpluta.base.editor.common.parameter.view.ParametersTableFragment
import com.bartlomiejpluta.base.editor.event.RedrawMapRequestEvent
import com.bartlomiejpluta.base.editor.map.model.layer.ColorLayer
import com.bartlomiejpluta.base.editor.map.parameter.layer.ColorLayerParametersBinder
import com.bartlomiejpluta.base.editor.map.viewmodel.EditorStateVM
import tornadofx.View
import tornadofx.observableListOf

class MapLayerParameters : View() {
   private val editorStateVM = find<EditorStateVM>()

   // For some reason Spring does not want to autowire a list of beans
   // of LayerParametersBinder<> type
   private val colorLayerParametersBinder: ColorLayerParametersBinder by di()

   private val parameters = observableListOf<Parameter<*>>()

   init {
      editorStateVM.selectedLayerProperty.addListener { _, previousLayer, layer ->
         when (previousLayer) {
            is ColorLayer -> colorLayerParametersBinder.unbind(previousLayer, parameters)
         }

         parameters.clear()

         when (layer) {
            is ColorLayer -> colorLayerParametersBinder.bind(layer, parameters) { fire(RedrawMapRequestEvent) }
         }
      }
   }

   override val root = find<ParametersTableFragment>(ParametersTableFragment::parameters to parameters).root
}