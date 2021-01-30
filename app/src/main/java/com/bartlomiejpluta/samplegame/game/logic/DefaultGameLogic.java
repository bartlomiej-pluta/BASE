package com.bartlomiejpluta.samplegame.game.logic;

import com.bartlomiejpluta.samplegame.core.gl.render.Renderer;
import com.bartlomiejpluta.samplegame.core.logic.GameLogic;
import com.bartlomiejpluta.samplegame.core.ui.Window;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DefaultGameLogic implements GameLogic {
   private final Renderer renderer;

   @Override
   public void init(Window window) {
      log.info("Initializing game logic");
      renderer.init();
   }

   @Override
   public void input(Window window) {

   }

   @Override
   public void update(float dt) {

   }

   @Override
   public void render(Window window) {
      renderer.render(window);
   }

   @Override
   public void cleanUp() {
      renderer.cleanUp();
   }
}
