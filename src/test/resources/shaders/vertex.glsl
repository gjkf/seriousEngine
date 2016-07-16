#version 150 core

// Given the position and  the color...

in vec3 position;
in vec3 color;

// ... find out then what color should be used

out vec3 vertexColor;

uniform mat4 model;
uniform mat4 view;
uniform mat4 projection;

void main() {
    vertexColor = color;
    mat4 mvp = projection * view * model;
    gl_Position = mvp * vec4(position, 1.0);
}