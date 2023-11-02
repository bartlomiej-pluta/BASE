package com.bartlomiejpluta.base.editor.map.parameter.layer

import com.bartlomiejpluta.base.editor.common.parameter.model.Parameter
import com.bartlomiejpluta.base.editor.map.model.layer.ObjectLayer
import com.bartlomiejpluta.base.editor.project.model.Project
import javafx.collections.ObservableList
import org.springframework.stereotype.Component

@Component
class ObjectLayerParametersBinder : LayerParametersBinder<ObjectLayer> {
   override fun bind(layer: ObjectLayer, parameters: ObservableList<Parameter<*>>, project: Project, onCommit: () -> Unit) {

   }
}