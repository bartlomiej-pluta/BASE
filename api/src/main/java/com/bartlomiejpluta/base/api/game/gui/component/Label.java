package com.bartlomiejpluta.base.api.game.gui.component;

import com.bartlomiejpluta.base.api.game.gui.base.Color;
import com.bartlomiejpluta.base.api.game.gui.base.GUI;
import com.bartlomiejpluta.base.api.game.screen.Screen;

import static java.util.Objects.requireNonNull;

public class Label extends BaseComponent {
   private final GUI gui;

   private String text = "";
   private String font;
   private float fontSize;
   private int alignment = GUI.ALIGN_LEFT;
   private final Color color;

   private final float[] bounds = new float[4];

   public Label(GUI gui, String font) {
      this.gui = requireNonNull(gui);
      this.font = requireNonNull(font);

      this.color = this.gui.createColor();
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

   public void setFontSize(float fontSize) {
      this.fontSize = fontSize;
   }

   public int getAlignment() {
      return alignment;
   }

   public void setAlignment(int alignment) {
      this.alignment = alignment;
   }

   public float getRed() {
      return color.getRed();
   }

   public void setRed(float value) {
      color.setRed(value);
   }

   public float getGreen() {
      return color.getGreen();
   }

   public void setGreen(float value) {
      color.setGreen(value);
   }

   public float getBlue() {
      return color.getBlue();
   }

   public void setBlue(float value) {
      color.setBlue(value);
   }

   public float getAlpha() {
      return color.getAlpha();
   }

   public void setAlpha(float value) {
      color.setAlpha(value);
   }

   public void setColor(float red, float green, float blue, float alpha) {
      color.setRGBA(red, green, blue, alpha);
   }

   public void setColor(float red, float green, float blue) {
      color.setRGB(red, green, blue);
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
