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
                  mainController.openProject()
               }
            }
         }
      }

      menu("Project") {
         item("Import Tile Set...") {
            enableWhen(projectContext.projectProperty.isNotNull)

            action {
               mainController.importTileSet()
            }
         }

         item("Import Image...") {
            enableWhen(projectContext.projectProperty.isNotNull)

            action {
               mainController.importImage()
            }
         }
      }

      menu("Edit") {
         item("Undo")
         item("Redo")
      }
   }
}