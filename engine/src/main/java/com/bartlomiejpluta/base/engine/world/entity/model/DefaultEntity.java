package com.bartlomiejpluta.base.engine.world.entity.model;

import com.bartlomiejpluta.base.api.entity.Entity;
import com.bartlomiejpluta.base.api.event.Event;
import com.bartlomiejpluta.base.api.event.EventType;
import com.bartlomiejpluta.base.api.map.layer.object.ObjectLayer;
import com.bartlomiejpluta.base.api.move.Direction;
import com.bartlomiejpluta.base.api.move.EntityMovement;
import com.bartlomiejpluta.base.api.move.Movement;
import com.bartlomiejpluta.base.engine.core.gl.object.material.Material;
import com.bartlomiejpluta.base.engine.core.gl.object.mesh.Mesh;
import com.bartlomiejpluta.base.engine.error.AppException;
import com.bartlomiejpluta.base.engine.world.entity.manager.EntitySetManager;
import com.bartlomiejpluta.base.engine.world.movement.MovableSprite;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.joml.Vector2f;
import org.joml.Vector2fc;
import org.joml.Vector2i;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

import static java.util.Objects.requireNonNull;

@EqualsAndHashCode(callSuper = true)
public class DefaultEntity extends MovableSprite implements Entity {
   private final int defaultSpriteColumn;
   private final EntitySetManager entitySetManager;
   private final Map<Direction, Integer> spriteDirectionRows;
   private final Map<Direction, Vector2fc> spriteDefaultRows;
   private final Vector2f entityScale = new Vector2f(1, 1);
   private Vector2fc entitySetSize;

   private final Map<EventType<?>, List<Consumer<? extends Event>>> listeners = new HashMap<>();

   @Getter
   @Setter
   private int zIndex = 0;

   @Getter
   private Direction faceDirection;

   @Getter
   @Setter
   private boolean blocking;

   @Getter
   private ObjectLayer layer;

   private boolean animationEnabled = true;

   private final Queue<EntityInstantAnimation> instantAnimations = new LinkedList<>();

   public DefaultEntity(Mesh mesh, EntitySetManager entitySetManager, int defaultSpriteColumn, Map<Direction, Integer> spriteDirectionRows, Map<Direction, Vector2fc> spriteDefaultRows, String entitySetUid) {
      super(mesh, createMaterial(entitySetManager, entitySetUid));
      this.defaultSpriteColumn = defaultSpriteColumn;
      this.entitySetManager = entitySetManager;
      this.spriteDirectionRows = spriteDirectionRows;
      this.faceDirection = Direction.DOWN;
      this.spriteDefaultRows = spriteDefaultRows;

      var texture = material.getTexture();
      if (texture != null) {
         this.entitySetSize = texture.getSpriteSize();
         super.setScale(entitySetSize.x() * entityScale.x, entitySetSize.y() * entityScale.y);
      }
   }

   @Override
   public void changeEntitySet(String entitySetUid) {
      this.material = createMaterial(entitySetManager, entitySetUid);
      var texture = this.material.getTexture();

      if (texture != null) {
         this.entitySetSize = texture.getSpriteSize();
         super.setScale(entitySetSize.x() * entityScale.x, entitySetSize.y() * entityScale.y);
      } else {
         this.entitySetSize = null;
      }
   }

   @Override
   public boolean isAnimationEnabled() {
      return animationEnabled;
   }

   @Override
   public void setAnimationEnabled(boolean enabled) {
      animationEnabled = enabled;
   }

   @Override
   public void enableAnimation() {
      animationEnabled = true;
   }

   @Override
   public void disableAnimation() {
      animationEnabled = false;
   }

   @Override
   public void toggleAnimationEnabled() {
      animationEnabled = !animationEnabled;
   }

   @Override
   protected boolean shouldAnimate() {
      return animationEnabled && material.getTexture() != null && (isMoving() || !instantAnimations.isEmpty());
   }

   @Override
   protected Vector2fc[] getSpriteAnimationFramesPositions() {
      var row = spriteDirectionRows.get(faceDirection);
      var frames = material.getTexture().getColumns();
      var array = new Vector2f[frames];

      for (int column = 0; column < frames; ++column) {
         array[column] = new Vector2f(column, row);
      }

      return array;
   }

   @Override
   protected void setDefaultAnimationFrame() {
      material.setSpritePosition(spriteDefaultRows.get(faceDirection));
   }

   @Override
   public CompletableFuture<Void> performInstantAnimation() {
      var animation = new EntityInstantAnimation(this, defaultSpriteColumn);
      instantAnimations.add(animation);

      return animation.getFuture();
   }

   @Override
   public Movement prepareMovement(Direction direction) {
      return new EntityMovement(this, direction);
   }

   @Override
   public boolean move(Movement movement) {
      if (super.move(movement)) {
         faceDirection = movement.getDirection();
         return true;
      }

      return false;
   }

   @Override
   public void setFaceDirection(Direction direction) {
      this.faceDirection = direction;
      setDefaultAnimationFrame();
   }

   @Override
   public int chebyshevDistance(Entity other) {
      return chebyshevDistance(other.getCoordinates());
   }

   @Override
   public int manhattanDistance(Entity other) {
      return manhattanDistance(other.getCoordinates());
   }

   @Override
   public Direction getDirectionTowards(Entity target) {
      return Direction.ofVector(target.getCoordinates().sub(getCoordinates(), new Vector2i()));
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
   public void setScaleX(float scaleX) {
      if (entitySetSize == null) {
         throw new AppException("Cannot change Entity scale if no Entity Set is provided");
      }

      this.entityScale.x = scaleX;
      super.setScaleX(entitySetSize.x() * scaleX);
   }

   @Override
   public void setScaleY(float scaleY) {
      if (entitySetSize == null) {
         throw new AppException("Cannot change Entity scale if no Entity Set is provided");
      }

      this.entityScale.y = scaleY;
      super.setScaleY(entitySetSize.y() * scaleY);
   }

   @Override
   public void setScale(float scale) {
      if (entitySetSize == null) {
         throw new AppException("Cannot change Entity scale if no Entity Set is provided");
      }

      this.entityScale.x = scale;
      this.entityScale.y = scale;
      super.setScale(entitySetSize.x() * scale, entitySetSize.y() * scale);
   }

   @Override
   public void setScale(float scaleX, float scaleY) {
      if (entitySetSize == null) {
         throw new AppException("Cannot change Entity scale if no Entity Set is provided");
      }

      this.entityScale.x = scaleX;
      this.entityScale.y = scaleY;
      super.setScale(entitySetSize.x() * scaleX, entitySetSize.y() * scaleY);
   }

   @Override
   public float getScaleX() {
      return entityScale.x;
   }

   @Override
   public float getScaleY() {
      return entityScale.y;
   }

   @SuppressWarnings("unchecked")
   @Override
   public <E extends Event> void handleEvent(E event) {
      var list = listeners.get(event.getType());
      if (list != null) {
         for (var listener : list) {
            ((Consumer<E>) listener).accept(event);
         }
      }
   }

   @Override
   public <E extends Event> void addEventListener(EventType<E> type, Consumer<E> listener) {
      var list = this.listeners.get(type);

      if (list != null) {
         list.add(listener);
      } else {
         list = new ArrayList<>();
         list.add(listener);
         listeners.put(type, list);
      }
   }

   @Override
   public <E extends Event> void removeEventListener(EventType<E> type, Consumer<E> listener) {
      var list = this.listeners.get(type);

      if (list != null) {
         list.remove(listener);
         if (list.isEmpty()) {
            this.listeners.remove(type);
         }
      }
   }

   @Override
   public void update(float dt) {
      super.update(dt);

      var instantAnimation = instantAnimations.peek();
      if (instantAnimation != null && instantAnimation.update() == EntityInstantAnimation.State.COMPLETED) {
         instantAnimations.poll();
      }
   }

   int currentFrame() {
      return currentAnimationFrame;
   }

   private static Material createMaterial(EntitySetManager entitySetManager, String entitySetUid) {
      return entitySetUid != null ? entitySetManager.loadObject(requireNonNull(entitySetUid)) : Material.colored(0, 0, 0, 0);
   }
}
