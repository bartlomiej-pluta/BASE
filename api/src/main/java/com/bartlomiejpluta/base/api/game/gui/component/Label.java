package com.bartlomiejpluta.base.api.game.gui.component;

import com.bartlomiejpluta.base.api.game.gui.base.GUI;
import com.bartlomiejpluta.base.api.game.screen.Screen;

import static java.util.Objects.requireNonNull;

public class Label extends BaseComponent {
   private String text;
   private String font;

   private float fontSize;

   private int alignment;

   private float red;
   private float green;
   private float blue;
   private float alpha;

   private final float[] bounds = new float[4];

   public Label(String font) {
      this(font, "", 10f, GUI.ALIGN_LEFT, 1.0f, 1.0f, 1.0f, 1.0f);
   }

   public Label(String font, String text) {
      this(font, text, 10f, GUI.ALIGN_LEFT, 1.0f, 1.0f, 1.0f, 1.0f);
   }

   public Label(String font, String text, float fontSize) {
      this(font, text, fontSize, GUI.ALIGN_LEFT, 1.0f, 1.0f, 1.0f, 1.0f);
   }

   public Label(String font, String text, float fontSize, int alignment) {
      this(font, text, fontSize, alignment, 1.0f, 1.0f, 1.0f, 1.0f);
   }

   public Label(String font, String text, float fontSize, int alignment, float red, float green, float blue) {
      this(font, text, fontSize, alignment, red, green, blue, 1.0f);
   }

   public Label(String font, String text, float fontSize, int alignment, float red, float green, float blue, float alpha) {
      this.text = requireNonNull(text);
      this.font = requireNonNull(font);
      this.fontSize = fontSize;
      this.alignment = alignment;
      this.red = red;
      this.green = green;
      this.blue = blue;
      this.alpha = alpha;
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
      return red;
   }

   public void setRed(float red) {
      this.red = red;
   }

   public float getGreen() {
      return green;
   }

   public void setGreen(float green) {
      this.green = green;
   }

   public float getBlue() {
      return blue;
   }

   public void setBlue(float blue) {
      this.blue = blue;
   }

   public float getAlpha() {
      return alpha;
   }

   public void setAlpha(float alpha) {
      this.alpha = alpha;
   }

   public void setColor(float red, float green, float blue, float alpha) {
      this.red = red;
      this.green = green;
      this.blue = blue;
      this.alpha = alpha;
   }

   public void setColor(float red, float green, float blue) {
      this.red = red;
      this.green = green;
      this.blue = blue;
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
      gui.setFillColor(red, green, blue, alpha);
      gui.fill();
      gui.putTextBox(x + paddingLeft, y + paddingTop, getWidth() - paddingLeft - paddingRight, text, bounds);
   }
}
