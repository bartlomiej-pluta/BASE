package com.bartlomiejpluta.base.engine.world.character.config;

import com.bartlomiejpluta.base.api.move.Direction;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Data
@Configuration
@ConfigurationProperties(prefix = "app.sprite.character")
public class CharacterSpriteConfiguration {
   private int defaultSpriteColumn;
   private Map<Direction, Integer> spriteDirectionRows;
}
