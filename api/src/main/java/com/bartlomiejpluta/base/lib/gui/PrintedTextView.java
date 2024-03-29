package com.bartlomiejpluta.base.lib.gui;

import com.bartlomiejpluta.base.api.context.Context;
import com.bartlomiejpluta.base.api.gui.Component;
import com.bartlomiejpluta.base.api.gui.GUI;
import lombok.Setter;

import java.util.Map;

public class PrintedTextView extends TextView {

   @Setter
   private Float duration;

   private float acc;

   private String originalText;

   public PrintedTextView(Context context, GUI gui, Map<String, Component> refs) {
      super(context, gui, refs);
   }

   public void start() {
      acc = 0f;
   }

   public boolean isPrinting() {
      return originalText.length() != super.getText().length();
   }

   public void printAll() {
      acc = originalText.length() * duration;
   }

   @Override
   public void setText(String text) {
      super.setText("");
      this.originalText = text;
   }

   @Override
   public void update(float dt) {
      super.update(dt);

      acc += dt;
      super.setText(originalText.substring(0, Math.min(originalText.length(), (int) (acc / duration))));
   }
}