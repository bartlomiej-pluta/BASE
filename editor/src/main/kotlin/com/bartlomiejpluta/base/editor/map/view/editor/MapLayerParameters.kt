package com.bartlomiejpluta.base.editor.map.view.editor

import com.bartlomiejpluta.base.editor.common.parameter.model.Parameter
import com.bartlomiejpluta.base.editor.common.parameter.view.ParametersTableFragment
import com.bartlomiejpluta.base.editor.event.RedrawMapRequestEvent
import com.bartlomiejpluta.base.editor.map.model.layer.AutoTileLayer
import com.bartlomiejpluta.base.editor.map.model.layer.ColorLayer
import com.bartlomiejpluta.base.editor.map.model.layer.ImageLayer
import com.bartlomiejpluta.base.editor.map.model.layer.TileLayer
import com.bartlomiejpluta.base.editor.map.parameter.layer.AutoTileLayerParametersBinder
import com.bartlomiejpluta.base.editor.map.parameter.layer.ColorLayerParametersBinder
import com.bartlomiejpluta.base.editor.map.parameter.layer.ImageLayerParametersBinder
import com.bartlomiejpluta.base.editor.map.parameter.layer.TileLayerParametersBinder
import com.bartlomiejpluta.base.editor.map.viewmodel.EditorStateVM
import com.bartlomiejpluta.base.editor.project.context.ProjectContext
import tornadofx.View
import tornadofx.observableListOf

class MapLayerParameters : View() {
   private val editorStateVM = find<EditorStateVM>()
   private val projectContext: ProjectContext by di()

   // For some reason Spring does not want to autowire a list of beans
   // of LayerParametersBinder<> type
   private val colorLayerParametersBinder: ColorLayerParametersBinder by di()
   private val imageLayerParametersBinder: ImageLayerParametersBinder by di()
   private val tileLayerParametersBinder: TileLayerParametersBinder by di()
   private val autoTileLayerParametersBinder: AutoTileLayerParametersBinder by di()

   private val parameters = observableListOf<Parameter<*>>()

   init {
      editorStateVM.selectedLayerProperty.addListener { _, _, layer ->
         parameters.forEach(Parameter<*>::unbindBidirectional)
         parameters.clear()

         when (layer) {
            is TileLayer -> tileLayerParametersBinder.bind(layer, parameters, projectContext.project!!) {
               fire(RedrawMapRequestEvent)
            }

            is AutoTileLayer -> autoTileLayerParametersBinder.bind(layer, parameters, projectContext.project!!) {
               fire(RedrawMapRequestEvent)
            }

            is ColorLayer -> colorLayerParametersBinder.bind(layer, parameters, projectContext.project!!) {
               fire(RedrawMapRequestEvent)
            }

            is ImageLayer -> imageLayerParametersBinder.bind(layer, parameters, projectContext.project!!) {
               fire(RedrawMapRequestEvent)
            }
         }
      }
   }

   override val root = find<ParametersTableFragment>(ParametersTableFragment::parameters to parameters).root
}