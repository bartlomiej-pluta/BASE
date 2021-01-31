#version 330

uniform mat4 modelMatrix;
uniform mat4 viewMatrix;
uniform mat4 projectionMatrix;

layout(location=0) in vec2 position;
layout(location=1) in vec2 texCoord;

out vec2 fragmentTexCoord;

void main()
{
   gl_Position = projectionMatrix * viewMatrix * modelMatrix * vec4(position, 0.0, 1.0);
   fragmentTexCoord = texCoord;
}