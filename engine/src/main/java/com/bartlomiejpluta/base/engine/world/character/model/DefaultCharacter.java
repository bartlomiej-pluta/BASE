package com.bartlomiejpluta.base.engine.world.character.model;

import com.bartlomiejpluta.base.api.character.Character;
import com.bartlomiejpluta.base.api.event.Event;
import com.bartlomiejpluta.base.api.event.EventType;
import com.bartlomiejpluta.base.api.map.layer.object.ObjectLayer;
import com.bartlomiejpluta.base.api.move.CharacterMovement;
import com.bartlomiejpluta.base.api.move.Direction;
import com.bartlomiejpluta.base.api.move.Movement;
import com.bartlomiejpluta.base.engine.core.gl.object.material.Material;
import com.bartlomiejpluta.base.engine.core.gl.object.mesh.Mesh;
import com.bartlomiejpluta.base.engine.error.AppException;
import com.bartlomiejpluta.base.engine.world.character.manager.CharacterSetManager;
import com.bartlomiejpluta.base.engine.world.movement.MovableSprite;
import com.bartlomiejpluta.base.lib.event.EventHandler;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.joml.Vector2f;
import org.joml.Vector2fc;

import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

import static java.util.Objects.requireNonNull;

@EqualsAndHashCode(callSuper = true)
public class DefaultCharacter extends MovableSprite implements Character {
   private final int defaultSpriteColumn;
   private final CharacterSetManager characterSetManager;
   private final Map<Direction, Integer> spriteDirectionRows;
   private final Map<Direction, Vector2fc> spriteDefaultRows;
   private final Vector2f characterScale = new Vector2f(1, 1);
   private Vector2fc characterSetSize;

   private final EventHandler eventHandler = new EventHandler();

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

   private final Queue<CharacterInstantAnimation> instantAnimations = new LinkedList<>();

   public DefaultCharacter(Mesh mesh, CharacterSetManager characterSetManager, int defaultSpriteColumn, Map<Direction, Integer> spriteDirectionRows, Map<Direction, Vector2fc> spriteDefaultRows, String characterSetUid) {
      super(mesh, createMaterial(characterSetManager, characterSetUid));
      this.defaultSpriteColumn = defaultSpriteColumn;
      this.characterSetManager = characterSetManager;
      this.spriteDirectionRows = spriteDirectionRows;
      this.faceDirection = Direction.DOWN;
      this.spriteDefaultRows = spriteDefaultRows;

      var texture = material.getTexture();
      if (texture != null) {
         this.characterSetSize = texture.getSpriteSize();
         super.setScale(characterSetSize.x() * characterScale.x, characterSetSize.y() * characterScale.y);
      }
   }

   private static Material createMaterial(CharacterSetManager characterSetManager, String characterSetUid) {
      return characterSetUid != null ? characterSetManager.loadObject(requireNonNull(characterSetUid)) : Material.colored(0, 0, 0, 0);
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
   public void changeCharacterSet(String characterSetUid) {
      this.material = createMaterial(characterSetManager, characterSetUid);
      var texture = this.material.getTexture();

      if (texture != null) {
         this.characterSetSize = texture.getSpriteSize();
         super.setScale(characterSetSize.x() * characterScale.x, characterSetSize.y() * characterScale.y);
      } else {
         this.characterSetSize = null;
      }
   }

   @Override
   public CompletableFuture<Void> performInstantAnimation() {
      var animation = new CharacterInstantAnimation(this, defaultSpriteColumn);
      instantAnimations.add(animation);

      return animation.getFuture();
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
   public void onAdd(ObjectLayer layer) {
      this.layer = layer;
   }

   @Override
   public void onRemove(ObjectLayer layer) {
      this.layer = null;
   }

   @Override
   public Movement prepareMovement(Direction direction) {
      return new CharacterMovement(this, direction);
   }

   @Override
   public void setScaleX(float scaleX) {
      if (characterSetSize == null) {
         throw new AppException("Cannot change Character scale if no Character Set is provided");
      }

      this.characterScale.x = scaleX;
      super.setScaleX(characterSetSize.x() * scaleX);
   }

   @Override
   public void setScaleY(float scaleY) {
      if (characterSetSize == null) {
         throw new AppException("Cannot change Character scale if no Character Set is provided");
      }

      this.characterScale.y = scaleY;
      super.setScaleY(characterSetSize.y() * scaleY);
   }

   @Override
   public void setScale(float scale) {
      if (characterSetSize == null) {
         throw new AppException("Cannot change Character scale if no Character Set is provided");
      }

      this.characterScale.x = scale;
      this.characterScale.y = scale;
      super.setScale(characterSetSize.x() * scale, characterSetSize.y() * scale);
   }

   @Override
   public float getScaleX() {
      return characterScale.x;
   }

   @Override
   public float getScaleY() {
      return characterScale.y;
   }

   @Override
   public <E extends Event> void handleEvent(E event) {
      eventHandler.handleEvent(event);
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
   public void setScale(float scaleX, float scaleY) {
      if (characterSetSize == null) {
         throw new AppException("Cannot change Character scale if no Character Set is provided");
      }

      this.characterScale.x = scaleX;
      this.characterScale.y = scaleY;
      super.setScale(characterSetSize.x() * scaleX, characterSetSize.y() * scaleY);
   }

   int currentFrame() {
      return currentAnimationFrame;
   }

   @Override
   public void update(float dt) {
      super.update(dt);

      var instantAnimation = instantAnimations.peek();
      if (instantAnimation != null && instantAnimation.update() == CharacterInstantAnimation.State.COMPLETED) {
         instantAnimations.poll();
      }
   }
}
