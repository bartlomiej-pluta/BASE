package com.bartlomiejpluta.base.editor.map.view.wizard

import com.bartlomiejpluta.base.editor.asset.view.select.SelectGraphicAssetView
import com.bartlomiejpluta.base.editor.map.viewmodel.GameMapBuilderVM
import com.bartlomiejpluta.base.editor.project.context.ProjectContext
import com.bartlomiejpluta.base.editor.tileset.asset.TileSetAsset
import tornadofx.View

class MapTileSetSelectionView : View("Tile Set") {
   private val mapBuilderVM = find<GameMapBuilderVM>()

   private val projectContext: ProjectContext by di()

   private val selectGraphicAssetView = find<SelectGraphicAssetView<TileSetAsset>>(
      SelectGraphicAssetView<TileSetAsset>::assets to projectContext.project!!.tileSets,
      SelectGraphicAssetView<TileSetAsset>::asset to mapBuilderVM.tileSetAssetProperty
   )

   // FIXME
   // It's kind of ugly solution because for some reason
   // the custom validator on tileSetsListView does not work here.
   // Desired solution should use mapBuilderVM.valid(mapBuilderVM.tileSetProperty)
   // as in the previous step of the wizard as well as the feedback for user
   // saying, that tile set field is required.
   override val complete = mapBuilderVM.tileSetAssetProperty.isNotNull

   override val root = selectGraphicAssetView.root
}