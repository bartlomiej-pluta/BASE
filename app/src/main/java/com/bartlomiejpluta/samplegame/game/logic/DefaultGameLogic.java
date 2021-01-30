package com.bartlomiejpluta.samplegame.game.logic;

import com.bartlomiejpluta.samplegame.core.gl.render.Renderer;
import com.bartlomiejpluta.samplegame.core.logic.GameLogic;
import com.bartlomiejpluta.samplegame.core.ui.Window;
import com.bartlomiejpluta.samplegame.core.world.object.Sprite;
import com.bartlomiejpluta.samplegame.core.world.scene.Scene;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.joml.Vector3f;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DefaultGameLogic implements GameLogic {
   private final Renderer renderer;
   private final Scene scene = new Scene();

   Sprite sprite;

   @Override
   public void init(Window window) {
      log.info("Initializing game logic");
      renderer.init();

      sprite = new Sprite();
      scene.add(sprite);
   }

   @Override
   public void input(Window window) {

   }

   @Override
   public void update(float dt) {

   }

   @Override
   public void render(Window window) {
      renderer.render(window, scene);
   }

   @Override
   public void cleanUp() {
      renderer.cleanUp();
   }
}
