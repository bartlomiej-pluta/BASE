package com.bartlomiejpluta.base.editor.map.asset

import com.bartlomiejpluta.base.editor.asset.model.Asset

// TODO(Add tileSetUID field)
data class GameMapAsset(
   override val uid: String,
   val name: String,
   val rows: Int,
   val columns: Int,
) : Asset {
   override val source = "$uid.dat"
}