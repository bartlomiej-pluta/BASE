package com.bartlomiejpluta.base.engine.world.entity.config;

import com.bartlomiejpluta.base.api.game.entity.Direction;
import lombok.Data;
import org.joml.Vector2i;
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

   @Data
   public static class EntitySpriteDimensionConfiguration {
      private int rows;
      private int cols;

      public Vector2i asVector() {
         return new Vector2i(rows, cols);
      }
   }
}
