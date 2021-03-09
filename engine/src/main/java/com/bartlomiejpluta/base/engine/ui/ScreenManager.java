package com.bartlomiejpluta.base.engine.ui;

import com.bartlomiejpluta.base.api.game.screen.Screen;
import org.springframework.stereotype.Component;

@Component
public class ScreenManager {
   public Screen createScreen(String title, int width, int height) {
      return GLFWScreen.create(title, width, height);
   }
}
