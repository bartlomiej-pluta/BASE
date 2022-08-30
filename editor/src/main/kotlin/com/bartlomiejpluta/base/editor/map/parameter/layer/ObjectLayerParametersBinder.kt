package com.bartlomiejpluta.base.editor.map.parameter.layer

import com.bartlomiejpluta.base.editor.code.model.CodeType
import com.bartlomiejpluta.base.editor.common.parameter.model.CodeSnippetParameter
import com.bartlomiejpluta.base.editor.common.parameter.model.Parameter
import com.bartlomiejpluta.base.editor.map.model.layer.ObjectLayer
import com.bartlomiejpluta.base.editor.project.model.Project
import javafx.collections.ObservableList
import org.springframework.stereotype.Component

@Component
class ObjectLayerParametersBinder : LayerParametersBinder<ObjectLayer> {
   override fun bind(layer: ObjectLayer, parameters: ObservableList<Parameter<*>>, project: Project, onCommit: () -> Unit) {
      val imports = CodeSnippetParameter("javaImports", layer.javaImports, CodeType.JAVA, "Provide Java imports") { _, _, submit ->
         onCommit()
         submit()
      }

      imports.bindBidirectional(layer.javaImportsProperty)

      parameters.addAll(imports)
   }
}