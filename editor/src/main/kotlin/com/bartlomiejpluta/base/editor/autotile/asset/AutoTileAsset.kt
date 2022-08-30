package com.bartlomiejpluta.base.editor.autotile.asset

import com.bartlomiejpluta.base.editor.asset.model.GraphicAsset
import com.bartlomiejpluta.base.editor.autotile.model.AutoTileLayout
import com.bartlomiejpluta.base.editor.project.model.Project

class AutoTileAsset(
   project: Project,
   uid: String,
   source: String,
   name: String,
   rows: Int,
   columns: Int,
   val layout: AutoTileLayout
) : GraphicAsset(project.autoTilesDirectoryProperty, uid, source, name, rows, columns)