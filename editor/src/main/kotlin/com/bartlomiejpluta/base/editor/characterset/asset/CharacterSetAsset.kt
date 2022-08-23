package com.bartlomiejpluta.base.editor.characterset.asset

import com.bartlomiejpluta.base.editor.asset.model.GraphicAsset
import com.bartlomiejpluta.base.editor.project.model.Project

class CharacterSetAsset(project: Project, uid: String, source: String, name: String, rows: Int, columns: Int) :
   GraphicAsset(project.characterSetsDirectoryProperty, uid, source, name, rows, columns)