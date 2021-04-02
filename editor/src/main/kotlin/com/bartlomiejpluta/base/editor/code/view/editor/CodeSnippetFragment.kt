package com.bartlomiejpluta.base.editor.code.view.editor

import com.bartlomiejpluta.base.editor.code.viewmodel.CodeVM
import tornadofx.*

class CodeSnippetFragment : Fragment("Enter code") {
   private val editor = find<CodeSnippetView>()
   private val codeVM = find<CodeVM>()

   private var onCompleteConsumer: ((String) -> Unit)? = null

   fun onComplete(consumer: (String) -> Unit) {
      this.onCompleteConsumer = consumer
   }

   override val root = borderpane {
      setPrefSize(640.0, 480.0)

      center = editor.root

      bottom = buttonbar {
         button("Apply") {
            action {
               onCompleteConsumer?.let { it(codeVM.code) }
               editor.shutdown()
               close()
            }
         }

         button("Cancel") {
            action {
               editor.shutdown()
               close()
            }
         }
      }
   }
}