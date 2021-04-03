package com.bartlomiejpluta.base.engine.ui.model;

import com.bartlomiejpluta.base.api.screen.Screen;
import com.bartlomiejpluta.base.engine.error.AppException;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.joml.Vector2f;
import org.joml.Vector2fc;
import org.joml.Vector2i;
import org.joml.Vector2ic;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class GLFWScreen implements Screen {
   private static final int REFRESH_RATE = 60;
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

   private final Vector2f size;

   public GLFWScreen(@NonNull String title, int width, int height) {
      this.title = title;
      this.width = width;
      this.height = height;
      this.size = new Vector2f(width, height);
   }

   @Override
   public void init() {
      if (initialized) {
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
      glfwWindowHint(GLFW_RESIZABLE, GL_FALSE);
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
         GLFWScreen.this.width = width;
         GLFWScreen.this.height = height;
         GLFWScreen.this.resized = true;
         GLFWScreen.this.size.x = width;
         GLFWScreen.this.size.y = height;
      });

      // Get the resolution of the primary monitor
      GLFWVidMode videoMode = glfwGetVideoMode(glfwGetPrimaryMonitor());

      // Center our window
      glfwSetWindowPos(windowHandle, (videoMode.width() - width) / 2, (videoMode.height() - height) / 2);

      // Make the OpenGL context current
      glfwMakeContextCurrent(windowHandle);

      // Enable antialiasing
      glfwWindowHint(GLFW_SAMPLES, 4);

      // Enable V-Sync
      // glfwSwapInterval(1);

      GL.createCapabilities();

      // Support for transparencies
      glEnable(GL_BLEND);
      glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

      // Set the clear color
      clear(0.0f, 0.0f, 0.0f, 0.0f);
   }

   @Override
   public Vector2ic getCurrentResolution() {
      var monitor = glfwGetPrimaryMonitor();
      var video = glfwGetVideoMode(monitor);

      if (video == null) {
         throw new AppException("Failed to obtain GLFW video mode");
      }

      return new Vector2i(video.width(), video.height());
   }

   @Override
   public void setFullscreenMode() {
      var monitor = glfwGetPrimaryMonitor();
      var video = glfwGetVideoMode(monitor);

      if (video == null) {
         throw new AppException("Failed to obtain GLFW video mode");
      }

      glfwSetWindowMonitor(windowHandle, monitor, 0, 0, video.width(), video.height(), video.refreshRate());
   }

   @Override
   public void setFullscreenMode(int width, int height) {
      var monitor = glfwGetPrimaryMonitor();
      var video = glfwGetVideoMode(monitor);

      if (video == null) {
         throw new AppException("Failed to obtain GLFW video mode");
      }

      glfwSetWindowMonitor(windowHandle, monitor, 0, 0, width, height, video.refreshRate());
   }

   @Override
   public void setWindowMode(int xPos, int yPos, int width, int height) {
      glfwSetWindowMonitor(windowHandle, NULL, xPos, yPos, width, height, REFRESH_RATE);
   }

   @Override
   public void setPosition(int x, int y) {
      glfwSetWindowPos(windowHandle, x, y);
   }

   @Override
   public void setSize(int width, int height) {
      glfwSetWindowSize(windowHandle, width, height);
   }

   @Override
   public void setResizable(boolean resizable) {
      glfwSetWindowAttrib(windowHandle, GLFW_RESIZABLE, resizable ? GLFW_TRUE : GLFW_FALSE);
   }

   @Override
   public void show() {
      glfwShowWindow(windowHandle);
   }

   @Override
   public void hide() {
      glfwHideWindow(windowHandle);
   }

   @Override
   public long getID() {
      return windowHandle;
   }

   @Override
   public void update() {
      glfwSwapBuffers(windowHandle);
      glfwPollEvents();
   }

   @Override
   public void clear(float r, float g, float b, float alpha) {
      glClearColor(r, g, b, alpha);
   }

   @Override
   public void restoreState() {
      glEnable(GL_BLEND);
      glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
   }

   @Override
   public boolean shouldClose() {
      return glfwWindowShouldClose(windowHandle);
   }

   @Override
   public Vector2fc getSize() {
      return size;
   }

   public static Screen create(String title, int width, int height) {
      return new GLFWScreen(title, width, height);
   }
}
