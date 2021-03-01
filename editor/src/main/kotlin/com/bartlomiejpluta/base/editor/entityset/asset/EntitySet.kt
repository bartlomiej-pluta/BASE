package com.bartlomiejpluta.base.editor.entityset.asset

import com.bartlomiejpluta.base.editor.asset.model.Asset
import com.bartlomiejpluta.base.editor.project.model.Project

class EntitySet(project: Project, uid: String, source: String, name: String, val rows: Int, val columns: Int) :
   Asset(project.entitySetsDirectoryProperty, uid, source, name)