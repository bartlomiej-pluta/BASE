package com.bartlomiejpluta.base.engine.world.light;

import com.bartlomiejpluta.base.api.camera.Camera;
import com.bartlomiejpluta.base.api.light.Light;
import com.bartlomiejpluta.base.api.screen.Screen;
import com.bartlomiejpluta.base.engine.core.gl.shader.constant.CounterName;
import com.bartlomiejpluta.base.engine.core.gl.shader.constant.UniformName;
import com.bartlomiejpluta.base.engine.world.location.LocationableModel;
import com.bartlomiejpluta.base.internal.render.ShaderManager;
import lombok.Getter;
import lombok.Setter;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector3fc;

public class DefaultLight extends LocationableModel implements Light {
   private final Vector3f intensity = new Vector3f(1f, 1f, 1f);

   @Getter
   @Setter
   private boolean luminescent;

   @Getter
   @Setter
   private float constantAttenuation = 1f;

   @Getter
   @Setter
   private float linearAttenuation = 0f;

   @Getter
   @Setter
   private float quadraticAttenuation = 1f;

   @Override
   public Vector3fc getIntensity() {
      return intensity;
   }

   @Override
   public void setIntensity(float red, float green, float blue) {
      this.intensity.x = red;
      this.intensity.y = green;
      this.intensity.z = blue;
   }

   @Override
   public void update(float dt) {
      // noop
   }

   @Override
   public void render(Screen screen, Camera camera, ShaderManager shaderManager) {
      var lightNumber = shaderManager.nextNumber(CounterName.LIGHT);
      shaderManager.setUniform(UniformName.UNI_LIGHTS + "[" + lightNumber + "].position", position);
      shaderManager.setUniform(UniformName.UNI_LIGHTS + "[" + lightNumber + "].intensity", intensity);
      shaderManager.setUniform(UniformName.UNI_LIGHTS + "[" + lightNumber + "].constantAttenuation", constantAttenuation);
      shaderManager.setUniform(UniformName.UNI_LIGHTS + "[" + lightNumber + "].linearAttenuation", linearAttenuation);
      shaderManager.setUniform(UniformName.UNI_LIGHTS + "[" + lightNumber + "].quadraticAttenuation", quadraticAttenuation);
   }
}
