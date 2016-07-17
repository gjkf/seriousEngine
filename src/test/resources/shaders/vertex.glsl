#version 400 core

in vec4 position;
in vec4 color;

uniform mat4 model;
uniform mat4 view;
uniform mat4 projection;

smooth out vec4 theColor;

void main() {
    //mat4 mvp = projection * view * model;
    //gl_Position = mvp * vec4(position, 1.0);
    theColor = color;
    gl_Position = position;
}