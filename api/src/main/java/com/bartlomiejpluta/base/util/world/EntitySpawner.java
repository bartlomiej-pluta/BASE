package com.bartlomiejpluta.base.util.world;

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
   private final GameMap map;
   private final ObjectLayer layer;
   private float maxTime = 10000f;
   private int range = 4;
   private int spawnChance = 50;
   private DiceRoller countRoller = DiceRoller.of("1d4");
   private Entity awayEntity;
   private EventType<? extends Event> entityRemoveEvent;

   private float accumulator = 10000f;
   private float threshold;

   public EntitySpawner(int x, int y, @NonNull GameMap map, @NonNull ObjectLayer layer) {
      this.origin = new Vector2i(x, y);
      this.map = map;
      this.layer = layer;
      drawThreshold();
   }

   public EntitySpawner maxTime(float maxTime) {
      this.maxTime = maxTime;
      this.accumulator = maxTime;
      drawThreshold();
      return this;
   }

   public EntitySpawner range(int range) {
      this.range = range;
      return this;
   }

   public EntitySpawner spawnChance(int percent) {
      this.spawnChance = percent;
      return this;
   }

   public EntitySpawner count(String dices) {
      this.countRoller = DiceRoller.of(dices);
      return this;
   }

   public EntitySpawner entityAway(@NonNull Entity awayEntity) {
      this.awayEntity = awayEntity;
      return this;
   }

   public EntitySpawner trackEntities(@NonNull EventType<? extends Event> entityRemoveEvent) {
      this.entityRemoveEvent = entityRemoveEvent;
      return this;
   }

   public EntitySpawner spawn(@NonNull Supplier<Entity> spawner) {
      this.spawners.add(spawner);
      return this;
   }

   private void drawThreshold() {
      threshold = random.nextFloat(maxTime);
   }

   @Override
   public void update(float dt) {
      if (awayEntity != null && awayEntity.manhattanDistance(origin) <= range) {
         return;
      }

      accumulator += dt * 1000;
      if (accumulator >= threshold) {
         spawn();
         drawThreshold();
         accumulator = 0f;
      }
   }

   private void spawn() {
      if (random.nextInt(100) < spawnChance) {
         return;
      }

      var count = Math.max(0, countRoller.roll() - spawnedEntities.size());
      for (int i = 0; i < count; ++i) {
         var attempts = 0;
         var coordinates = new Vector2i();

         do {
            coordinates.x = MathUtil.clamp(origin.x() + random.nextInt(2 * range) - range, 0, map.getColumns() - 1);
            coordinates.y = MathUtil.clamp(origin.y() + random.nextInt(2 * range) - range, 0, map.getRows() - 1);

            if (!layer.isTileReachable(coordinates)) {
               ++attempts;
               continue;
            }

            if (attempts > MAX_REPOSITION_ATTEMPTS) {
               return;
            }

         } while (Distance.manhattan(origin, coordinates) <= range);

         var spawner = spawners.get(random.nextInt(spawners.size()));
         var entity = spawner.get();

         entity.setCoordinates(coordinates);
         layer.addEntity(entity);

         if (entityRemoveEvent != null) {
            spawnedEntities.add(entity);
            entity.addEventListener(entityRemoveEvent, e -> spawnedEntities.remove(entity));
         }
      }
   }
}
