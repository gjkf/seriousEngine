#version 400 core

out vec4 outputColor;

void main(){
    // This is the most basic shader possible, it just fills the pixel red
    gl_FragColor = vec4(1, 0, 0, 1);
}