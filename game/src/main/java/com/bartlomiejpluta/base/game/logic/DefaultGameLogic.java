package com.bartlomiejpluta.base.game.logic;

import com.bartlomiejpluta.base.api.runner.GameRunner;
import com.bartlomiejpluta.base.core.gl.object.texture.TextureManager;
import com.bartlomiejpluta.base.core.gl.render.Renderer;
import com.bartlomiejpluta.base.core.logic.GameLogic;
import com.bartlomiejpluta.base.core.profiling.fps.FPSMonitor;
import com.bartlomiejpluta.base.core.profiling.time.TimeProfilerService;
import com.bartlomiejpluta.base.core.ui.Window;
import com.bartlomiejpluta.base.core.util.mesh.MeshManager;
import com.bartlomiejpluta.base.core.world.camera.Camera;
import com.bartlomiejpluta.base.game.image.manager.ImageManager;
import com.bartlomiejpluta.base.game.map.manager.MapManager;
import com.bartlomiejpluta.base.game.project.loader.ClassLoader;
import com.bartlomiejpluta.base.game.project.loader.ProjectLoader;
import com.bartlomiejpluta.base.game.project.model.Project;
import com.bartlomiejpluta.base.game.project.model.RenderableContext;
import com.bartlomiejpluta.base.game.tileset.manager.TileSetManager;
import com.bartlomiejpluta.base.game.world.entity.manager.EntityManager;
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

   private final Camera camera = new Camera();

   private Project project;
   private GameRunner runner;

   @SneakyThrows
   @Override
   public void init(Window window) {
      log.info("Initializing game logic");
      renderer.init();

      project = projectLoader.loadProject();
      var runnerClass = classLoader.<GameRunner>loadClass(project.getRunner());
      runner = runnerClass.getConstructor().newInstance();

      runner.init(context);
   }

   @Override
   public void input(Window window) {

   }

   @Override
   public void update(float dt) {
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
