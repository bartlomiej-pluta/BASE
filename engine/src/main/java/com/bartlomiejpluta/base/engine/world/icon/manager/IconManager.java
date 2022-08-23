package com.bartlomiejpluta.base.engine.world.icon.manager;

import com.bartlomiejpluta.base.api.icon.Icon;
import com.bartlomiejpluta.base.engine.common.init.Initializable;
import com.bartlomiejpluta.base.internal.gc.Cleanable;

public interface IconManager extends Initializable, Cleanable {
   Icon createIcon(String iconSetUid, int row, int column);
}
