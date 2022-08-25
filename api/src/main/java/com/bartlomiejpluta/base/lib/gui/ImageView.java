package com.bartlomiejpluta.base.lib.gui;

import com.bartlomiejpluta.base.api.context.Context;
import com.bartlomiejpluta.base.api.gui.Component;
import com.bartlomiejpluta.base.api.gui.GUI;
import com.bartlomiejpluta.base.api.gui.Image;
import com.bartlomiejpluta.base.api.gui.Paint;
import com.bartlomiejpluta.base.api.screen.Screen;

import java.util.Map;

public class ImageView extends BaseComponent {
   private final Paint paint;

   private Image image;
   private float angle = 0;
   private float opacity = 1;
   private float scaleX = 1;
   private float scaleY = 1;

   public ImageView(Context context, GUI gui, Map<String, Component> refs) {
      super(context, gui, refs);
      this.paint = gui.createPaint();
   }

   public void setScale(float scaleX, float scaleY) {
      this.scaleX = scaleX;
      this.scaleY = scaleY;
   }

   public void setScale(float scale) {
      this.scaleX = scale;
      this.scaleY = scale;
   }

   public Image getImage() {
      return image;
   }

   public void setImage(String imageUid) {
      this.image = gui.getImage(imageUid);
   }

   public float getAngle() {
      return angle;
   }

   public void setAngle(float angle) {
      this.angle = angle;
   }

   public float getOpacity() {
      return opacity;
   }

   public void setOpacity(float opacity) {
      this.opacity = opacity;
   }

   public float getScaleX() {
      return scaleX;
   }

   public void setScaleX(float scaleX) {
      this.scaleX = scaleX;
   }

   public float getScaleY() {
      return scaleY;
   }

   public void setScaleY(float scaleY) {
      this.scaleY = scaleY;
   }

   @Override
   protected float getContentWidth() {
      return image.getWidth() * scaleX;
   }

   @Override
   protected float getContentHeight() {
      return image.getHeight() * scaleY;
   }

   @Override
   public void draw(Screen screen, GUI gui) {
      var posX = x + paddingLeft;
      var posY = y + paddingTop;
      var width = getWidth() - paddingLeft - paddingRight;
      var height = getHeight() - paddingTop - paddingBottom;

      gui.beginPath();
      gui.drawRectangle(posX, posY, width, height);
      gui.imagePattern(posX, posY, width, height, angle, opacity, image, paint);
      gui.setFillPaint(paint);
      gui.fill();
   }
}