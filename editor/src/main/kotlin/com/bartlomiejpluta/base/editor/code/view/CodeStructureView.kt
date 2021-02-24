package com.bartlomiejpluta.base.editor.code.view

import com.bartlomiejpluta.base.editor.code.component.CodeStructureItemTreeCell
import com.bartlomiejpluta.base.editor.code.model.FileSystemNode
import com.bartlomiejpluta.base.editor.main.controller.MainController
import com.bartlomiejpluta.base.editor.project.context.ProjectContext
import javafx.scene.control.TreeItem
import javafx.scene.control.TreeView
import javafx.scene.input.MouseButton
import tornadofx.View
import tornadofx.expandAll
import tornadofx.populate
import tornadofx.treeview

class CodeStructureView : View() {
   private val projectContext: ProjectContext by di()
   private val mainController: MainController by di()

   init {
      projectContext.projectProperty.addListener { _, _, project ->
         project?.let {
            treeView.root = TreeItem(FileSystemNode(it.codeDirectory))
            treeView.populate { item -> item.value?.children }
            root.root.expandAll()
         }
      }
   }

   private val treeView: TreeView<FileSystemNode> = treeview {
      setCellFactory { CodeStructureItemTreeCell() }

      setOnMouseClicked { event ->
         if (event.button == MouseButton.PRIMARY && event.clickCount == 2) {
            selectionModel?.selectedItem?.value
               .takeIf { it?.isFile ?: false }
               ?.let { mainController.openScript(it.file) }

            event.consume()
         }
      }
   }

   override val root = treeView
}