package com.bartlomiejpluta.base.api.game.gui.component;

import com.bartlomiejpluta.base.api.game.input.KeyEvent;

import java.util.LinkedList;
import java.util.List;

public abstract class BaseContainer extends BaseComponent implements Container {
   protected final List<Component> children = new LinkedList<>();

   @Override
   public void add(Component component) {
      this.children.add(component);
      component.setParent(this);
   }

   @Override
   public void remove(Component component) {
      this.children.remove(component);
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
         if (!event.isConsumed()) {
            return;
         }

         child.handleKeyEvent(event);
      }
   }
}
