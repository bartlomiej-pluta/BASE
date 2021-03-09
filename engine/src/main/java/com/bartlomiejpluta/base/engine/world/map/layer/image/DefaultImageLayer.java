package com.bartlomiejpluta.base.engine.world.map.layer.image;

import com.bartlomiejpluta.base.api.game.camera.Camera;
import com.bartlomiejpluta.base.api.game.image.Image;
import com.bartlomiejpluta.base.api.game.map.layer.image.ImageLayer;
import com.bartlomiejpluta.base.api.game.map.layer.image.ImageLayerMode;
import com.bartlomiejpluta.base.api.game.map.model.GameMap;
import com.bartlomiejpluta.base.api.game.screen.Screen;
import com.bartlomiejpluta.base.api.internal.render.ShaderManager;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

public class DefaultImageLayer implements ImageLayer {
   private final float mapWidth;
   private final float mapHeight;

   @Getter
   private final GameMap map;

   @NonNull
   @Getter
   private Image image;
   private float imagePrimaryWidth;
   private float imagePrimaryHeight;

   private float x;
   private float y;
   private float scaleX;
   private float scaleY;
   private float factor;

   @NonNull
   private ImageLayerMode mode;

   @Getter
   private float opacity;

   @Setter
   private boolean parallax;

   public DefaultImageLayer(GameMap map, Image image, float opacity, float x, float y, float scaleX, float scaleY, ImageLayerMode mode, boolean parallax) {
      this.map = map;
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
         case NORMAL -> image.setScale(factor * scaleX, factor * scaleY);
         case FIT_MAP -> image.setScale(mapWidth / imagePrimaryWidth, mapHeight / imagePrimaryHeight);
      }
   }

   @Override
   public void setImage(Image image) {
      this.image = image;
      this.imagePrimaryWidth = image.getPrimaryWidth();
      this.imagePrimaryHeight = image.getPrimaryHeight();
      this.factor = image.getFactor();

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
   public void update(float dt) {
      // Do nothing
   }

   @Override
   public void render(Screen screen, Camera camera, ShaderManager shaderManager) {
      if (image != null) {
         if (parallax) {
            var cameraPosition = camera.getPosition();
            image.setPosition(cameraPosition.x() + x, cameraPosition.y() + y);
         }

         if (mode == ImageLayerMode.FIT_SCREEN) {
            image.setScale(screen.getWidth() / imagePrimaryWidth, screen.getHeight() / imagePrimaryHeight);
         }

         image.render(screen, camera, shaderManager);
      }
   }
}
