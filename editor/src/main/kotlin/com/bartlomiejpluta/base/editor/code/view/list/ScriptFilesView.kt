package com.bartlomiejpluta.base.editor.code.view.list

import com.bartlomiejpluta.base.editor.code.api.APIProvider
import com.bartlomiejpluta.base.editor.code.component.ScriptFileTreeCell
import com.bartlomiejpluta.base.editor.file.model.FileNode
import com.bartlomiejpluta.base.editor.file.model.FileType
import com.bartlomiejpluta.base.editor.file.model.PseudoFileNode
import com.bartlomiejpluta.base.editor.main.controller.MainController
import com.bartlomiejpluta.base.editor.project.context.ProjectContext
import javafx.scene.control.TextInputDialog
import javafx.scene.control.TreeItem
import javafx.scene.control.TreeView
import javafx.scene.input.MouseButton
import tornadofx.View
import tornadofx.expandAll
import tornadofx.populate
import tornadofx.treeview
import java.io.File

class ScriptFilesView : View() {
   private val projectContext: ProjectContext by di()
   private val mainController: MainController by di()
   private val apiProvider: APIProvider by di()

   init {
      projectContext.projectProperty.addListener { _, _, project ->
         project?.let {
            val rootNode = PseudoFileNode.emptyRoot().apply {
               children += apiProvider.apiNode
               children += it.codeFSNode
            }

            treeView.root = TreeItem(rootNode)
            treeView.populate { item -> item.value?.children }
            root.root.expandAll()
         }
      }
   }

   private val treeView: TreeView<FileNode> = treeview {
      isShowRoot = false

      setCellFactory {
         ScriptFileTreeCell(this@ScriptFilesView::onCreate, mainController::closeScript)
      }

      setOnMouseClicked { event ->
         if (event.button == MouseButton.PRIMARY && event.clickCount == 2) {
            selectionModel?.selectedItem?.value
               .takeIf { it?.type == FileType.FILE }
               ?.let { mainController.openScript(it) }

            event.consume()
         }
      }
   }

   override val root = treeView

   private fun onCreate(fsNode: FileNode) {
      TextInputDialog().apply {
         width = 300.0
         contentText = "Class name"
         title = "New class"
      }
         .showAndWait()
         .map { it.replace(".", File.separator) + ".java" }
         .map { fsNode.createNode(it) }
         .ifPresent { mainController.openScript(it) }
   }
}