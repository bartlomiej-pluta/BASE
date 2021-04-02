package com.bartlomiejpluta.base.engine.ui.model;

import com.bartlomiejpluta.base.api.input.Input;
import com.bartlomiejpluta.base.api.input.Key;
import com.bartlomiejpluta.base.api.input.KeyEventHandler;
import com.bartlomiejpluta.base.api.screen.Screen;
import com.bartlomiejpluta.base.engine.ui.event.GLFWKeyEvent;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.util.Deque;
import java.util.LinkedList;

import static com.bartlomiejpluta.base.engine.ui.event.GLFWKeyEvent.glfwCode;
import static org.lwjgl.glfw.GLFW.*;

@Slf4j
public class GLFWInput implements Input {
   private final long windowHandle;
   private final Deque<KeyEventHandler> keyEventHandlers = new LinkedList<>();

   public GLFWInput(@NonNull Screen screen) {
      this.windowHandle = screen.getID();
   }

   public GLFWInput init() {
      log.info("Registering key callback");
      glfwSetKeyCallback(windowHandle, (window, key, scancode, action, mods) -> {
         var event = GLFWKeyEvent.of(key, action);

         if (event == null) {
            return;
         }

         // Use iterator to support removal from loop inside
         // We need also to iterate in FILO order (a stack-based model)
         for (var iterator = keyEventHandlers.descendingIterator(); iterator.hasNext(); ) {
            if (event.isConsumed()) {
               return;
            }

            iterator.next().handleKeyEvent(event);
         }
      });

      return this;
   }

   @Override
   public boolean isKeyPressed(Key key) {
      return glfwGetKey(windowHandle, glfwCode(key)) == GLFW_PRESS;
   }

   @Override
   public void addKeyEventHandler(@NonNull KeyEventHandler handler) {
      keyEventHandlers.addLast(handler);
      handler.onKeyEventHandlerRegister();
   }

   @Override
   public void removeKeyEventHandler(@NonNull KeyEventHandler handler) {
      keyEventHandlers.remove(handler);
      handler.onKeyEventHandlerUnregister();
   }
}
