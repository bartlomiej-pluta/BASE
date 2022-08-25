package com.bartlomiejpluta.base.lib.gui;

import com.bartlomiejpluta.base.api.gui.GUI;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum TextAlignment {
   LEFT(GUI.ALIGN_LEFT),
   CENTER(GUI.ALIGN_CENTER),
   RIGHT(GUI.ALIGN_RIGHT),
   TOP(GUI.ALIGN_TOP),
   MIDDLE(GUI.ALIGN_MIDDLE),
   BOTTOM(GUI.ALIGN_BOTTOM),
   BASELINE(GUI.ALIGN_BASELINE);

   @Getter
   private final int align;
}
