package com.bartlomiejpluta.base.editor.map.view.editor

import com.bartlomiejpluta.base.editor.code.model.CodeType
import com.bartlomiejpluta.base.editor.common.parameter.model.CodeSnippetParameter
import com.bartlomiejpluta.base.editor.common.parameter.model.IntegerParameter
import com.bartlomiejpluta.base.editor.common.parameter.model.JavaClassParameter
import com.bartlomiejpluta.base.editor.common.parameter.view.ParametersTableFragment
import com.bartlomiejpluta.base.editor.map.viewmodel.GameMapVM
import tornadofx.View
import tornadofx.observableListOf

class MapParameters : View() {
   private val mapVM = find<GameMapVM>()

   private val parameters = observableListOf(
      IntegerParameter("rows", mapVM.rows, 1, 100).apply { bindBidirectional(mapVM.item.rowsProperty) },
      IntegerParameter("columns", mapVM.columns, 1, 100).apply { bindBidirectional(mapVM.item.columnsProperty) },
      JavaClassParameter("handler", mapVM.handler).apply { bindBidirectional(mapVM.item.handlerProperty) },
      CodeSnippetParameter("javaImports", mapVM.javaImports, CodeType.JAVA).apply { bindBidirectional(mapVM.item.javaImportsProperty) }
   )

   override val root = find<ParametersTableFragment>(ParametersTableFragment::parameters to parameters).root
}