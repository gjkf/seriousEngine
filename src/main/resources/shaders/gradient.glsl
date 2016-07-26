#version 400 core

uniform vec2 uResolution;
uniform int uDirection;
uniform vec4 uStartingColor;
uniform vec4 uEndingColor;

out vec4 outColor;

void main() {
    vec2 position = gl_FragCoord.xy / uResolution.xy;
    float d;
    if(uDirection == 0){
        d = position.x;
    }else{
        d = position.y;
    }
    vec4 c = mix(uStartingColor, uEndingColor, d);
    gl_FragColor = vec4(c);
}
