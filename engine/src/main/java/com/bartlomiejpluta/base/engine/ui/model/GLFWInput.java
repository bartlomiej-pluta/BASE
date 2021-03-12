package com.bartlomiejpluta.base.engine.ui.model;

import com.bartlomiejpluta.base.api.game.input.Input;
import com.bartlomiejpluta.base.api.game.input.Key;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class GLFWInput implements Input {
   private final GLFWScreen screen;

   @Override
   public boolean isKeyPressed(Key key) {
      return screen.isKeyPressed(key);
   }
}
