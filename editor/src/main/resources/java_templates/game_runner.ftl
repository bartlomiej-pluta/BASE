package ${package};

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bartlomiejpluta.base.api.game.context.Context;
import com.bartlomiejpluta.base.api.game.input.Input;
import com.bartlomiejpluta.base.api.game.screen.Screen;
import com.bartlomiejpluta.base.api.game.runner.GameRunner;

public class ${className} implements GameRunner {
   private static final Logger log = LoggerFactory.getLogger(${className}.class);

   @Override
   public void init(Context context) {
      // Resume engine, because it is initially paused
      context.resume();

      log.info("The game runner is not implemented yet...");
      throw new RuntimeException("Not implemented yet");
   }

   @Override
   public void input(Screen screen) {

   }

   @Override
   public void update(float dt) {

   }

   @Override
   public void dispose() {
      // Do something after game loop is end
   }
}