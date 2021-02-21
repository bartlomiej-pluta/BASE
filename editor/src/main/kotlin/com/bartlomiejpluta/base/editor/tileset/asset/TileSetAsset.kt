package com.bartlomiejpluta.base.editor.tileset.asset

import com.bartlomiejpluta.base.editor.asset.model.GraphicAsset
import com.bartlomiejpluta.base.editor.project.model.Project

class TileSetAsset(project: Project, uid: String, source: String, name: String, val rows: Int, val columns: Int) :
   GraphicAsset(project.tileSetsDirectoryProperty, uid, source, name)
