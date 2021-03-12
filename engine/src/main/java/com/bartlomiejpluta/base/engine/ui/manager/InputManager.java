package com.bartlomiejpluta.base.engine.ui.manager;

import com.bartlomiejpluta.base.api.game.input.Input;
import com.bartlomiejpluta.base.engine.error.AppException;
import com.bartlomiejpluta.base.engine.ui.model.GLFWInput;
import com.bartlomiejpluta.base.engine.ui.model.GLFWScreen;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class InputManager {
   private GLFWScreen screen;
   private Input input;

   void registerScreen(@NonNull GLFWScreen screen) {
      this.screen = screen;
   }

   public Input getInput() {
      if (input != null) {
         return input;
      }

      log.info("Creating input model singleton instance");

      if (screen == null) {
         throw new AppException("GLFWScreen is not registered yet");
      }

      input = new GLFWInput(screen);

      return input;
   }
}
