#version 400 core

in vec3 position;

// KEEP THESE 3 LINES!!!
uniform mat4 model;
uniform mat4 view;
uniform mat4 projection;

void main() {
    // This just sets the current and normal position of the pixel. I suggest to keep using these 2 lines.
    mat4 mvp = projection * view * model;
    gl_Position = mvp * vec4(position, 1.0);
}