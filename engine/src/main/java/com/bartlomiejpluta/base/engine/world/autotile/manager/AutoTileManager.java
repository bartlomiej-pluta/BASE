package com.bartlomiejpluta.base.engine.world.autotile.manager;

import com.bartlomiejpluta.base.engine.common.init.Initializable;
import com.bartlomiejpluta.base.engine.common.manager.AssetManager;
import com.bartlomiejpluta.base.engine.world.autotile.asset.AutoTileSetAsset;
import com.bartlomiejpluta.base.engine.world.autotile.model.AutoTileSet;

public interface AutoTileManager extends Initializable, AssetManager<AutoTileSetAsset, AutoTileSet> {
}
