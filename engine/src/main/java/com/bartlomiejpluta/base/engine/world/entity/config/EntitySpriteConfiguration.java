package com.bartlomiejpluta.base.engine.world.entity.config;

import com.bartlomiejpluta.base.api.move.Direction;
import lombok.Data;
import org.joml.Vector2i;
import org.joml.Vector2ic;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Data
@Configuration
@ConfigurationProperties(prefix = "app.sprite.entity")
public class EntitySpriteConfiguration {
   private EntitySpriteDimensionConfiguration dimension;
   private int defaultSpriteColumn;
   private Map<Direction, Integer> spriteDirectionRows;

   public static class EntitySpriteDimensionConfiguration {
      private final Vector2i vector = new Vector2i();

      public Vector2ic asVector() {
         return vector;
      }

      public void setRows(int rows) {
         this.vector.y = rows;
      }

      public void setCols(int cols) {
         this.vector.x = cols;
      }
   }
}
