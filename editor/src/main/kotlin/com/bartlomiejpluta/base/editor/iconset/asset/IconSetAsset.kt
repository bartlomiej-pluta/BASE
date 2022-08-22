package com.bartlomiejpluta.base.editor.iconset.asset

import com.bartlomiejpluta.base.editor.asset.model.Asset
import com.bartlomiejpluta.base.editor.project.model.Project

class IconSetAsset(project: Project, uid: String, source: String, name: String, val rows: Int, val columns: Int) :
   Asset(project.iconSetsDirectoryProperty, uid, source, name)