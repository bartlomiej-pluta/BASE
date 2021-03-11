package ${package};

import com.bartlomiejpluta.base.api.game.context.Context;
import com.bartlomiejpluta.base.api.game.screen.Screen;
import com.bartlomiejpluta.base.api.game.runner.GameRunner;

public class ${className} implements GameRunner {

   @Override
   public void init(Context context) {
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