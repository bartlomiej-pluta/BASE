package com.bartlomiejpluta.base.engine.ui;

import com.bartlomiejpluta.base.api.internal.window.Window;
import org.springframework.stereotype.Component;

@Component
public class WindowManager {
   public Window createWindow(String title, int width, int height) {
      return GLFWWindow.create(title, width, height);
   }
}
