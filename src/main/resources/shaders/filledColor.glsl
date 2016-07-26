#version 400 core

in vec4 color;

out vec4 outColor;

void main() {
    gl_FragColor = vec4(color);
}
