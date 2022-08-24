package com.bartlomiejpluta.base.engine.gui.render;

import com.bartlomiejpluta.base.api.gui.IconSet;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
public class NanoVGIconSet implements IconSet {
   private final int imageHandle;
   private final int width;
   private final int height;
   private final int rows;
   private final int columns;
   private final int iconWidth;
   private final int iconHeight;

   public NanoVGIconSet(int imageHandle, int width, int height, int rows, int columns) {
      this.imageHandle = imageHandle;
      this.width = width;
      this.height = height;
      this.rows = rows;
      this.columns = columns;
      this.iconWidth = width / columns;
      this.iconHeight = height / rows;
   }
}
