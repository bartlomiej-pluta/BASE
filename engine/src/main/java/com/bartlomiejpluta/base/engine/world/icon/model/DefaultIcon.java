package com.bartlomiejpluta.base.engine.world.icon.model;

import com.bartlomiejpluta.base.api.event.Event;
import com.bartlomiejpluta.base.api.event.EventType;
import com.bartlomiejpluta.base.api.icon.Icon;
import com.bartlomiejpluta.base.api.map.layer.object.ObjectLayer;
import com.bartlomiejpluta.base.engine.core.gl.object.mesh.Mesh;
import com.bartlomiejpluta.base.engine.error.AppException;
import com.bartlomiejpluta.base.engine.world.icon.manager.IconSetManager;
import com.bartlomiejpluta.base.engine.world.object.Sprite;
import com.bartlomiejpluta.base.lib.event.EventHandler;
import lombok.Getter;
import lombok.Setter;
import org.joml.Vector2f;
import org.joml.Vector2fc;

import java.util.function.Consumer;

public class DefaultIcon extends Sprite implements Icon {
   private final EventHandler eventHandler = new EventHandler();
   private final IconSetManager iconSetManager;
   private final Vector2f iconScale = new Vector2f(1, 1);
   private Vector2fc iconSetSize;

   @Getter
   private String iconSetUid;

   @Getter
   private int iconSetRow;

   @Getter
   private int iconSetColumn;

   @Setter
   @Getter
   private boolean blocking;

   @Getter
   private ObjectLayer layer;

   @Setter
   @Getter
   private int zIndex;

   public DefaultIcon(Mesh mesh, IconSetManager iconSetManager, String iconSetUid, int row, int column) {
      super(mesh, iconSetManager.loadObject(iconSetUid));
      this.iconSetManager = iconSetManager;
      material.setSpritePosition(column, row);

      var texture = material.getTexture();
      if (texture != null) {
         this.iconSetSize = texture.getSpriteSize();
         super.setScale(iconSetSize.x() * iconScale.x, iconSetSize.y() * iconScale.y);
      }

      this.iconSetUid = iconSetUid;
      this.iconSetRow = row;
      this.iconSetColumn = column;
   }

   @Override
   public void onAdd(ObjectLayer layer) {
      this.layer = layer;
   }

   @Override
   public void onRemove(ObjectLayer layer) {
      this.layer = null;
   }

   @Override
   public void changeIcon(int row, int column) {
      material.setSpritePosition(column, row);
      this.iconSetRow = row;
      this.iconSetColumn = column;
   }

   @Override
   public void changeIcon(String iconSetUid, int row, int column) {
      this.material = iconSetManager.loadObject(iconSetUid);
      material.setSpritePosition(column, row);

      this.iconSetUid = iconSetUid;
      this.iconSetRow = row;
      this.iconSetColumn = column;

      var texture = material.getTexture();
      if (texture != null) {
         this.iconSetSize = texture.getSpriteSize();
         super.setScale(iconSetSize.x() * iconScale.x, iconSetSize.y() * iconScale.y);
      } else {
         this.iconSetSize = null;
      }
   }

   @Override
   public void setScaleX(float scaleX) {
      if (iconSetSize == null) {
         throw new AppException("Cannot change Icon scale if no Icon Set is provided");
      }

      this.iconScale.x = scaleX;
      super.setScaleX(iconSetSize.x() * scaleX);
   }

   @Override
   public void setScaleY(float scaleY) {
      if (iconSetSize == null) {
         throw new AppException("Cannot change Icon scale if no Icon Set is provided");
      }

      this.iconScale.y = scaleY;
      super.setScaleY(iconSetSize.y() * scaleY);
   }

   @Override
   public void setScale(float scale) {
      if (iconSetSize == null) {
         throw new AppException("Cannot change Icon scale if no Icon Set is provided");
      }

      this.iconScale.x = scale;
      this.iconScale.y = scale;
      super.setScale(iconSetSize.x() * scale, iconSetSize.y() * scale);
   }

   @Override
   public void setScale(float scaleX, float scaleY) {
      if (iconSetSize == null) {
         throw new AppException("Cannot change Icon scale if no Icon Set is provided");
      }

      this.iconScale.x = scaleX;
      this.iconScale.y = scaleY;
      super.setScale(iconSetSize.x() * scaleX, iconSetSize.y() * scaleY);
   }

   @Override
   public float getScaleX() {
      return iconScale.x;
   }

   @Override
   public float getScaleY() {
      return iconScale.y;
   }

   @Override
   public <E extends Event> void addEventListener(EventType<E> type, Consumer<E> listener) {
      eventHandler.addListener(type, listener);
   }

   @Override
   public <E extends Event> void removeEventListener(EventType<E> type, Consumer<E> listener) {
      eventHandler.removeListener(type, listener);
   }

   @Override
   public <E extends Event> void handleEvent(E event) {
      eventHandler.handleEvent(event);
   }

   @Override
   public void update(float dt) {
      // noop
   }
}
