#version 330

uniform vec4 objectColor;

out vec4 fragColor;

void main()
{
   fragColor = objectColor;
}