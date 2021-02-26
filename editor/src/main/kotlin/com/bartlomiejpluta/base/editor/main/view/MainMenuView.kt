package com.bartlomiejpluta.base.editor.main.view

import com.bartlomiejpluta.base.editor.code.build.pipeline.BuildPipelineService
import com.bartlomiejpluta.base.editor.main.controller.MainController
import com.bartlomiejpluta.base.editor.project.context.ProjectContext
import tornadofx.*

class MainMenuView : View() {
   private val mainController: MainController by di()
   private val projectContext: ProjectContext by di()
   private val buildPipelineService: BuildPipelineService by di()

   override val root = menubar {
      menu("File") {
         item("New project...") {
            action {
               mainController.createEmptyProject()
            }
         }


         item("Open project...") {
            action {
               mainController.openProject()
            }
         }
      }

      menu("Project") {
         enableWhen(projectContext.projectProperty.isNotNull)

         menu("Create") {
            item("Map...") {
               action {
                  mainController.createEmptyMap()
               }
            }
         }

         menu("Import") {
            item("Tile Set...") {
               action {
                  mainController.importTileSet()
               }
            }

            item("Image...") {
               action {
                  mainController.importImage()
               }
            }
         }
      }

      menu("Build") {
         enableWhen(projectContext.projectProperty.isNotNull)

         item("Compile") {
            enableWhen(buildPipelineService.isRunningProperty.not())
            action {
               buildPipelineService.build()
            }
         }
      }
   }
}