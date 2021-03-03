package com.bartlomiejpluta.base.engine.world.image.manager;

import com.bartlomiejpluta.base.api.game.map.Image;
import com.bartlomiejpluta.base.engine.core.gl.object.material.Material;
import com.bartlomiejpluta.base.engine.core.gl.object.texture.TextureManager;
import com.bartlomiejpluta.base.engine.error.AppException;
import com.bartlomiejpluta.base.engine.project.config.ProjectConfiguration;
import com.bartlomiejpluta.base.engine.util.math.MathUtil;
import com.bartlomiejpluta.base.engine.util.mesh.MeshManager;
import com.bartlomiejpluta.base.engine.world.image.asset.ImageAsset;
import com.bartlomiejpluta.base.engine.world.image.model.DefaultImage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DefaultImageManager implements ImageManager {
   private final MeshManager meshManager;
   private final TextureManager textureManager;
   private final Map<String, ImageAsset> assets = new HashMap<>();
   private final ProjectConfiguration configuration;


   @Override
   public void registerAsset(ImageAsset asset) {
      log.info("Registering [{}] image asset under UID: [{}]", asset.getSource(), asset.getUid());
      assets.put(asset.getUid(), asset);
   }

   @Override
   public Image loadObject(String uid) {
      var asset = assets.get(uid);

      if (asset == null) {
         throw new AppException("The image asset with UID: [%s] does not exist", uid);
      }

      var source = configuration.projectFile("images", asset.getSource());
      var texture = textureManager.loadTexture(source);
      var width = texture.getWidth();
      var height = texture.getHeight();
      var gcd = MathUtil.gcdEuclidean(width, height);
      var initialWidth = width / gcd;
      var initialHeight = height / gcd;
      var mesh = meshManager.createQuad(initialWidth, initialHeight, 0, 0);
      var material = Material.textured(texture);
      log.info("Creating new image on asset with UID: [{}]", uid);

      return new DefaultImage(mesh, material, initialWidth, initialHeight, gcd);
   }

   @Override
   public void cleanUp() {
      log.info("There is nothing to clean up here");
   }
}
