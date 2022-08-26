package com.bartlomiejpluta.base.editor.autotile.asset

import com.bartlomiejpluta.base.editor.asset.model.GraphicAsset
import com.bartlomiejpluta.base.editor.autotile.model.AutoTile
import com.bartlomiejpluta.base.editor.project.model.Project

class AutoTileAsset(project: Project, uid: String, source: String, name: String) :
   GraphicAsset(project.autoTilesDirectoryProperty, uid, source, name, AutoTile.ROWS, AutoTile.COLUMNS)