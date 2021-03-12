package com.bartlomiejpluta.base.engine.ui.manager;

import com.bartlomiejpluta.base.api.game.screen.Screen;
import com.bartlomiejpluta.base.engine.ui.model.GLFWScreen;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ScreenManager {
   private final InputManager inputManager;

   public Screen createScreen(String title, int width, int height) {
      log.info("Creating GLFW window ([{}], {}x{})", title, width, height);
      var screen = GLFWScreen.create(title, width, height);
      inputManager.registerScreen(screen);
      return screen;
   }
}
