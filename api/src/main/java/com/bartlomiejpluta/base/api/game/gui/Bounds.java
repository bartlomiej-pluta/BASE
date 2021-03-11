package com.bartlomiejpluta.base.api.game.gui;

public class Bounds {
   private float minX;
   private float minY;
   private float maxX;
   private float maxY;

   public void update(float minX, float minY, float maxX, float maxY) {
      this.minX = minX;
      this.minY = minY;
      this.maxX = maxX;
      this.maxY = maxY;
   }

   public float getMinX() {
      return minX;
   }

   public float getMinY() {
      return minY;
   }

   public float getMaxX() {
      return maxX;
   }

   public float getMaxY() {
      return maxY;
   }

   public float getWidth() {
      return maxX - minX;
   }

   public float getHeight() {
      return maxY - minY;
   }
}
