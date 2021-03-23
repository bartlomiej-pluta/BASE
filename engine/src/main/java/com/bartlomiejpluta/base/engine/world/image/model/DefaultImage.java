package com.bartlomiejpluta.base.engine.world.image.model;

import com.bartlomiejpluta.base.api.image.Image;
import com.bartlomiejpluta.base.engine.core.gl.object.material.Material;
import com.bartlomiejpluta.base.engine.core.gl.object.mesh.Mesh;
import com.bartlomiejpluta.base.engine.world.object.Sprite;
import lombok.Getter;
import lombok.NonNull;
import org.joml.Vector2f;

@Getter
public class DefaultImage extends Sprite implements Image {
   private final Vector2f imageScale = new Vector2f(1, 1);
   private final int primaryWidth;
   private final int primaryHeight;
   private final int factor;
   private final int width;
   private final int height;

   public DefaultImage(@NonNull Mesh mesh, @NonNull Material texture, int primaryWidth, int primaryHeight, int factor) {
      super(mesh, texture);
      this.primaryWidth = primaryWidth;
      this.primaryHeight = primaryHeight;
      this.factor = factor;

      this.width = primaryWidth * factor;
      this.height = primaryHeight * factor;

      super.setScale(width * imageScale.x, height * imageScale.y);
   }

   @Override
   public void setOpacity(float opacity) {
      material.setAlpha(opacity);
   }

   @Override
   public float getOpacity() {
      return material.getColor().w();
   }

   @Override
   public void setScaleX(float scaleX) {
      this.imageScale.x = scaleX;
      super.setScaleX(width * scaleX);
   }

   @Override
   public void setScaleY(float scaleY) {
      this.imageScale.y = scaleY;
      super.setScaleY(height * scaleY);
   }

   @Override
   public void setScale(float scale) {
      this.imageScale.x = scale;
      this.imageScale.y = scale;
      super.setScale(width * scale, height * scale);
   }

   public void setScale(float scaleX, float scaleY) {
      this.imageScale.x = scaleX;
      this.imageScale.y = scaleY;
      super.setScale(width * scaleX, height * scaleY);
   }

   @Override
   public float getScaleX() {
      return imageScale.x;
   }

   @Override
   public float getScaleY() {
      return imageScale.y;
   }
}
