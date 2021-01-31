package com.bartlomiejpluta.base.game.logic;

import com.bartlomiejpluta.base.core.gl.object.material.Material;
import com.bartlomiejpluta.base.core.gl.object.texture.TextureManager;
import com.bartlomiejpluta.base.core.gl.render.Renderer;
import com.bartlomiejpluta.base.core.logic.GameLogic;
import com.bartlomiejpluta.base.core.ui.Window;
import com.bartlomiejpluta.base.core.util.mesh.MeshManager;
import com.bartlomiejpluta.base.core.world.animation.Animator;
import com.bartlomiejpluta.base.core.world.camera.Camera;
import com.bartlomiejpluta.base.core.world.map.GameMap;
import com.bartlomiejpluta.base.core.world.movement.Direction;
import com.bartlomiejpluta.base.core.world.scene.Scene;
import com.bartlomiejpluta.base.core.world.tileset.manager.TileSetManager;
import com.bartlomiejpluta.base.game.world.entity.Entity;
import com.bartlomiejpluta.base.game.world.entity.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static org.lwjgl.glfw.GLFW.*;

@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DefaultGameLogic implements GameLogic {
   private final Renderer renderer;
   private final TileSetManager tileSetManager;
   private final MeshManager meshManager;
   private final TextureManager textureManager;
   private final EntityManager entityManager;
   private final Animator animator;

   private Camera camera;
   private GameMap map;
   private Scene scene;

   private Entity player;

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
