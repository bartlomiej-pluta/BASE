package com.bartlomiejpluta.base.editor.map.view.editor

import com.bartlomiejpluta.base.editor.command.context.UndoableScope
import com.bartlomiejpluta.base.editor.command.service.UndoRedoService
import com.bartlomiejpluta.base.editor.map.viewmodel.GameMapVM
import com.bartlomiejpluta.base.editor.util.fx.TextFieldUtil
import tornadofx.*

class MapSettingsFragment : Fragment("Map Settings") {
   override val scope = super.scope as UndoableScope
   private val undoRedoService: UndoRedoService by di()

   private val mapVM = find<GameMapVM>()

   var result = false
      private set

   override val root = form {
      fieldset("Map Settings") {
         field("Rows") {

            spinner(min = 1, max = 100, property = mapVM.rowsProperty, editable = true) {
               required()
               editor.textFormatter = TextFieldUtil.integerFormatter(mapVM.rows)

               validator {
                  when (it?.toInt()) {
                     in 1..50 -> null
                     in 50..100 -> warning("The map sizes over 50 can impact game performance")
                     else -> error("The map size must be between 1 and 100")
                  }
               }
            }
         }

         field("Columns") {
            spinner(min = 1, max = 100, property = mapVM.columnsProperty, editable = true) {
               required()
               editor.textFormatter = TextFieldUtil.integerFormatter(mapVM.columns)

               validator {
                  when (it?.toInt()) {
                     in 1..50 -> null
                     in 50..100 -> warning("The map sizes over 50 can impact game performance")
                     else -> error("The map size must be between 1 and 100")
                  }
               }
            }
         }

         label("Warning: Submitting the form will clear related undo/redo stacks!")
      }

      buttonbar {
         button("Ok") {
            shortcut("Enter")

            action {
               mapVM.commit {
                  result = true
                  undoRedoService.clear(scope)
                  close()
               }
            }
         }

         button("Reset") {
            action { mapVM.rollback() }
         }

         button("Cancel") {
            action { close() }
         }
      }
   }
}