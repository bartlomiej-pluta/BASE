package com.bartlomiejpluta.base.editor.map.view.wizard

import com.bartlomiejpluta.base.editor.code.view.select.SelectJavaClassFragment
import com.bartlomiejpluta.base.editor.map.model.map.GameMapBuilder
import com.bartlomiejpluta.base.editor.map.viewmodel.GameMapBuilderVM
import com.bartlomiejpluta.base.editor.util.fx.TextFieldUtil
import tornadofx.*

class MapCreationFragment : Fragment("Basic Data") {
   private val mapBuilderVM = find<GameMapBuilderVM>()
   private var onCompleteConsumer: ((GameMapBuilder) -> Unit)? = null

   override val complete = mapBuilderVM.valid(
      mapBuilderVM.nameProperty,
      mapBuilderVM.rowsProperty,
      mapBuilderVM.columnsProperty,
      mapBuilderVM.handlerProperty
   )

   fun onComplete(consumer: (GameMapBuilder) -> Unit) {
      this.onCompleteConsumer = consumer
   }


   override val root = form {
      fieldset("Map Settings") {
         field("Map name") {
            textfield(mapBuilderVM.nameProperty) {
               required()
               whenDocked { requestFocus() }
            }
         }

         field("Tile Width") {
            spinner(min = 1, property = mapBuilderVM.tileWidthProperty, editable = true) {
               required()
               editor.textFormatter = TextFieldUtil.integerFormatter(mapBuilderVM.tileWidth)

               validator {
                  if (it?.toInt()?.let { value -> value <= 0 } != false) {
                     error("The tile width must be greater than 0")
                  }

                  null
               }
            }
         }

         field("Tile Height") {
            spinner(min = 1, property = mapBuilderVM.tileHeightProperty, editable = true) {
               required()
               editor.textFormatter = TextFieldUtil.integerFormatter(mapBuilderVM.tileHeight)

               validator {
                  if (it?.toInt()?.let { value -> value <= 0 } != false) {
                     error("The tile height must be greater than 0")
                  }

                  null
               }
            }
         }

         field("Rows") {
            spinner(min = 1, max = 100, property = mapBuilderVM.rowsProperty, editable = true) {
               required()
               editor.textFormatter = TextFieldUtil.integerFormatter(mapBuilderVM.rows)

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
            spinner(min = 1, max = 100, property = mapBuilderVM.columnsProperty, editable = true) {
               required()
               editor.textFormatter = TextFieldUtil.integerFormatter(mapBuilderVM.columns)

               validator {
                  when (it?.toInt()) {
                     in 1..50 -> null
                     in 50..100 -> warning("The map sizes over 50 can impact game performance")
                     else -> error("The map size must be between 1 and 100")
                  }
               }
            }
         }

         field("Map Handler class") {
            textfield(mapBuilderVM.handlerProperty) {
               required()
               trimWhitespace()
            }
         }

         field("Map Handler base class") {
            hbox {
               textfield(mapBuilderVM.handlerBaseClassProperty) {
                  trimWhitespace()
               }

               button("Select") {
                  action {
                     find<SelectJavaClassFragment>(Scope()).apply {
                        onComplete { className ->
                           mapBuilderVM.handlerBaseClassProperty.value = className
                        }

                        openModal(block = true, resizable = false)
                     }
                  }
               }
            }
         }
      }

      buttonbar {
         button("Create") {
            action {
               if (mapBuilderVM.commit()) {
                  onCompleteConsumer?.let { it(mapBuilderVM.item) }
                  close()
               }
            }
         }

         button("Reset") {
            action { mapBuilderVM.rollback() }
         }


         button("Cancel") {
            action { close() }
         }
      }
   }
}