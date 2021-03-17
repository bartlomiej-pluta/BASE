package com.bartlomiejpluta.base.api.game.gui.component;

import com.bartlomiejpluta.base.api.game.context.Context;
import com.bartlomiejpluta.base.api.game.gui.base.GUI;
import com.bartlomiejpluta.base.api.game.input.KeyEvent;

import java.util.LinkedList;
import java.util.List;

import static java.util.Collections.unmodifiableList;
import static java.util.Objects.requireNonNull;

public abstract class BaseContainer extends BaseComponent implements Container {
   protected final List<Component> children = new LinkedList<>();
   private final List<Component> readOnlyChildren = unmodifiableList(children);

   public BaseContainer(Context context, GUI gui) {
      super(context, gui);
   }

   @Override
   public Iterable<Component> getChildren() {
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
   public void blur() {
      super.blur();

      for (var child : children) {
         child.blur();
      }
   }

   @Override
   public void handleKeyEvent(KeyEvent event) {
      for (var child : children) {
         if (event.isConsumed()) {
            return;
         }

         child.handleKeyEvent(event);
      }
   }
}
