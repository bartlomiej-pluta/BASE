package com.bartlomiejpluta.samplegame.game.logic;

import com.bartlomiejpluta.samplegame.core.gl.object.material.Material;
import com.bartlomiejpluta.samplegame.core.gl.object.texture.TextureManager;
import com.bartlomiejpluta.samplegame.core.gl.render.Renderer;
import com.bartlomiejpluta.samplegame.core.logic.GameLogic;
import com.bartlomiejpluta.samplegame.core.ui.Window;
import com.bartlomiejpluta.samplegame.core.world.camera.Camera;
import com.bartlomiejpluta.samplegame.core.world.scene.Scene;
import com.bartlomiejpluta.samplegame.game.object.Sprite;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DefaultGameLogic implements GameLogic {
   private final Renderer renderer;
   private final TextureManager textureManager;

   private final Camera camera = new Camera();
   private final Scene scene = new Scene(camera);

   @Override
   public void init(Window window) {
      log.info("Initializing game logic");
      renderer.init();

      var sprite = new Sprite(Material.textured(textureManager.loadTexture("/textures/grass.png")));
      var sprite2 = new Sprite(Material.colored(0.7f, 1.0f, 0.4f, 1.0f));
      sprite.setPosition(320, 240);
      sprite2.setPosition(110, 110);
      scene.add(sprite2);
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
