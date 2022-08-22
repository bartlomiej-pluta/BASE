package com.bartlomiejpluta.base.api.ai;

import com.bartlomiejpluta.base.api.character.Character;

public interface NPC extends Character {
   AI getStrategy();
}
