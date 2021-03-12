package com.bartlomiejpluta.base.engine.ui;

import com.bartlomiejpluta.base.api.game.screen.Screen;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ScreenManager {
   public Screen createScreen(String title, int width, int height) {
      log.info("Creating GLFW window ([{}], {}x{})", title, width, height);
      return GLFWScreen.create(title, width, height);
   }
}
