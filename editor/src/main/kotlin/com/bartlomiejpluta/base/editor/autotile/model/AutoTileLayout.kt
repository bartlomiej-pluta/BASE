package com.bartlomiejpluta.base.editor.autotile.model

enum class AutoTileLayout(val columns: Int, val rows: Int) {
   LAYOUT_2X2(2, 2),
   LAYOUT_2X3(2, 3);

   override fun toString() = "$columns x $rows"
}