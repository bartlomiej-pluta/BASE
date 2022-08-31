package com.bartlomiejpluta.base.editor.map.viewmodel

import com.bartlomiejpluta.base.editor.map.model.map.GameMapBuilder
import tornadofx.ItemViewModel
import tornadofx.getValue
import tornadofx.setValue

class GameMapBuilderVM : ItemViewModel<GameMapBuilder>(GameMapBuilder()) {
   val nameProperty = bind(GameMapBuilder::nameProperty, autocommit = true)
   var name by nameProperty

   val tileWidthProperty = bind(GameMapBuilder::tileWidthProperty, autocommit = true)
   var tileWidth by tileWidthProperty

   val tileHeightProperty = bind(GameMapBuilder::tileHeightProperty, autocommit = true)
   var tileHeight by tileHeightProperty

   val rowsProperty = bind(GameMapBuilder::rowsProperty, autocommit = true)
   var rows by rowsProperty

   val columnsProperty = bind(GameMapBuilder::columnsProperty, autocommit = true)
   var columns by columnsProperty

   val handlerProperty = bind(GameMapBuilder::handlerProperty, autocommit = true)
   var handler by handlerProperty

   val handlerBaseClassProperty = bind(GameMapBuilder::handlerBaseClassProperty, autocommit = true)
   var handlerBaseClass by handlerBaseClassProperty

   val fileProperty = bind(GameMapBuilder::fileProperty, autocommit = true)
   var file by fileProperty
}