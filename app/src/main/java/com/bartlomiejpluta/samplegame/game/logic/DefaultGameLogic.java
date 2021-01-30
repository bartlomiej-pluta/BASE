package com.bartlomiejpluta.samplegame.game.logic;

import com.bartlomiejpluta.samplegame.core.gl.render.Renderer;
import com.bartlomiejpluta.samplegame.core.logic.GameLogic;
import com.bartlomiejpluta.samplegame.core.ui.Window;
import com.bartlomiejpluta.samplegame.core.world.camera.Camera;
import com.bartlomiejpluta.samplegame.core.world.scene.Scene;
import com.bartlomiejpluta.samplegame.game.world.map.GameMap;
import com.bartlomiejpluta.samplegame.game.world.tileset.manager.TileSetManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DefaultGameLogic implements GameLogic {
   private static final float SCALE = 3.0f;
   private final Renderer renderer;
   private final TileSetManager tileSetManager;

   private Camera camera;
   private GameMap map;
   private Scene scene;

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
      renderer.render(window, scene);
   }

   @Override
   public void cleanUp() {
      renderer.cleanUp();
      scene.cleanUp();
   }
}
