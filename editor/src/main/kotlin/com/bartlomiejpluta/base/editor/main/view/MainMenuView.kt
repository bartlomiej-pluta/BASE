package com.bartlomiejpluta.base.editor.main.view

import com.bartlomiejpluta.base.editor.main.controller.MainController
import tornadofx.*

class MainMenuView : View() {
   private val mainController: MainController by di()

   override val root = menubar {
      menu("File") {
         menu("New") {
            item("Project...") {
               action {
                  mainController.createEmptyProject()
               }
            }

            item("Map...") {
               enableWhen(mainController.openProject.isNotNull)
               action {
                  mainController.createEmptyMap()
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