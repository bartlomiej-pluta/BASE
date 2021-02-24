package com.bartlomiejpluta.base.editor.code.view

import com.bartlomiejpluta.base.editor.code.component.CodeStructureItemTreeCell
import com.bartlomiejpluta.base.editor.main.controller.MainController
import com.bartlomiejpluta.base.editor.project.context.ProjectContext
import javafx.scene.control.TreeItem
import javafx.scene.control.TreeView
import tornadofx.*
import java.io.File

class CodeStructureView : View() {
   private val projectContext: ProjectContext by di()
   private val mainController: MainController by di()

   init {
      projectContext.projectProperty.addListener { _, _, project ->
         project?.let {
            treeView.root = TreeItem(it.codeDirectory)
            treeView.populate { item -> item.value?.listFiles()?.toList() }
            root.root.expandAll()
         }
      }
   }

   private val treeView: TreeView<File> = treeview {
      setCellFactory {
         CodeStructureItemTreeCell(this@CodeStructureView::renameFile, this@CodeStructureView::deleteFile)
      }

      setOnMouseClicked { event ->
         if (event.clickCount == 2) {
            selectionModel?.selectedItem?.value
               .takeIf { it?.isFile ?: false }
               ?.let { mainController.openScript(it) }
         }

         event.consume()
      }
   }

   override val root = treeView

   private fun renameFile(file: File, name: String) = file.apply {
      // TODO
   }

   private fun deleteFile(file: File) {
      // TODO
   }
}