package com.bartlomiejpluta.base.editor.characterset.asset

import com.bartlomiejpluta.base.editor.asset.model.Asset
import com.bartlomiejpluta.base.editor.project.model.Project

class CharacterSet(project: Project, uid: String, source: String, name: String, val rows: Int, val columns: Int) :
   Asset(project.characterSetsDirectoryProperty, uid, source, name)