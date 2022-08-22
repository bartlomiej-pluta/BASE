package com.bartlomiejpluta.base.editor.main.view

import com.bartlomiejpluta.base.editor.code.build.pipeline.BuildPipelineService
import com.bartlomiejpluta.base.editor.main.controller.MainController
import com.bartlomiejpluta.base.editor.process.runner.app.ApplicationRunner
import com.bartlomiejpluta.base.editor.project.context.ProjectContext
import org.kordamp.ikonli.javafx.FontIcon
import tornadofx.*

class MainMenuView : View() {
   private val mainController: MainController by di()
   private val projectContext: ProjectContext by di()
   private val buildPipelineService: BuildPipelineService by di()
   private val applicationRunner: ApplicationRunner by di()

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
            item("Map...") {
               action {
                  mainController.importMap()
               }
            }

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

            item("Character Set...") {
               action {
                  mainController.importCharacterSet()
               }
            }
         }
      }

      menu("Build") {
         enableWhen(projectContext.projectProperty.isNotNull)

         item("Build project") {
            graphic = FontIcon("fa-gavel")

            enableWhen(buildPipelineService.isRunningProperty.not().and(applicationRunner.isRunningProperty.not()))

            action {
               buildPipelineService.build()
            }
         }

         item("Run") {
            graphic = FontIcon("fa-play")

            enableWhen(buildPipelineService.isRunningProperty.not().and(applicationRunner.isRunningProperty.not()))

            action {
               applicationRunner.run()
            }
         }

         item("Rebuild & Run") {
            enableWhen(buildPipelineService.isRunningProperty.not().and(applicationRunner.isRunningProperty.not()))

            action {
               buildPipelineService.clean()
               applicationRunner.run()
            }
         }

         item("Terminate") {
            graphic = FontIcon("fa-stop")

            enableWhen(applicationRunner.processProperty.isNotNull)

            action {
               applicationRunner.terminate()
            }
         }

         item("Clean build") {
            graphic = FontIcon("fa-trash")

            action {
               buildPipelineService.clean()
            }
         }
      }
   }
}