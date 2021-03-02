package com.bartlomiejpluta.base.game.input;

import com.bartlomiejpluta.base.api.input.Key;
import com.bartlomiejpluta.base.api.input.Keyboard;
import com.bartlomiejpluta.base.core.ui.Window;
import lombok.RequiredArgsConstructor;

import static org.lwjgl.glfw.GLFW.*;

@RequiredArgsConstructor
public class GLFWKeyboard implements Keyboard {
   private final Window window;

   @Override
   public boolean isKeyPressed(Key key) {
      return window.isKeyPressed(glfwCode(key));
   }

   private static int glfwCode(Key key) {
      return switch (key) {
         case KEY_SPACE -> GLFW_KEY_SPACE;
         case KEY_APOSTROPHE -> GLFW_KEY_APOSTROPHE;
         case KEY_COMMA -> GLFW_KEY_COMMA;
         case KEY_MINUS -> GLFW_KEY_MINUS;
         case KEY_PERIOD -> GLFW_KEY_PERIOD;
         case KEY_SLASH -> GLFW_KEY_SLASH;
         case KEY_0 -> GLFW_KEY_0;
         case KEY_1 -> GLFW_KEY_1;
         case KEY_2 -> GLFW_KEY_2;
         case KEY_3 -> GLFW_KEY_3;
         case KEY_4 -> GLFW_KEY_4;
         case KEY_5 -> GLFW_KEY_5;
         case KEY_6 -> GLFW_KEY_6;
         case KEY_7 -> GLFW_KEY_7;
         case KEY_8 -> GLFW_KEY_8;
         case KEY_9 -> GLFW_KEY_9;
         case KEY_SEMICOLON -> GLFW_KEY_SEMICOLON;
         case KEY_EQUAL -> GLFW_KEY_EQUAL;
         case KEY_A -> GLFW_KEY_A;
         case KEY_B -> GLFW_KEY_B;
         case KEY_C -> GLFW_KEY_C;
         case KEY_D -> GLFW_KEY_D;
         case KEY_E -> GLFW_KEY_E;
         case KEY_F -> GLFW_KEY_F;
         case KEY_G -> GLFW_KEY_G;
         case KEY_H -> GLFW_KEY_H;
         case KEY_I -> GLFW_KEY_I;
         case KEY_J -> GLFW_KEY_J;
         case KEY_K -> GLFW_KEY_K;
         case KEY_L -> GLFW_KEY_L;
         case KEY_M -> GLFW_KEY_M;
         case KEY_N -> GLFW_KEY_N;
         case KEY_O -> GLFW_KEY_O;
         case KEY_P -> GLFW_KEY_P;
         case KEY_Q -> GLFW_KEY_Q;
         case KEY_R -> GLFW_KEY_R;
         case KEY_S -> GLFW_KEY_S;
         case KEY_T -> GLFW_KEY_T;
         case KEY_U -> GLFW_KEY_U;
         case KEY_V -> GLFW_KEY_V;
         case KEY_W -> GLFW_KEY_W;
         case KEY_X -> GLFW_KEY_X;
         case KEY_Y -> GLFW_KEY_Y;
         case KEY_Z -> GLFW_KEY_Z;
         case KEY_LEFT_BRACKET -> GLFW_KEY_LEFT_BRACKET;
         case KEY_BACKSLASH -> GLFW_KEY_BACKSLASH;
         case KEY_RIGHT_BRACKET -> GLFW_KEY_RIGHT_BRACKET;
         case KEY_GRAVE_ACCENT -> GLFW_KEY_GRAVE_ACCENT;
         case KEY_WORLD_1 -> GLFW_KEY_WORLD_1;
         case KEY_WORLD_2 -> GLFW_KEY_WORLD_2;
         case KEY_ESCAPE -> GLFW_KEY_ESCAPE;
         case KEY_ENTER -> GLFW_KEY_ENTER;
         case KEY_TAB -> GLFW_KEY_TAB;
         case KEY_BACKSPACE -> GLFW_KEY_BACKSPACE;
         case KEY_INSERT -> GLFW_KEY_INSERT;
         case KEY_DELETE -> GLFW_KEY_DELETE;
         case KEY_RIGHT -> GLFW_KEY_RIGHT;
         case KEY_LEFT -> GLFW_KEY_LEFT;
         case KEY_DOWN -> GLFW_KEY_DOWN;
         case KEY_UP -> GLFW_KEY_UP;
         case KEY_PAGE_UP -> GLFW_KEY_PAGE_UP;
         case KEY_PAGE_DOWN -> GLFW_KEY_PAGE_DOWN;
         case KEY_HOME -> GLFW_KEY_HOME;
         case KEY_END -> GLFW_KEY_END;
         case KEY_CAPS_LOCK -> GLFW_KEY_CAPS_LOCK;
         case KEY_SCROLL_LOCK -> GLFW_KEY_SCROLL_LOCK;
         case KEY_NUM_LOCK -> GLFW_KEY_NUM_LOCK;
         case KEY_PRINT_SCREEN -> GLFW_KEY_PRINT_SCREEN;
         case KEY_PAUSE -> GLFW_KEY_PAUSE;
         case KEY_F1 -> GLFW_KEY_F1;
         case KEY_F2 -> GLFW_KEY_F2;
         case KEY_F3 -> GLFW_KEY_F3;
         case KEY_F4 -> GLFW_KEY_F4;
         case KEY_F5 -> GLFW_KEY_F5;
         case KEY_F6 -> GLFW_KEY_F6;
         case KEY_F7 -> GLFW_KEY_F7;
         case KEY_F8 -> GLFW_KEY_F8;
         case KEY_F9 -> GLFW_KEY_F9;
         case KEY_F10 -> GLFW_KEY_F10;
         case KEY_F11 -> GLFW_KEY_F11;
         case KEY_F12 -> GLFW_KEY_F12;
         case KEY_F13 -> GLFW_KEY_F13;
         case KEY_F14 -> GLFW_KEY_F14;
         case KEY_F15 -> GLFW_KEY_F15;
         case KEY_F16 -> GLFW_KEY_F16;
         case KEY_F17 -> GLFW_KEY_F17;
         case KEY_F18 -> GLFW_KEY_F18;
         case KEY_F19 -> GLFW_KEY_F19;
         case KEY_F20 -> GLFW_KEY_F20;
         case KEY_F21 -> GLFW_KEY_F21;
         case KEY_F22 -> GLFW_KEY_F22;
         case KEY_F23 -> GLFW_KEY_F23;
         case KEY_F24 -> GLFW_KEY_F24;
         case KEY_F25 -> GLFW_KEY_F25;
         case KEY_KP_0 -> GLFW_KEY_KP_0;
         case KEY_KP_1 -> GLFW_KEY_KP_1;
         case KEY_KP_2 -> GLFW_KEY_KP_2;
         case KEY_KP_3 -> GLFW_KEY_KP_3;
         case KEY_KP_4 -> GLFW_KEY_KP_4;
         case KEY_KP_5 -> GLFW_KEY_KP_5;
         case KEY_KP_6 -> GLFW_KEY_KP_6;
         case KEY_KP_7 -> GLFW_KEY_KP_7;
         case KEY_KP_8 -> GLFW_KEY_KP_8;
         case KEY_KP_9 -> GLFW_KEY_KP_9;
         case KEY_KP_DECIMAL -> GLFW_KEY_KP_DECIMAL;
         case KEY_KP_DIVIDE -> GLFW_KEY_KP_DIVIDE;
         case KEY_KP_MULTIPLY -> GLFW_KEY_KP_MULTIPLY;
         case KEY_KP_SUBTRACT -> GLFW_KEY_KP_SUBTRACT;
         case KEY_KP_ADD -> GLFW_KEY_KP_ADD;
         case KEY_KP_ENTER -> GLFW_KEY_KP_ENTER;
         case KEY_KP_EQUAL -> GLFW_KEY_KP_EQUAL;
         case KEY_LEFT_SHIFT -> GLFW_KEY_LEFT_SHIFT;
         case KEY_LEFT_CONTROL -> GLFW_KEY_LEFT_CONTROL;
         case KEY_LEFT_ALT -> GLFW_KEY_LEFT_ALT;
         case KEY_LEFT_SUPER -> GLFW_KEY_LEFT_SUPER;
         case KEY_RIGHT_SHIFT -> GLFW_KEY_RIGHT_SHIFT;
         case KEY_RIGHT_CONTROL -> GLFW_KEY_RIGHT_CONTROL;
         case KEY_RIGHT_ALT -> GLFW_KEY_RIGHT_ALT;
         case KEY_RIGHT_SUPER -> GLFW_KEY_RIGHT_SUPER;
         case KEY_MENU -> GLFW_KEY_MENU;
         case KEY_LAST -> GLFW_KEY_LAST;
      };
   }
}