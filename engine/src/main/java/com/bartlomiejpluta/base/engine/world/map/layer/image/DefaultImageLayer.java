package com.bartlomiejpluta.base.engine.world.map.layer.image;

import com.bartlomiejpluta.base.api.camera.Camera;
import com.bartlomiejpluta.base.api.image.Image;
import com.bartlomiejpluta.base.api.map.layer.image.ImageLayer;
import com.bartlomiejpluta.base.api.map.layer.image.ImageLayerMode;
import com.bartlomiejpluta.base.api.map.model.GameMap;
import com.bartlomiejpluta.base.api.screen.Screen;
import com.bartlomiejpluta.base.engine.world.map.layer.base.BaseLayer;
import com.bartlomiejpluta.base.internal.render.ShaderManager;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

public class DefaultImageLayer extends BaseLayer implements ImageLayer {
   private final float mapWidth;
   private final float mapHeight;

   @NonNull
   @Getter
   private Image image;
   private float imageWidth;
   private float imageHeight;

   private float x;
   private float y;
   private float scaleX;
   private float scaleY;

   @NonNull
   private ImageLayerMode mode;

   @Getter
   private float opacity;

   @Setter
   private boolean parallax;

   public DefaultImageLayer(GameMap map, Image image, float opacity, float x, float y, float scaleX, float scaleY, ImageLayerMode mode, boolean parallax) {
      super(map);
      this.mapWidth = map.getWidth();
      this.mapHeight = map.getHeight();

      this.x = x;
      this.y = y;

      this.scaleX = scaleX;
      this.scaleY = scaleY;

      this.mode = mode;
      this.parallax = parallax;
      this.opacity = opacity;

      setImage(image);
   }

   private void recalculate() {
      switch (mode) {
         case NORMAL -> image.setScale(scaleX, scaleY);
         case FIT_MAP -> image.setScale(mapWidth / imageWidth, mapHeight / imageHeight);
      }
   }

   @Override
   public void setImage(Image image) {
      this.image = image;
      this.imageWidth = image.getWidth();
      this.imageHeight = image.getHeight();

      this.image.setPosition(x, y);

      this.image.setOpacity(opacity);

      recalculate();
   }

   @Override
   public void setOpacity(float opacity) {
      this.opacity = opacity;

      this.image.setOpacity(opacity);
   }

   @Override
   public void setPosition(float x, float y) {
      this.x = x;
      this.y = y;

      this.image.setPosition(x, y);
   }

   @Override
   public void setMode(ImageLayerMode mode) {
      this.mode = mode;

      recalculate();
   }

   @Override
   public void setScale(float scaleX, float scaleY) {
      this.x = scaleX;
      this.y = scaleY;

      recalculate();
   }

   @Override
   public void render(Screen screen, Camera camera, ShaderManager shaderManager) {
      if (image != null) {
         if (parallax) {
            var cameraPosition = camera.getPosition();
            image.setPosition(cameraPosition.x() + x, cameraPosition.y() + y);
         }

         if (mode == ImageLayerMode.FIT_SCREEN) {
            image.setScale(screen.getWidth() / imageWidth, screen.getHeight() / imageHeight);
         }

         image.render(screen, camera, shaderManager);
      }

      super.render(screen, camera, shaderManager);
   }
}
