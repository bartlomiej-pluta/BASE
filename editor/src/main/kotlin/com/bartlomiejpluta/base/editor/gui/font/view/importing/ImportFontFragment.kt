package com.bartlomiejpluta.base.editor.gui.font.view.importing

import com.bartlomiejpluta.base.editor.common.component.SingleFileChooserField
import com.bartlomiejpluta.base.editor.gui.font.asset.FontAssetData
import com.bartlomiejpluta.base.editor.gui.font.viewmodel.FontAssetDataVM
import javafx.stage.FileChooser
import tornadofx.*

class ImportFontFragment : Fragment("Import Font") {
   private val dataVM = find<FontAssetDataVM>()

   private var onCompleteConsumer: ((FontAssetData) -> Unit)? = null

   fun onComplete(consumer: (FontAssetData) -> Unit) {
      this.onCompleteConsumer = consumer
   }

   override val root = form {
      fieldset("Import Font") {
         field("Font Name") {
            textfield(dataVM.nameProperty) {
               requestFocus()
               required()
               trimWhitespace()
            }
         }

         field("Font File") {
            this += SingleFileChooserField(dataVM.fileProperty) {
               type = SingleFileChooserField.Type.FILE
               validationContext = dataVM.validationContext

               dialogFormat {
                  chooseFile("Font File Location", arrayOf(FileChooser.ExtensionFilter("TTF File", "*.ttf")))
               }
            }
         }
      }

      buttonbar {
         button("Ok") {
            action {
               if (dataVM.commit()) {
                  onCompleteConsumer?.let { it(dataVM.item) }
                  close()
               }
            }
         }

         button("Cancel") {
            action { close() }
         }
      }
   }
}