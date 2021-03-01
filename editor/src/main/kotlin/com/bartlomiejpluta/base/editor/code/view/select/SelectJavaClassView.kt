package com.bartlomiejpluta.base.editor.code.view.select

import com.bartlomiejpluta.base.editor.file.model.FileNode
import com.bartlomiejpluta.base.editor.file.model.FileType
import javafx.beans.property.Property
import javafx.scene.control.TreeItem
import org.kordamp.ikonli.javafx.FontIcon
import tornadofx.*

class SelectJavaClassView : View() {
   val rootNode: FileNode by param()
   val selection: Property<FileNode> by param()

   private val treeView = treeview<FileNode> {
      root = TreeItem(rootNode)
      isShowRoot = false

      populate {
         it.value?.children
      }

      cellFormat {
         text = it.nameWithoutExtension
         graphic = when (it.type) {
            FileType.FILE -> FontIcon("fa-cube")
            FileType.DIRECTORY -> FontIcon("fa-folder")
         }
      }

      bindSelected(selection)
   }

   init {
      treeView.root.expandAll()
   }

   override val root = treeView
}