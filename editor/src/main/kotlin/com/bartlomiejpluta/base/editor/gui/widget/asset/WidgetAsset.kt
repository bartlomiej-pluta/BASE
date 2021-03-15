package com.bartlomiejpluta.base.editor.gui.widget.asset

import com.bartlomiejpluta.base.editor.asset.model.Asset
import com.bartlomiejpluta.base.editor.project.model.Project

class WidgetAsset(project: Project, uid: String, name: String) :
   Asset(project.widgetsDirectoryProperty, uid, "$uid.xml", name)