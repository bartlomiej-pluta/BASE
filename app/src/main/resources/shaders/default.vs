#version 330

uniform mat4 modelMatrix;

layout(location=0) in vec3 position;

void main()
{
   gl_Position = modelMatrix * vec4(position, 1.0);
}