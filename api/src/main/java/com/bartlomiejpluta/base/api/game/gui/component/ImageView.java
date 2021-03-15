package com.bartlomiejpluta.base.api.game.gui.component;

import com.bartlomiejpluta.base.api.game.context.Context;
import com.bartlomiejpluta.base.api.game.gui.base.GUI;
import com.bartlomiejpluta.base.api.game.gui.base.Image;
import com.bartlomiejpluta.base.api.game.gui.base.Paint;
import com.bartlomiejpluta.base.api.game.screen.Screen;

public class ImageView extends BaseComponent {
   private final Paint paint;

   private Image image;
   private float angle = 0;
   private float opacity = 1;
   private float scaleX = 1;
   private float scaleY = 1;

   public ImageView(Context context, GUI gui) {
      super(context, gui);
      this.paint = gui.createPaint();
   }

   public void setImage(String imageUid) {
      this.image = gui.getImage(imageUid);
   }

   public void setAngle(float angle) {
      this.angle = angle;
   }

   public void setOpacity(float opacity) {
      this.opacity = opacity;
   }

   public void setScaleX(float scaleX) {
      this.scaleX = scaleX;
   }

   public void setScaleY(float scaleY) {
      this.scaleY = scaleY;
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

   public float getAngle() {
      return angle;
   }

   public float getOpacity() {
      return opacity;
   }

   public float getScaleX() {
      return scaleX;
   }

   public float getScaleY() {
      return scaleY;
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