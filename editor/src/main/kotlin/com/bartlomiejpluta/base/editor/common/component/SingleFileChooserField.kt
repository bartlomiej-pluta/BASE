package com.bartlomiejpluta.base.editor.common.component

import javafx.beans.property.ObjectProperty
import javafx.beans.property.SimpleObjectProperty
import javafx.beans.property.SimpleStringProperty
import javafx.scene.layout.HBox
import tornadofx.*
import java.io.File

class SingleFileChooserField(
   private val fileProperty: ObjectProperty<File> = SimpleObjectProperty(),
   var type: Type = Type.FILE,
   var validationContext: ValidationContext? = null,
   op: SingleFileChooserField.() -> Unit = {}
) : HBox() {
   private val filePath = SimpleStringProperty()
   private var openDialog: (() -> List<File>) = { chooseFile("File Location", emptyArray()) }

   fun dialogFormat(op: () -> List<File>) {
      this.openDialog = op
   }

   init {
      textfield(filePath) {
         trimWhitespace()

         validationContext?.addValidator(this, filePath) {
            if (it.isNullOrBlank()) {
               error("Field is required")
            }

            val file = File(it!!)
            when {
               !file.exists() -> error("Provide valid path to the ${type.name.toLowerCase()}")
               type == Type.FILE && file.isDirectory -> error("Expected file, but directory is provided")
               type == Type.DIRECTORY && file.isFile -> error("Expected directory, but file is provided")
               else -> null
            }
         }
      }

      button("Choose") {
         action {
            filePath.value = openDialog().getOrNull(0)?.absolutePath
         }
      }

      filePath.addListener { _, _, value ->
         value?.let {
            fileProperty.value = File(it)
         }
      }

      op(this)
   }

   enum class Type {
      FILE,
      DIRECTORY
   }
}