package com.bartlomiejpluta.base.editor.project.view

import com.bartlomiejpluta.base.editor.project.viewmodel.ProjectVM
import tornadofx.*

class ProjectSettingsFragment : Fragment("Project Settings") {
   private val projectVM = find<ProjectVM>(FX.defaultScope)

   var result = false
      private set

   override val root = form {
      fieldset("Project Settings") {
         field("Project Name") {
            textfield(projectVM.nameProperty) {
               required()
               whenDocked { requestFocus() }
            }
         }
      }

      buttonbar {
         button("Ok") {
            action {
               projectVM.commit {
                  result = true
                  close()
               }
            }

            shortcut("Enter")
         }

         button("Reset") {
            action { projectVM.rollback() }
         }

         button("Cancel") {
            action { close() }
         }
      }
   }
}