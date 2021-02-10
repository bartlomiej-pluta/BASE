package com.bartlomiejpluta.base.editor.map.view

import com.bartlomiejpluta.base.editor.map.viewmodel.GameMapBuilderVM
import org.kordamp.ikonli.javafx.FontIcon
import tornadofx.Wizard

class MapCreationWizard : Wizard("New Map", "Provide map information") {
   private val mapBuilderVM = find<GameMapBuilderVM>()

   init {
      graphic = FontIcon("fa-map").apply {
         iconSize = 40
      }

      add(MapCreationBasicDataView::class)
      add(MapTileSetSelectionView::class)
   }

   override fun onSave() {
      if(mapBuilderVM.commit()) {
         super.onSave()
      }
   }
}