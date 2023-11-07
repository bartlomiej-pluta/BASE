#version 330

uniform mat4 viewModelMatrix;
uniform mat4 modelMatrix;
uniform mat4 projectionMatrix;

layout(location=0) in vec2 position;
layout(location=1) in vec2 texCoord;

out vec2 objectPosition;
out vec2 fragmentTexCoord;

void main()
{
   gl_Position = projectionMatrix * viewModelMatrix * vec4(position, 0.0, 1.0);
   objectPosition = (modelMatrix * vec4(position, 0.0, 1.0)).xy;
   fragmentTexCoord = texCoord;
}