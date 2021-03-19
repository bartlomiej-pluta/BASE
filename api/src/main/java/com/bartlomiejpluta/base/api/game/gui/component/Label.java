package com.bartlomiejpluta.base.api.game.gui.component;

import com.bartlomiejpluta.base.api.game.context.Context;
import com.bartlomiejpluta.base.api.game.gui.base.Color;
import com.bartlomiejpluta.base.api.game.gui.base.GUI;
import com.bartlomiejpluta.base.api.game.screen.Screen;

import static java.util.Objects.requireNonNull;

public class Label extends BaseComponent {
   private String text = "";
   private String font;
   private float fontSize;
   private int alignment = GUI.ALIGN_LEFT;
   private Color color;

   private final float[] bounds = new float[4];

   public Label(Context context, GUI gui) {
      super(context, gui);
      this.color = gui.createColor();
      this.color.setRGBA(0xFFFFFFFF);
   }

   public String getText() {
      return text;
   }

   public void setText(String text) {
      this.text = requireNonNull(text);
   }

   public String getFont() {
      return font;
   }

   public void setFont(String font) {
      this.font = requireNonNull(font);
   }

   public float getFontSize() {
      return fontSize;
   }

   public void setFontSize(Float fontSize) {
      this.fontSize = fontSize;
   }

   public int getAlignment() {
      return alignment;
   }

   public void setAlignment(Integer alignment) {
      this.alignment = alignment;
   }

   public float getRed() {
      return color.getRed();
   }

   public void setRed(Float value) {
      color.setRed(value);
   }

   public void setRed(Integer value) {
      color.setRed(value);
   }

   public float getGreen() {
      return color.getGreen();
   }

   public void setGreen(Float value) {
      color.setGreen(value);
   }

   public void setGreen(Integer value) {
      color.setGreen(value);
   }

   public float getBlue() {
      return color.getBlue();
   }

   public void setBlue(Float value) {
      color.setBlue(value);
   }

   public void setBlue(Integer value) {
      color.setBlue(value);
   }

   public float getAlpha() {
      return color.getAlpha();
   }

   public void setAlpha(Float value) {
      color.setAlpha(value);
   }

   public void setAlpha(Integer value) {
      color.setAlpha(value);
   }

   public void setColor(Float red, Float green, Float blue, Float alpha) {
      color.setRGBA(red, green, blue, alpha);
   }

   public void setColor(Float red, Float green, Float blue) {
      color.setRGB(red, green, blue);
   }

   public void setColor(Integer hex) {
      color.setRGB(hex);
   }

   @Override
   protected float getContentWidth() {
      return bounds[2] - bounds[0];
   }

   @Override
   protected float getContentHeight() {
      return bounds[3] - bounds[1];
   }

   @Override
   public void draw(Screen screen, GUI gui) {
      gui.beginPath();
      gui.setFontFace(font);
      gui.setTextAlignment(alignment);
      gui.setFontSize(fontSize);
      gui.setFillColor(color);
      gui.fill();
      gui.putTextBox(x + paddingLeft, y + paddingTop, getWidth() - paddingLeft - paddingRight, text, bounds);
   }
}
