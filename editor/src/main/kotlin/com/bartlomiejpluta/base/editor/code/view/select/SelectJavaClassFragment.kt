package com.bartlomiejpluta.base.editor.code.view.select

import com.bartlomiejpluta.base.editor.code.api.APIProvider
import com.bartlomiejpluta.base.editor.file.model.*
import com.bartlomiejpluta.base.editor.project.context.ProjectContext
import javafx.beans.binding.Bindings.createBooleanBinding
import javafx.beans.property.SimpleObjectProperty
import tornadofx.*
import java.nio.file.Path

class SelectJavaClassFragment : Fragment("Select Java Class") {
   private val projectContext: ProjectContext by di()
   private val apiProvider: APIProvider by di()

   private val codeFSNode = projectContext.project!!.codeFSNode
   private val rootNode = PseudoFileNode.emptyRoot().apply {
      children += apiProvider.apiNode
      children += codeFSNode
   }

   private val selection = SimpleObjectProperty<FileNode>()

   private val selectJavaClassView = find<SelectJavaClassView>(
      SelectJavaClassView::rootNode to rootNode,
      SelectJavaClassView::selection to selection
   )

   private val isFile = createBooleanBinding({ selection.value?.type == FileType.FILE }, selection)

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
                     consumer(formatClassName(node))
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

   private fun formatClassName(node: FileNode) = when (node) {
      is FileSystemNode -> node.file.relativeTo(codeFSNode.file).toPath()
      is ResourceFileNode -> Path.of(node.absolutePath.substringAfter("/${APIProvider.API_DIR}"))
      else -> throw IllegalStateException("Unsupported file node type")
   }.normalize().joinToString(".").substringBeforeLast(".")
}