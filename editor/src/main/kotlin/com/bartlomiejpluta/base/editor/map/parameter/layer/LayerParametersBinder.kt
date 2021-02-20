package com.bartlomiejpluta.base.editor.map.parameter.layer

import com.bartlomiejpluta.base.editor.common.parameter.model.Parameter
import com.bartlomiejpluta.base.editor.map.model.layer.Layer
import com.bartlomiejpluta.base.editor.project.model.Project
import javafx.collections.ObservableList

interface LayerParametersBinder<T : Layer> {
   fun bind(layer: T, parameters: ObservableList<Parameter<*>>, project: Project, onCommit: () -> Unit)
}