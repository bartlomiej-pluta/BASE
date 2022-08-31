package ${package};

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bartlomiejpluta.base.api.context.Context;
import com.bartlomiejpluta.base.api.input.Input;
import com.bartlomiejpluta.base.api.screen.Screen;
import com.bartlomiejpluta.base.api.runner.GameRunner;

public class ${className} implements GameRunner {
   private static final Logger log = LoggerFactory.getLogger(${className}.class);
   private static ${className} INSTANCE;

   @Override
   public void init(Context context) {
      ${className}.INSTANCE = this;

      // Resume engine, because it is initially paused
      context.resume();

      log.info("The game runner is not implemented yet...");
      throw new RuntimeException("Not implemented yet");
   }

   @Override
   public void input(Input input) {

   }

   @Override
   public void update(float dt) {

   }

   @Override
   public void dispose() {
      // Do something after game loop is end
   }

   public static ${className} instance() {
      return INSTANCE;
   }
}