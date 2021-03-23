package com.bartlomiejpluta.base.editor.audio.view.importing

import com.bartlomiejpluta.base.editor.audio.asset.SoundAssetData
import com.bartlomiejpluta.base.editor.audio.viewmodel.SoundAssetDataVM
import com.bartlomiejpluta.base.editor.common.component.SingleFileChooserField
import javafx.stage.FileChooser
import tornadofx.*

class ImportSoundFragment : Fragment("Import Sound") {
   private val dataVM = find<SoundAssetDataVM>()

   private var onCompleteConsumer: ((SoundAssetData) -> Unit)? = null

   fun onComplete(consumer: (SoundAssetData) -> Unit) {
      this.onCompleteConsumer = consumer
   }

   override val root = form {
      fieldset("Import Sound") {
         field("Sound Name") {
            textfield(dataVM.nameProperty) {
               requestFocus()
               required()
               trimWhitespace()
            }
         }

         field("Sound File") {
            this += SingleFileChooserField(dataVM.fileProperty) {
               type = SingleFileChooserField.Type.FILE
               validationContext = dataVM.validationContext

               dialogFormat {
                  chooseFile("Sound File Location", arrayOf(FileChooser.ExtensionFilter("OGG Vorbis File", "*.ogg")))
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