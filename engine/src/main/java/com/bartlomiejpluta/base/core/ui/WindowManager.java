package com.bartlomiejpluta.base.core.ui;

import org.springframework.stereotype.Component;

@Component
public class WindowManager {
   public Window createWindow(String title, int width, int height) {
      return Window.create(title, width, height);
   }
}
