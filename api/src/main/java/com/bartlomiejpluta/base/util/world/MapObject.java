package com.bartlomiejpluta.base.util.world;


import com.bartlomiejpluta.base.api.character.Character;
import com.bartlomiejpluta.base.lib.character.CharacterDelegate;
import com.bartlomiejpluta.base.util.path.CharacterPath;
import com.bartlomiejpluta.base.util.path.PathExecutor;
import lombok.NonNull;
import lombok.SneakyThrows;

import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

import static com.bartlomiejpluta.base.api.move.Direction.*;
import static java.util.concurrent.CompletableFuture.completedFuture;

public abstract class MapObject extends CharacterDelegate {
   protected final PathExecutor<MapObject> pathExecutor = new PathExecutor<>(this);
   private final short frame;
   private int pathLength;

   protected boolean interacting = false;
   private CompletableFuture<Boolean> condition;
   private CompletableFuture<?> future;

   private float step = 0.05f;
   private float cooldown = 0.5f;

   private Supplier<CompletableFuture<Boolean>> triggerCondition;
   private Supplier<CompletableFuture<Boolean>> interactionCondition;
   private Supplier<CompletableFuture<Boolean>> completionCondition;

   private Supplier<CompletableFuture<?>> beforeAll;
   private Supplier<CompletableFuture<?>> before;
   private Supplier<CompletableFuture<?>> after;
   private Supplier<CompletableFuture<?>> afterAll;


   public MapObject(@NonNull Character character, short frame) {
      super(character);

      this.frame = frame;
      setBlocking(true);
      disableAnimation();
      setAnimationFrame(frame);
      pathExecutor.setRepeat(1);

      initPath();
   }

   private void initPath() {
      var path = new CharacterPath<MapObject>()
              .run(this::initTriggerCondition)
              .suspend(this::checkCondition)
              .run(this::startInteraction)
              .suspend(this::waitForFuture)
              .turn(LEFT, frame)
              .wait(step)
              .turn(RIGHT, frame)
              .wait(step)
              .turn(UP, frame)
              .wait(step)
              .run(this::initInteractionCondition)
              .suspend(this::checkCondition)
              .run(this::runInteraction)
              .suspend(this::waitForFuture)
              .run(this::initCompletionCondition)
              .suspend(this::checkCondition)
              .wait(step)
              .turn(RIGHT, frame)
              .wait(step)
              .turn(LEFT, frame)
              .wait(step)
              .turn(DOWN, frame)
              .run(this::finishInteraction)
              .suspend(this::waitForFuture)
              .wait(cooldown)
              .run(this::completeInteraction);

      pathLength = path.getPath().size();

      pathExecutor.setPath(path);
   }

   private void initTriggerCondition() {
      this.condition = triggerCondition != null ? triggerCondition.get() : completedFuture(true);
   }

   @SneakyThrows
   private boolean checkCondition(MapObject object) {
      if (!condition.isDone()) {
         return false;
      }

      if (!condition.get()) {
         // We are interested in the last before last segment which is wait(cooldown)
         // to put some cooldown after aborting interaction so that
         // it won't be immediately triggered again by accident
         pathExecutor.setCurrentSegment(pathLength - 2);
      }

      return true;
   }

   private void initInteractionCondition() {
      this.condition = interactionCondition != null ? interactionCondition.get() : completedFuture(true);
   }

   private void initCompletionCondition() {
      this.condition = completionCondition != null ? completionCondition.get() : completedFuture(true);
   }

   public MapObject animationStepDuration(float duration) {
      this.step = duration;
      initPath();
      return this;
   }

   public MapObject interactionCooldown(float cooldown) {
      this.cooldown = cooldown;
      initPath();
      return this;
   }

   public MapObject triggerCondition(@NonNull Supplier<CompletableFuture<Boolean>> condition) {
      this.triggerCondition = condition;
      return this;
   }

   public MapObject interactionCondition(@NonNull Supplier<CompletableFuture<Boolean>> condition) {
      this.interactionCondition = condition;
      return this;
   }

   public MapObject completionCondition(@NonNull Supplier<CompletableFuture<Boolean>> condition) {
      this.completionCondition = condition;
      return this;
   }

   public MapObject beforeAll(@NonNull Supplier<CompletableFuture<?>> action) {
      this.beforeAll = action;
      return this;
   }

   public MapObject before(@NonNull Supplier<CompletableFuture<?>> action) {
      this.before = action;
      return this;
   }

   public MapObject after(@NonNull Supplier<CompletableFuture<?>> action) {
      this.after = action;
      return this;
   }

   public MapObject afterAll(@NonNull Supplier<CompletableFuture<?>> action) {
      this.afterAll = action;
      return this;
   }

   public void triggerInteraction() {
      if (interacting) {
         return;
      }

      pathExecutor.reset();

      if (beforeAll != null) {
         beforeAll.get().thenRun(() -> interacting = true);
      } else {
         interacting = true;
      }
   }

   private void startInteraction() {
      this.future = onInteractionBegin();
   }

   private CompletableFuture<?> waitForFuture() {
      return future;
   }

   private void runInteraction() {
      this.future = (before != null ? before.get() : completedFuture(null))
              .thenCompose(v -> interact())
              .thenCompose(v -> (after != null ? after.get() : completedFuture(null)));
   }

   private void finishInteraction() {
      this.future = onIntegrationEnd().thenCompose(v -> afterAll != null ? afterAll.get() : completedFuture(null));
   }

   private void completeInteraction() {
      interacting = false;
      reset();
   }

   @Override
   public void update(float dt) {
      if (interacting) {
         pathExecutor.execute(getLayer(), dt);
      }
   }

   protected final void reset() {
      setFaceDirection(DOWN);
      pathExecutor.reset();
      interacting = false;
   }

   @NonNull
   protected CompletableFuture<?> onInteractionBegin() {
      return completedFuture(null);
   }

   @NonNull
   protected CompletableFuture<?> onIntegrationEnd() {
      return completedFuture(null);
   }

   protected abstract CompletableFuture<?> interact();
}