package com.bartlomiejpluta.base.editor.code.view.select

import com.bartlomiejpluta.base.editor.code.model.FileSystemNode
import javafx.beans.property.Property
import javafx.scene.control.TreeItem
import org.kordamp.ikonli.javafx.FontIcon
import tornadofx.*

class SelectJavaClassView : View() {
   val rootNode: FileSystemNode by param()
   val selection: Property<FileSystemNode> by param()

   private val treeView = treeview<FileSystemNode> {
      root = TreeItem(rootNode)

      populate {
         it.value?.children
      }

      cellFormat {
         text = it.file.nameWithoutExtension
         graphic = when {
            it.file.isFile -> FontIcon("fa-cube")
            else -> FontIcon("fa-folder")
         }
      }

      bindSelected(selection)
   }

   init {
      treeView.root.expandAll()
   }

   override val root = treeView
}