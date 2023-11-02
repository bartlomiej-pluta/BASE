package com.bartlomiejpluta.base.lib.gui;

import com.bartlomiejpluta.base.api.context.Context;
import com.bartlomiejpluta.base.api.event.Event;
import com.bartlomiejpluta.base.api.gui.Component;
import com.bartlomiejpluta.base.api.gui.GUI;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static java.util.Collections.unmodifiableList;
import static java.util.Objects.requireNonNull;

public abstract class BaseContainer extends BaseComponent {
   protected final List<Component> children = new LinkedList<>();
   private final List<Component> readOnlyChildren = unmodifiableList(children);

   public BaseContainer(Context context, GUI gui, Map<String, Component> refs) {
      super(context, gui, refs);
   }

   @Override
   public List<Component> getChildren() {
      return readOnlyChildren;
   }

   @Override
   public void add(Component component) {
      this.children.add(requireNonNull(component));
      component.setParent(this);
   }

   @Override
   public void remove(Component component) {
      this.children.remove(requireNonNull(component));
      component.setParent(null);
   }

   public void removeAllChildren() {
      this.children.clear();
   }

   protected float maxChildrenWidth() {
      var theWidestChild = 0.0f;

      for (var child : children) {
         var width = child.getMarginLeft() + child.getActualWidth() + child.getMarginRight();
         if (width > theWidestChild) {
            theWidestChild = width;
         }
      }

      return theWidestChild;
   }

   protected float maxChildrenHeight() {
      var theHighestChild = 0.0f;

      for (var child : children) {
         var height = child.getMarginTop() + child.getActualHeight() + child.getMarginBottom();
         if (height > theHighestChild) {
            theHighestChild = height;
         }
      }

      return theHighestChild;
   }

   protected float sumChildrenWidth() {
      var childrenWidth = 0.0f;

      for (var child : children) {
         childrenWidth += child.getMarginLeft() + child.getWidth() + child.getMarginRight();
      }

      return childrenWidth;
   }

   protected float sumChildrenHeight() {
      var childrenHeight = 0.0f;

      for (var child : children) {
         childrenHeight += child.getMarginTop() + child.getHeight() + child.getMarginBottom();
      }

      return childrenHeight;
   }

   @Override
   public void focus() {
      blur();
      super.focus();
   }

   @Override
   public void blur() {
      super.blur();

      for (var child : children) {
         child.blur();
      }
   }

   protected void blurChildren() {
      for (var child : children) {
         child.blur();
      }
   }

   @Override
   public <E extends Event> void handleEvent(E event) {
      // Populate event downstream
      for (var child : children) {
         if (event.isConsumed()) {
            return;
         }

         child.handleEvent(event);
      }

      eventHandler.handleEvent(event);
   }

   @Override
   public void update(float dt) {
      for (var child : children) {
         child.update(dt);
      }
   }
}
