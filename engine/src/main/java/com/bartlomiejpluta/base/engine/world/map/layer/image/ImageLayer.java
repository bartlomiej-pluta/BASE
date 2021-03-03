package com.bartlomiejpluta.base.engine.world.map.layer.image;

import com.bartlomiejpluta.base.api.game.camera.Camera;
import com.bartlomiejpluta.base.api.game.map.Layer;
import com.bartlomiejpluta.base.api.game.window.Window;
import com.bartlomiejpluta.base.api.internal.render.ShaderManager;
import com.bartlomiejpluta.base.engine.world.image.model.Image;
import com.bartlomiejpluta.base.engine.world.map.model.DefaultGameMap;
import lombok.NonNull;

public class ImageLayer implements Layer {
   private final float mapWidth;
   private final float mapHeight;

   @NonNull
   private Image image;
   private float imageInitialWidth;
   private float imageInitialHeight;
   private float gcd;

   private float x;
   private float y;
   private float scaleX;
   private float scaleY;

   @NonNull
   private ImageLayerMode mode;

   private boolean parallax;

   public ImageLayer(DefaultGameMap map, Image image, float opacity, float x, float y, float scaleX, float scaleY, ImageLayerMode mode, boolean parallax) {
      this.mapWidth = map.getWidth();
      this.mapHeight = map.getHeight();

      this.image = image;
      this.imageInitialWidth = image.getInitialWidth();
      this.imageInitialHeight = image.getInitialHeight();
      this.gcd = image.getGcd();

      this.x = x;
      this.y = y;
      this.scaleX = scaleX;
      this.scaleY = scaleY;
      this.mode = mode;
      this.parallax = parallax;

      this.image.getMaterial().setAlpha(opacity);
      this.image.setPosition(x, y);

      switch (mode) {
         case NORMAL -> image.setScale(mapWidth / imageInitialWidth, mapHeight / imageInitialHeight);
         case FIT_MAP -> image.setScale(image.getGcd() * imageInitialWidth * scaleX, image.getGcd() * imageInitialHeight * scaleY);
      }
   }


   @Override
   public void render(Window window, Camera camera, ShaderManager shaderManager) {
      if (image != null) {
         if (parallax) {
            var cameraPosition = camera.getPosition();
            image.setPosition(cameraPosition.x + x, cameraPosition.y + y);
         }

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
