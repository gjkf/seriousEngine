#version 400 core

uniform vec2 uResolution;

out vec4 outputColor;

void main(){
    vec2 position = gl_FragCoord.xy/uResolution.xy;
    float gradient = position.x;
    gl_FragColor = vec4(0., gradient, 0., 1.);
}