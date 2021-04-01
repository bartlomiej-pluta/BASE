package com.bartlomiejpluta.base.engine.world.entity.config;

import com.bartlomiejpluta.base.api.move.Direction;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Data
@Configuration
@ConfigurationProperties(prefix = "app.sprite.entity")
public class EntitySpriteConfiguration {
   private int defaultSpriteColumn;
   private Map<Direction, Integer> spriteDirectionRows;
}
