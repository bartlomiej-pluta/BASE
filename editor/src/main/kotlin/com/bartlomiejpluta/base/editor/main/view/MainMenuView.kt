package com.bartlomiejpluta.base.editor.main.view

import com.bartlomiejpluta.base.editor.main.controller.MainController
import com.bartlomiejpluta.base.editor.project.context.ProjectContext
import tornadofx.*

class MainMenuView : View() {
   private val mainController: MainController by di()
   private val projectContext: ProjectContext by di()

   override val root = menubar {
      menu("File") {
         menu("New") {
            item("Project...") {
               action {
                  mainController.createEmptyProject()
               }
            }

            item("Map...") {
               enableWhen(projectContext.projectProperty.isNotNull)
               action {
                  mainController.createEmptyMap()
               }
            }
         }

         menu("Open") {
            item("Project...") {
               action {
                  mainController.loadProject()
               }
            }
         }
      }

      menu("Edit") {
         item("Undo")
         item("Redo")
      }
   }
}