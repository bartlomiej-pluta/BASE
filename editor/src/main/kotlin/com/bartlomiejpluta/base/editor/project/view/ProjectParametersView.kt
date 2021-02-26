package com.bartlomiejpluta.base.editor.project.view

import com.bartlomiejpluta.base.editor.common.parameter.model.StringParameter
import com.bartlomiejpluta.base.editor.common.parameter.view.ParametersTableFragment
import com.bartlomiejpluta.base.editor.project.context.ProjectContext
import javafx.beans.property.SimpleStringProperty
import tornadofx.View
import tornadofx.observableListOf

class ProjectParametersView : View() {
   private val projectContext: ProjectContext by di()
   private val name = SimpleStringProperty()

   private val parameters = observableListOf(
      StringParameter("name", "", onCommit = { _, _, submit ->
         submit()
         projectContext.save()
      }).apply { bindBidirectional(name) }
   )

   init {
      projectContext.projectProperty.addListener { _, _, project ->
         project?.let {
            name.bindBidirectional(it.nameProperty)
         }
      }
   }

   override val root = find<ParametersTableFragment>(ParametersTableFragment::parameters to parameters).root
}