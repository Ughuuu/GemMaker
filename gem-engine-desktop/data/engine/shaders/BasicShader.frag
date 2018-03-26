uniform sampler2D u_sampler0;

varying vec2 vTexCoord;
varying vec4 vColor;

void main() {
    vec4 texColor = texture2D(u_sampler0, vTexCoord);

    gl_FragColor = vColor * texColor;
}