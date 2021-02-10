package com.bartlomiejpluta.base.editor.map.viewmodel

import com.bartlomiejpluta.base.editor.map.model.map.GameMapBuilder
import tornadofx.ItemViewModel
import tornadofx.getValue
import tornadofx.setValue

class GameMapBuilderVM : ItemViewModel<GameMapBuilder>(GameMapBuilder()) {
   val tileSetProperty = bind(GameMapBuilder::tileSetProperty, autocommit = true)
   var tileSet by tileSetProperty

   val nameProperty = bind(GameMapBuilder::nameProperty, autocommit = true)
   var name by nameProperty

   val rowsProperty = bind(GameMapBuilder::rowsProperty, autocommit = true)
   var rows by rowsProperty

   val columnsProperty = bind(GameMapBuilder::columnsProperty, autocommit = true)
   var columns by columnsProperty
}