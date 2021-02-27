package com.bartlomiejpluta.base.editor.project.view

import com.bartlomiejpluta.base.editor.common.parameter.model.JavaClassParameter
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
      }).apply { bindBidirectional(name) },

      // TODO: It should never be null so it is required Project to have a gameClass set
      // from its initialization via New project dialog.
      // In that case, the initialValue will ever be a projectContext.project.gameClass
      // The "Select class..." placeholder is temporary and it should never be here, because
      // the game engine would treat the "Select class..." string as a game class name.
      JavaClassParameter("gameClass", "Select class...")
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