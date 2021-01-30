package com.bartlomiejpluta.samplegame.core.ui;

import com.bartlomiejpluta.samplegame.core.error.AppException;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import static org.lwjgl.system.MemoryUtil.NULL;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Window {
   private final String title;
   private int width;
   private int height;
   private long windowHandle;
   private boolean resized;

   public void init() {
      // Setup an error callback. The default implementation
      // will print the error message in System.err.
      GLFWErrorCallback.createPrint(System.err).set();

      // Initialize GLFW. Most GLFW functions will not work before doing this.
      if (!glfwInit()) {
         throw new AppException("Unable to initialize GLFW");
      }

      glfwDefaultWindowHints(); // optional, the current window hints are already the default
      glfwWindowHint(GLFW_VISIBLE, GL_FALSE); // the window will stay hidden after creation
      glfwWindowHint(GLFW_RESIZABLE, GL_TRUE); // the window will be resizable
      glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
      glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 2);
      glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
      glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GL_TRUE);

      // Create the window
      windowHandle = glfwCreateWindow(width, height, title, NULL, NULL);
      if (windowHandle == NULL) {
         throw new AppException("Failed to create the GLFW window");
      }

      // Setup resize callback
      glfwSetFramebufferSizeCallback(windowHandle, (window, width, height) -> {
         Window.this.width = width;
         Window.this.height = height;
         Window.this.resized = true;
      });

      // Setup a key callback. It will be called every time a key is pressed, repeated or released.
      glfwSetKeyCallback(windowHandle, (window, key, scancode, action, mods) -> {
         if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE) {
            glfwSetWindowShouldClose(window, true); // We will detect this in the rendering loop
         }
      });

      // Get the resolution of the primary monitor
      GLFWVidMode videoMode = glfwGetVideoMode(glfwGetPrimaryMonitor());

      // Center our window
      glfwSetWindowPos(windowHandle, (videoMode.width() - width) / 2, (videoMode.height() - height) / 2);

      // Make the OpenGL context current
      glfwMakeContextCurrent(windowHandle);

      // Enable V-Sync
      glfwSwapInterval(1);

      // Make the window visible
      glfwShowWindow(windowHandle);

      GL.createCapabilities();

      // Support for transparencies
      glEnable(GL_BLEND);
      glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

      // Set the clear color
      glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
      glEnable(GL_DEPTH_TEST);
   }

   public void update() {
      glfwSwapBuffers(windowHandle);
      glfwPollEvents();
   }

   public boolean isKeyPressed(int keyCode) {
      return glfwGetKey(windowHandle, keyCode) == GLFW_PRESS;
   }

   public void clear(float r, float g, float b, float alpha) {
      glClearColor(r, g, b, alpha);
   }

   public boolean shouldClose() {
      return glfwWindowShouldClose(windowHandle);
   }

   public static Window create(String title, int width, int height) {
      return new Window(title, width, height, -1, false);
   }
}
