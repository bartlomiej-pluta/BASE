package com.bartlomiejpluta.base.engine.logic;

import com.bartlomiejpluta.base.api.game.camera.Camera;
import com.bartlomiejpluta.base.api.game.runner.GameRunner;
import com.bartlomiejpluta.base.api.game.window.Window;
import com.bartlomiejpluta.base.engine.core.gl.object.texture.TextureManager;
import com.bartlomiejpluta.base.engine.core.gl.render.Renderer;
import com.bartlomiejpluta.base.engine.project.loader.ClassLoader;
import com.bartlomiejpluta.base.engine.project.loader.ProjectLoader;
import com.bartlomiejpluta.base.engine.project.model.Project;
import com.bartlomiejpluta.base.engine.project.model.RenderableContext;
import com.bartlomiejpluta.base.engine.util.mesh.MeshManager;
import com.bartlomiejpluta.base.engine.util.profiling.fps.FPSMonitor;
import com.bartlomiejpluta.base.engine.util.profiling.time.TimeProfilerService;
import com.bartlomiejpluta.base.engine.world.camera.DefaultCamera;
import com.bartlomiejpluta.base.engine.world.entity.manager.EntityManager;
import com.bartlomiejpluta.base.engine.world.image.manager.ImageManager;
import com.bartlomiejpluta.base.engine.world.map.manager.MapManager;
import com.bartlomiejpluta.base.engine.world.tileset.manager.TileSetManager;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DefaultGameLogic implements GameLogic {
   private final Renderer renderer;
   private final TileSetManager tileSetManager;
   private final MeshManager meshManager;
   private final TextureManager textureManager;
   private final EntityManager entityManager;
   private final ImageManager imageManager;
   private final TimeProfilerService profiler;
   private final FPSMonitor fpsMonitor;
   private final MapManager mapManager;
   private final ProjectLoader projectLoader;
   private final ClassLoader classLoader;

   private final RenderableContext context;

   private final Camera camera = new DefaultCamera();

   private Project project;
   private GameRunner runner;

   @SneakyThrows
   @Override
   public void init(Window window) {
      log.info("Initializing game logic");
      renderer.init();
      context.init(camera);

      project = projectLoader.loadProject();
      var runnerClass = classLoader.<GameRunner>loadClass(project.getRunner());
      runner = runnerClass.getConstructor().newInstance();

      runner.init(context);
   }

   @Override
   public void input(Window window) {
      context.input(window);
   }

   @Override
   public void update(float dt) {
      context.update(dt);
      fpsMonitor.update(dt);
   }

   @Override
   public void render(Window window) {
      renderer.render(window, camera, context);
   }

   @Override
   public void cleanUp() {
      log.info("There is nothing to clean up here");
   }
}
