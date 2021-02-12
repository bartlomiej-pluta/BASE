package com.bartlomiejpluta.base.editor.map.view.wizard

import com.bartlomiejpluta.base.editor.asset.model.Asset
import com.bartlomiejpluta.base.editor.map.viewmodel.GameMapBuilderVM
import com.bartlomiejpluta.base.editor.project.context.ProjectContext
import com.bartlomiejpluta.base.editor.tileset.model.TileSet
import javafx.scene.control.ListView
import tornadofx.*

class MapTileSetSelectionView : View("Tile Set") {
   private val mapBuilderVM = find<GameMapBuilderVM>()

   private var tileSetsListView: ListView<TileSet> by singleAssign()

   private val projectContext: ProjectContext by di()

   // FIXME
   // Because of loading all the images at once and storing them in cache
   // this solution is not best efficient. It could be better to store images
   // in the local cache, scoped to this view.
   private val tileSets =
      projectContext.project?.tileSets?.map(Asset::uid)?.map(projectContext::loadTileSet)?.asObservable()
         ?: throw IllegalStateException("There is no open project in the context")


   // FIXME
   // It's kind of ugly solution because for some reason
   // the custom validator on tileSetsListView does not work here.
   // Desired solution should use mapBuilderVM.valid(mapBuilderVM.tileSetProperty)
   // as in the previous step of the wizard as well as the feedback for user
   // saying, that tile set field is required.
   override val complete = mapBuilderVM.tileSetProperty.isNotNull

   override val root = hbox {
      prefWidth = 640.0

      tileSetsListView = listview(tileSets) {
         cellFormat { text = it.name }
         bindSelected(mapBuilderVM.tileSetProperty)
      }

      scrollpane {
         prefWidthProperty().bind(mapBuilderVM.tileSetProperty.select { it.widthProperty })
         prefHeightProperty().bind(tileSetsListView.heightProperty())
         imageview(mapBuilderVM.tileSetProperty.select { it.imageProperty })
      }
   }
}