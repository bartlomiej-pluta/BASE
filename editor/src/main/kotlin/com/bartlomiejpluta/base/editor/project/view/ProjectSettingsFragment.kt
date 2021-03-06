package com.bartlomiejpluta.base.editor.project.view

import com.bartlomiejpluta.base.editor.project.model.Project
import com.bartlomiejpluta.base.editor.project.viewmodel.ProjectVM
import javafx.beans.binding.Bindings
import javafx.beans.binding.Bindings.createStringBinding
import javafx.beans.property.SimpleStringProperty
import tornadofx.*
import java.io.File

class ProjectSettingsFragment : Fragment("Project Settings") {
   private val projectVM = find<ProjectVM>(FX.defaultScope)
   private val rootDirectory = SimpleStringProperty("")

   private val projectDirectory = SimpleStringProperty("")
   private val directory = createStringBinding({
      File(rootDirectory.value, projectDirectory.value).absolutePath
   }, rootDirectory, projectDirectory)

   private var onCompleteConsumer: ((Project) -> Unit)? = null

   init {
      directory.addListener { _, _, path -> projectVM.sourceDirectoryProperty.value = File(path) }
      projectVM.nameProperty.addListener { _, _, name -> projectDirectory.value = name }
   }

   fun onComplete(consumer: (Project) -> Unit) {
      this.onCompleteConsumer = consumer
   }

   override val root = form {
      fieldset("Project Settings") {
         field("Project Name") {
            textfield(projectVM.nameProperty) {
               required()
               trimWhitespace()
               whenDocked { requestFocus() }
            }
         }

         field("Project Location") {
            hbox {
               textfield(rootDirectory) {
                  trimWhitespace()

                  projectVM.validationContext.addValidator(this, rootDirectory) {
                     when {
                        it.isNullOrBlank() -> error("Field is required")
                        !File(it).exists() -> error("Provide valid path to the direction")
                        else -> null
                     }
                  }
               }

               button("Choose") {
                  action {
                     rootDirectory.value = chooseDirectory("Project Location")?.absolutePath
                  }
               }
            }
         }

         field("Project Directory Name") {
            textfield(projectDirectory) {
               trimWhitespace()

               projectVM.validationContext.addValidator(this, projectDirectory) {
                  when {
                     it.isNullOrBlank() -> error("Field is required")
                     File(directory.value).exists() -> error("The directory ${directory.value} already exists")
                     else -> null
                  }
               }
            }
         }

         label(Bindings.format("Directory:\n%s", directory))

         field("Game Runner class") {
            textfield(projectVM.runnerProperty) {
               required()
               trimWhitespace()
            }
         }
      }

      buttonbar {
         button("Ok") {
            action {
               projectVM.commit {
                  onCompleteConsumer?.let { it(projectVM.item) }
                  close()
               }
            }

            shortcut("Enter")
         }

         button("Cancel") {
            action { close() }
         }
      }
   }
}