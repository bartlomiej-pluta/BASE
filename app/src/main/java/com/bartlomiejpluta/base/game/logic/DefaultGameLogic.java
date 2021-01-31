package com.bartlomiejpluta.base.game.logic;

import com.bartlomiejpluta.base.core.gl.render.Renderer;
import com.bartlomiejpluta.base.core.logic.GameLogic;
import com.bartlomiejpluta.base.core.ui.Window;
import com.bartlomiejpluta.base.core.world.camera.Camera;
import com.bartlomiejpluta.base.core.world.scene.Scene;
import com.bartlomiejpluta.base.game.world.map.GameMap;
import com.bartlomiejpluta.base.game.world.tileset.manager.TileSetManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DefaultGameLogic implements GameLogic {
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
