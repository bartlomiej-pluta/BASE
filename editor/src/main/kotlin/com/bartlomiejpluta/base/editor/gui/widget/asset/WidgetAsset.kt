package com.bartlomiejpluta.base.editor.gui.widget.asset

import com.bartlomiejpluta.base.editor.file.model.ScriptAssetFileNode
import com.bartlomiejpluta.base.editor.project.model.Project

class WidgetAsset(project: Project, uid: String, name: String) :
   ScriptAssetFileNode(project.widgetsDirectoryProperty, uid, "xml", name)