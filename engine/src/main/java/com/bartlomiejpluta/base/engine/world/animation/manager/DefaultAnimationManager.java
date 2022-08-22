package com.bartlomiejpluta.base.engine.world.animation.manager;

import com.bartlomiejpluta.base.api.animation.Animation;
import com.bartlomiejpluta.base.engine.core.gl.object.material.Material;
import com.bartlomiejpluta.base.engine.core.gl.object.mesh.Mesh;
import com.bartlomiejpluta.base.engine.core.gl.object.texture.TextureManager;
import com.bartlomiejpluta.base.engine.error.AppException;
import com.bartlomiejpluta.base.engine.project.config.ProjectConfiguration;
import com.bartlomiejpluta.base.engine.util.mesh.MeshManager;
import com.bartlomiejpluta.base.engine.world.animation.asset.AnimationAsset;
import com.bartlomiejpluta.base.engine.world.animation.model.DefaultAnimation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.joml.Vector2f;
import org.joml.Vector2fc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DefaultAnimationManager implements AnimationManager {
   private final MeshManager meshManager;
   private final TextureManager textureManager;
   private final Map<String, AnimationAsset> assets = new HashMap<>();
   private final Map<String, Vector2fc[]> frames = new HashMap<>();
   private final ProjectConfiguration configuration;
   private Mesh mesh;

   @Override
   public void init() {
      mesh = meshManager.createQuad(1, 1, 0.5f, 0.5f);
   }

   @Override
   public void registerAsset(AnimationAsset asset) {
      log.info("Registering [{}] animation asset under UID: [{}]", asset.getSource(), asset.getUid());
      assets.put(asset.getUid(), asset);
   }

   @Override
   public Animation loadObject(String uid) {
      var asset = assets.get(uid);

      if (asset == null) {
         throw new AppException("The animation asset with UID: [%s] does not exist", uid);
      }

      var animationFrames = frames.computeIfAbsent(asset.framesSignature(),
            signature -> createFrames(asset.getRows(), asset.getColumns())
      );

      var source = configuration.projectFile("animations", asset.getSource());
      var texture = textureManager.loadTexture(source, asset.getRows(), asset.getColumns());
      var material = Material.textured(texture);

      return new DefaultAnimation(mesh, material, animationFrames);
   }

   private Vector2fc[] createFrames(int rows, int columns) {
      log.info("Caching [{}x{}] animation ([{}] frames)", columns, rows, columns * rows);

      var frames = new Vector2fc[rows * columns];

      for (int row = 0; row < rows; ++row) {
         for (int column = 0; column < columns; ++column) {
            frames[row * columns + column] = new Vector2f(column, row);
         }
      }

      return frames;
   }
}
