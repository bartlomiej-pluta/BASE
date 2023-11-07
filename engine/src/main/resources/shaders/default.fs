#version 330

struct Light
{
   vec2 position;
   vec3 intensity;
   float constantAttenuation;
   float linearAttenuation;
   float quadraticAttenuation;
};

uniform vec4 objectColor;
uniform int hasTexture;
uniform sampler2D sampler;
uniform vec2 spriteSize;
uniform vec2 spritePosition;
uniform vec3 ambient;
uniform Light lights[100];
uniform int activeLights;

in vec2 objectPosition;
in vec2 fragmentTexCoord;

out vec4 fragColor;

void main()
{
   vec4 color = hasTexture == 1 ? objectColor * texture(sampler, fragmentTexCoord * spriteSize + spritePosition) : objectColor;
   vec4 total = vec4(color.rgb * ambient, color.a);

   for(int i=0; i<activeLights; ++i)
   {
      Light light = lights[i];
      float dist = distance(light.position, objectPosition);
      total.rgb += color.rgb * light.intensity.rgb / (light.constantAttenuation + light.linearAttenuation * dist + light.quadraticAttenuation * dist * dist);
   }

   fragColor = total;
}