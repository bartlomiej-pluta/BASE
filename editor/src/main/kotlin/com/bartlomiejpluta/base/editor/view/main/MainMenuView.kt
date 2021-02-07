package com.bartlomiejpluta.base.editor.view.main

import com.bartlomiejpluta.base.editor.controller.main.MainController
import com.bartlomiejpluta.base.editor.view.map.MapSettingsFragment
import tornadofx.*

class MainMenuView : View() {
   private val mainController: MainController by di()

   override val root = menubar {
      menu("File") {
         menu("New") {
            item("Project...")
            item("Map...") {
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