package com.bartlomiejpluta.base.util.world;

import com.bartlomiejpluta.base.api.camera.Camera;
import com.bartlomiejpluta.base.api.context.Context;
import com.bartlomiejpluta.base.api.entity.Entity;
import com.bartlomiejpluta.base.api.event.Event;
import com.bartlomiejpluta.base.api.event.EventType;
import com.bartlomiejpluta.base.api.map.layer.object.ObjectLayer;
import com.bartlomiejpluta.base.api.map.model.GameMap;
import com.bartlomiejpluta.base.internal.logic.Updatable;
import com.bartlomiejpluta.base.util.math.Distance;
import com.bartlomiejpluta.base.util.math.MathUtil;
import com.bartlomiejpluta.base.util.random.DiceRoller;
import lombok.NonNull;
import org.joml.Vector2i;
import org.joml.Vector2ic;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.function.Supplier;


public class EntitySpawner implements Updatable {
   private static final int MAX_REPOSITION_ATTEMPTS = 10;
   private final Random random = new Random();
   private final List<Entity> spawnedEntities = new LinkedList<>();
   private final List<Supplier<Entity>> spawners = new ArrayList<>();
   private final Vector2ic origin;
   private final Context context;
   private final Camera camera;
   private final GameMap map;
   private final ObjectLayer layer;
   private DiceRoller interval = DiceRoller.of("90d2");
   private int range = 4;
   private float spawnChance = 50;
   private DiceRoller countRoller = DiceRoller.of("1d4");
   private EventType<? extends Event> entityRemoveEvent;
   private float accumulator = 10000f;
   private boolean spawnOutsideViewport = false;
   private float threshold;

   public EntitySpawner(int x, int y, @NonNull Context context, @NonNull GameMap map, @NonNull ObjectLayer layer) {
      this.origin = new Vector2i(x, y);
      this.context = context;
      this.camera = context.getCamera();
      this.map = map;
      this.layer = layer;
      drawThreshold();
   }

   public EntitySpawner interval(@NonNull String interval) {
      this.interval = DiceRoller.of(interval);
      drawThreshold();
      return this;
   }

   public EntitySpawner range(int range) {
      this.range = range;
      return this;
   }

   public EntitySpawner spawnChance(float change) {
      this.spawnChance = change;
      return this;
   }

   public EntitySpawner count(String dices) {
      this.countRoller = DiceRoller.of(dices);
      return this;
   }

   public EntitySpawner trackEntities(@NonNull EventType<? extends Event> entityRemoveEvent) {
      this.entityRemoveEvent = entityRemoveEvent;
      return this;
   }

   public EntitySpawner spawnOutsideViewport() {
      this.spawnOutsideViewport = true;
      return this;
   }

   public EntitySpawner spawnOnCreate() {
      this.threshold = 0;
      return this;
   }

   public EntitySpawner spawn(@NonNull Supplier<Entity> spawner) {
      this.spawners.add(spawner);
      return this;
   }

   private void drawThreshold() {
      threshold = interval.roll() * 1000f;
   }

   @Override
   public void update(float dt) {
      accumulator += dt * 1000;
      if (accumulator >= threshold) {
         spawn();
         drawThreshold();
         accumulator = 0f;
      }
   }

   private void spawn() {
      if (random.nextFloat() > spawnChance) {
         return;
      }

      // Spawn multiple entities at the time
      var roll = countRoller.roll();
      var count = Math.max(0, roll - spawnedEntities.size());
      for (int i = 0; i < count; ++i) {
         var attempts = 0;
         var coordinates = new Vector2i();

         while (true) {
            // Give up if too many fails during drawing the proper coordinates
            if (attempts > MAX_REPOSITION_ATTEMPTS) {
               return;
            }

            // Draw the coordinates and make sure they are inside the current map boundaries
            coordinates.x = MathUtil.clamp(origin.x() + random.nextInt(2 * range) - range, 0, map.getColumns() - 1);
            coordinates.y = MathUtil.clamp(origin.y() + random.nextInt(2 * range) - range, 0, map.getRows() - 1);

            // If tile is not reachable, draw the coordinates again
            if (!layer.isTileReachable(coordinates)) {
               ++attempts;
               continue;
            }

            // If we want to keep spawning entities outside the camera view
            // check if the drawn coordinates are inside frustum.
            // If so, draw it again.
            if (spawnOutsideViewport && camera.insideFrustum(context, coordinates.x, coordinates.y)) {
               ++attempts;
               continue;
            }

            // We need also to drop the coordinates that are too far from the spawner origin
            if (Distance.manhattan(origin, coordinates) > range) {
               ++attempts;
               continue;
            }

            // We found the proper coordinates
            break;
         }

         // Draw the entity spawner
         var spawner = spawners.get(random.nextInt(spawners.size()));

         // Create the entity and push it onto the map layer
         var entity = spawner.get();
         entity.setCoordinates(coordinates);
         layer.addEntity(entity);

         // If we want to keep the number of spawned entities per spawner almost constant
         // we need to know when the entity should be removed (i.e. it has been killed by player).
         if (entityRemoveEvent != null) {
            spawnedEntities.add(entity);
            entity.addEventListener(entityRemoveEvent, e -> spawnedEntities.remove(entity));
         }
      }
   }
}
