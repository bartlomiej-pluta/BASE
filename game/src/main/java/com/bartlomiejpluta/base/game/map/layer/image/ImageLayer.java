package com.bartlomiejpluta.base.game.map.layer.image;

import com.bartlomiejpluta.base.core.gl.shader.manager.ShaderManager;
import com.bartlomiejpluta.base.core.ui.Window;
import com.bartlomiejpluta.base.core.world.camera.Camera;
import com.bartlomiejpluta.base.game.image.model.Image;
import com.bartlomiejpluta.base.game.map.layer.base.Layer;
import com.bartlomiejpluta.base.game.map.model.GameMap;
import lombok.NonNull;

public class ImageLayer implements Layer {
   private final float mapWidth;
   private final float mapHeight;

   private Image image;
   private float imageInitialWidth;
   private float imageInitialHeight;

   @NonNull
   private final ImageLayerMode mode;

   public ImageLayer(GameMap map, Image image, ImageLayerMode mode, float opacity, float x, float y) {
      this.mapWidth = map.getWidth();
      this.mapHeight = map.getHeight();
      this.mode = mode;

      setImage(image);
      setOpacity(opacity);
      setPosition(x, y);
   }

   public void setImage(Image image) {
      this.image = image;
      this.imageInitialWidth = image.getInitialWidth();
      this.imageInitialHeight = image.getInitialHeight();

      switch (mode) {
         case NORMAL -> image.setScale(image.getGcd() * imageInitialWidth, image.getGcd() * imageInitialHeight);
         case FIT_MAP -> image.setScale(mapWidth / imageInitialWidth, mapHeight / imageInitialHeight);
      }
   }

   public void setOpacity(float opacity) {
      this.image.getMaterial().setAlpha(opacity);
   }

   public void setPosition(float x, float y) {
      image.setPosition(x, y);
   }

   @Override
   public void render(Window window, Camera camera, ShaderManager shaderManager) {
      if (image != null) {
         if (mode == ImageLayerMode.FIT_SCREEN) {
            image.setScale(window.getWidth() / imageInitialWidth, window.getHeight() / imageInitialHeight);
         }

         image.render(window, camera, shaderManager);
      }
   }

   @Override
   public void update(float dt) {

   }
}
