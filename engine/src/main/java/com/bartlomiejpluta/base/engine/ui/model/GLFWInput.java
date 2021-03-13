package com.bartlomiejpluta.base.engine.ui.model;

import com.bartlomiejpluta.base.api.game.context.Context;
import com.bartlomiejpluta.base.api.game.input.Input;
import com.bartlomiejpluta.base.api.game.input.Key;
import com.bartlomiejpluta.base.api.game.input.KeyEvent;
import com.bartlomiejpluta.base.api.game.screen.Screen;
import com.bartlomiejpluta.base.engine.ui.event.GLFWKeyEvent;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.util.Deque;
import java.util.LinkedList;
import java.util.function.Consumer;

import static com.bartlomiejpluta.base.engine.ui.event.GLFWKeyEvent.glfwCode;
import static org.lwjgl.glfw.GLFW.*;

@Slf4j
public class GLFWInput implements Input {
   private final long windowHandle;
   private final Deque<Consumer<KeyEvent>> keyEventHandlers = new LinkedList<>();

   public GLFWInput(@NonNull Screen screen) {
      this.windowHandle = screen.getID();
   }

   public void init(Context context) {
      log.info("Registering key callback");
      glfwSetKeyCallback(windowHandle, (window, key, scancode, action, mods) -> {
         var event = GLFWKeyEvent.of(key, action);

         context.handleKeyEvent(event);

         for (var handler : keyEventHandlers) {
            if (event.isConsumed()) {
               return;
            }

            handler.accept(event);
         }
      });
   }

   @Override
   public boolean isKeyPressed(Key key) {
      return glfwGetKey(windowHandle, glfwCode(key)) == GLFW_PRESS;
   }

   @Override
   public void addKeyEventHandler(Consumer<KeyEvent> handler) {
      keyEventHandlers.addLast(handler);
   }

   @Override
   public void removeKeyEventHandler(Consumer<KeyEvent> handler) {
      keyEventHandlers.remove(handler);
   }
}
