attribute vec2 a_position;
attribute vec4 a_color;

uniform mat4 u_projView;
uniform mat4 u_Model;

varying vec4 vColor;

void main() {
    gl_Position = u_projView * u_Model * vec4(a_position, 0.0, 1.0);
    vColor = a_color;
}