package com.bartlomiejpluta.base.engine.world.animation.manager;

import com.bartlomiejpluta.base.api.animation.Animation;
import com.bartlomiejpluta.base.engine.common.init.Initializable;
import com.bartlomiejpluta.base.engine.common.manager.AssetManager;
import com.bartlomiejpluta.base.engine.world.animation.asset.AnimationAsset;

public interface AnimationManager extends Initializable, AssetManager<AnimationAsset, Animation> {
}
