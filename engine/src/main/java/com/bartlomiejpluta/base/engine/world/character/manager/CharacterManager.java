package com.bartlomiejpluta.base.engine.world.character.manager;

import com.bartlomiejpluta.base.api.character.Character;
import com.bartlomiejpluta.base.engine.common.init.Initializable;
import com.bartlomiejpluta.base.internal.gc.Cleanable;

public interface CharacterManager extends Initializable, Cleanable {
   Character createCharacter(String characterSetUid);
}
