package com.bartlomiejpluta.base.lib.gui;

import com.bartlomiejpluta.base.api.context.Context;
import com.bartlomiejpluta.base.api.event.Event;
import com.bartlomiejpluta.base.api.gui.*;
import com.bartlomiejpluta.base.api.screen.Screen;
import lombok.Getter;
import lombok.NonNull;

import java.util.Map;

import static java.lang.String.format;
import static java.util.Objects.requireNonNull;

public abstract class BaseWindow extends BaseWidget implements Window {
   @Getter
   private final Map<String, Component> refs;
   protected Context context;
   protected GUI gui;
   protected WindowManager manager;
   protected Component content;
   protected WindowPosition windowPosition = WindowPosition.CENTER;

   protected BaseWindow(Context context, GUI gui, Map<String, Component> refs) {
      this.context = context;
      this.gui = gui;
      this.refs = refs;
   }

   @Override
   public void setContent(Component component) {
      if (this.content != null) {
         this.content.setParent(null);
      }

      this.content = component;

      if (component != null) {
         component.setParent(this);
      }
   }

   @Override
   public Component reference(@NonNull String ref) {
      return requireNonNull(refs.get(ref), format("Referenced component (with ref=[%s]) does not exist", ref));
   }

   @Override
   public <T extends Component> T reference(String ref, Class<T> type) {
      return type.cast(requireNonNull(refs.get(ref), format("Referenced component (with ref=[%s]) does not exist", ref)));
   }

   @Override
   public WindowPosition getWindowPosition() {
      return windowPosition;
   }

   @Override
   @Attribute("windowPosition")
   public void setWindowPosition(WindowPosition windowPosition) {
      this.windowPosition = requireNonNull(windowPosition);
   }

   @Override
   protected float getContentWidth() {
      return content.getMarginLeft() + content.getActualWidth() + content.getMarginRight();
   }

   @Override
   protected float getContentHeight() {
      return content.getMarginTop() + content.getActualHeight() + content.getMarginBottom();
   }

   @Override
   public void draw(Screen screen, GUI gui) {
      content.setPosition(x + paddingLeft, y + paddingTop);
      content.draw(screen, gui);
   }

   @Override
   public <E extends Event> void handleEvent(E event) {
      if (content != null) {
         content.handleEvent(event);
      }

      if(!event.isConsumed()) {
         super.handleEvent(event);
      }
   }

   @Override
   public void update(float dt) {
      if (content != null) {
         content.update(dt);
      }
   }

   @Override
   public void onOpen(WindowManager manager, Object[] args) {
      this.manager = manager;
      content.focus();
   }

   @Override
   public void onClose(WindowManager manager) {
      this.manager = null;
      content.blur();
   }
}
