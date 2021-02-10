package com.bartlomiejpluta.base.core.image;

import com.bartlomiejpluta.base.core.gl.object.material.Material;
import com.bartlomiejpluta.base.core.gl.object.texture.TextureManager;
import com.bartlomiejpluta.base.core.util.math.MathUtil;
import com.bartlomiejpluta.base.core.util.mesh.MeshManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DefaultImageManager implements ImageManager {
   private final MeshManager meshManager;
   private final TextureManager textureManager;

   @Override
   public Image createImage(String imageFileName) {
      var texture = textureManager.loadTexture(imageFileName);
      var width = texture.getWidth();
      var height = texture.getHeight();
      var gcd = MathUtil.gcdEuclidean(width, height);
      var initialWidth = width / gcd;
      var initialHeight = height / gcd;
      var mesh = meshManager.createQuad(initialWidth, initialHeight, 0, 0);

      var image = new Image(mesh, Material.textured(texture), initialWidth, initialHeight);
      image.setScale(gcd);

      return image;
   }

   @Override
   public void cleanUp() {
      log.info("There is nothing to clean up here");
   }
}
