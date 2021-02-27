package com.bartlomiejpluta.base.editor.code.view.select

import com.bartlomiejpluta.base.editor.code.model.FileSystemNode
import com.bartlomiejpluta.base.editor.project.context.ProjectContext
import javafx.beans.binding.Bindings.createBooleanBinding
import javafx.beans.property.SimpleObjectProperty
import tornadofx.*
import java.io.File

class SelectJavaClassFragment : Fragment("Select Java Class") {
   private val projectContext: ProjectContext by di()
   private val rootNode = projectContext.project!!.codeFSNode
   private val selection = SimpleObjectProperty<FileSystemNode>()

   private val selectJavaClassView = find<SelectJavaClassView>(
      SelectJavaClassView::rootNode to rootNode,
      SelectJavaClassView::selection to selection
   )

   private val isFile = createBooleanBinding({ selection.value?.isFile ?: false }, selection)

   private var onCompleteConsumer: ((String) -> Unit)? = null

   fun onComplete(onCompleteConsumer: ((String) -> Unit)) {
      this.onCompleteConsumer = onCompleteConsumer
   }

   override val root = vbox {
      this += selectJavaClassView.root
      buttonbar {
         button("Ok") {
            enableWhen(isFile)

            action {
               selection.value?.let { node ->
                  onCompleteConsumer?.let { consumer ->
                     consumer(formatClassName(node.file))
                     close()
                  }
               }
            }
         }

         button("Cancel") {
            action {
               close()
            }
         }
      }
   }

   private fun formatClassName(file: File) = file
      .relativeTo(rootNode.file)
      .toPath()
      .normalize()
      .joinToString(".")
      .substringBeforeLast(".")
}