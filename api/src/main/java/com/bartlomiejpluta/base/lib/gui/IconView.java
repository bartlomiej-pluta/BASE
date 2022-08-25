package com.bartlomiejpluta.base.lib.gui;

import com.bartlomiejpluta.base.api.context.Context;
import com.bartlomiejpluta.base.api.gui.Component;
import com.bartlomiejpluta.base.api.gui.GUI;
import com.bartlomiejpluta.base.api.gui.IconSet;
import com.bartlomiejpluta.base.api.gui.Paint;
import com.bartlomiejpluta.base.api.icon.Icon;
import com.bartlomiejpluta.base.api.screen.Screen;
import lombok.NonNull;
import lombok.Setter;

import java.util.Map;

public class IconView extends BaseComponent {
   private final GUI gui;
   private final Paint paint;

   private IconSet iconSet;

   @NonNull
   @Setter
   private Float scaleX = 1f;

   @NonNull
   @Setter
   private Float scaleY = 1f;

   @NonNull
   @Setter
   private Float angle = 0f;

   @NonNull
   @Setter
   private Float alpha = 1f;

   @NonNull
   @Setter
   private Integer iconSetRow = 0;

   @NonNull
   @Setter
   private Integer iconSetColumn = 0;


   public IconView(Context context, GUI gui, Map<String, Component> refs) {
      super(context, gui, refs);
      this.gui = gui;
      this.paint = gui.createPaint();
   }

   public void setIcon(Icon icon) {
      if (icon == null) {
         iconSet = null;
         return;
      }

      iconSet = gui.getIconSet(icon.getIconSetUid());
      iconSetRow = icon.getIconSetRow();
      iconSetColumn = icon.getIconSetColumn();
   }

   public void setIconSet(String iconSetUid) {
      if (iconSetUid == null) {
         iconSet = null;
         return;
      }

      iconSet = gui.getIconSet(iconSetUid);
   }

   public void setScale(@NonNull Float scale) {
      this.scaleX = scale;
      this.scaleY = scale;
   }

   @Override
   protected float getContentWidth() {
      return iconSet != null ? iconSet.getIconWidth() * scaleX : 0;
   }

   @Override
   protected float getContentHeight() {
      return iconSet != null ? iconSet.getIconHeight() * scaleY : 0;
   }

   @Override
   public void draw(Screen screen, GUI gui) {
      if (iconSet == null) {
         return;
      }

      gui.icon(x, y, scaleX, scaleY, angle, alpha, iconSet, iconSetRow, iconSetColumn, paint);
   }
}
