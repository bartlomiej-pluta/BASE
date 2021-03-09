package com.bartlomiejpluta.base.editor.gui.font.asset

import com.bartlomiejpluta.base.editor.asset.model.Asset
import com.bartlomiejpluta.base.editor.project.model.Project

class FontAsset(project: Project, uid: String, source: String, name: String) :
   Asset(project.fontsDirectoryProperty, uid, source, name)