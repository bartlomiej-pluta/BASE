package com.bartlomiejpluta.base.engine.ui;

import com.bartlomiejpluta.base.api.internal.window.Window;
import com.bartlomiejpluta.base.engine.error.AppException;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.joml.Vector2f;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class GLFWWindow implements Window {
   private final String title;
   private long windowHandle = -1;

   @Getter
   private int width;

   @Getter
   private int height;

   @Getter
   @Setter
   private boolean resized = false;

   private boolean initialized = false;

   public GLFWWindow(@NonNull String title, int width, int height) {
      this.title = title;
      this.width = width;
      this.height = height;
   }

   @Override
   public void init() {
      if(initialized) {
         throw new IllegalStateException("The window is already initialized");
      }

      initialized = true;

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
         GLFWWindow.this.width = width;
         GLFWWindow.this.height = height;
         GLFWWindow.this.resized = true;
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
//      glfwSwapInterval(1);

      // Make the window visible
      glfwShowWindow(windowHandle);

      GL.createCapabilities();

      // Support for transparencies
      glEnable(GL_BLEND);
      glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

      // Set the clear color
      glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
   }

   @Override
   public void update() {
      glfwSwapBuffers(windowHandle);
      glfwPollEvents();
   }

   @Override
   public boolean isKeyPressed(int keyCode) {
      return glfwGetKey(windowHandle, keyCode) == GLFW_PRESS;
   }

   @Override
   public void clear(float r, float g, float b, float alpha) {
      glClearColor(r, g, b, alpha);
   }

   @Override
   public boolean shouldClose() {
      return glfwWindowShouldClose(windowHandle);
   }

   @Override
   public Vector2f getSize() {
      return new Vector2f(width, height);
   }

   public static Window create(String title, int width, int height) {
      return new GLFWWindow(title, width, height);
   }
}
