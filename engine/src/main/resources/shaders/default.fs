#version 330

uniform vec4 objectColor;
uniform int hasTexture;
uniform sampler2D sampler;
uniform vec2 spriteSize;
uniform vec2 spritePosition;

in vec2 fragmentTexCoord;

out vec4 fragColor;

void main()
{
   if(hasTexture == 1)
   {
      fragColor = objectColor * texture(sampler, fragmentTexCoord * spriteSize + spritePosition);
   }
   else
   {
      fragColor = objectColor;
   }
}