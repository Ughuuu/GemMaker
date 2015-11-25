package com.ngeen.components;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.ngeen.engine.Constant;
import com.ngeen.engine.Ngeen;
import com.ngeen.misc.Pair;
import com.ngeen.misc.ShaderData;

public class DrawbleComponent {

	private Map<Pair<String, Integer>, ShaderData> uniforms = new HashMap<Pair<String, Integer>, ShaderData>();

	private String shader_id;
	private static Ngeen ng;

	public DrawbleComponent(String shader) {
		this.shader_id = shader;
	}

	public ShaderProgram getShader() {
		return Constant.MANAGER.get(shader_id, ShaderProgram.class);
	}

	public void bind() {
		int i=0;
		ShaderProgram prog = getShader();
		prog.begin();
		for (Map.Entry<Pair<String, Integer>, ShaderData> entry : uniforms.entrySet()) {
			int type = entry.getKey().y;
			String name = entry.getKey().x;
			ShaderData data = entry.getValue();
			switch(type){
			case GL20.GL_FLOAT:
				prog.setUniformf(name, data.getData()[0]);
				break;
			case GL20.GL_FLOAT_VEC2:
				prog.setUniformf(name, data.getData()[0], data.getData()[1]);
				break;
			case GL20.GL_FLOAT_VEC3:
				prog.setUniformf(name, data.getData()[0], data.getData()[1], data.getData()[2]);
				break;
			case GL20.GL_FLOAT_VEC4:
				prog.setUniformf(name, data.getData()[0], data.getData()[1], data.getData()[2], data.getData()[3]);
				break;
			}
			ShaderData tex = AssetFactory.get(entry.getValue(), TextureAsset.class);
			shader.
			tex.texture.bind(i++);
		}
	}

	public void setUniforms() {
		ShaderAsset shader = getShader();
		int i=0;
		//textures
		for (Map.Entry<String, Integer> entry : sampler.entrySet()) {
			TextureAsset tex = AssetFactory.get(entry.getValue(), TextureAsset.class);
			tex.texture.bind(i);
			shader.prog.setUniformi(entry.getValue(), i++);
		}
		//float vectors
		for (Entry<String, Float> entry : uniform1f.entrySet()) {
			shader.prog.setUniformf(entry.getKey(), entry.getValue());
		}
		for (Entry<String, Float[]> entry : uniform2f.entrySet()) {
			shader.prog.setUniform2fv(entry.getKey(), entry.getValue());
		}
		for (int i = 0; i < uniform2f.size(); i++) {
			Pair<String, Float[]> floats = uniform2f.get(i);
			shader.prog.setUniformf(floats.x, floats.y[0], floats.y[1]);
		}
		for (int i = 0; i < uniform3f.size(); i++) {
			Pair<String, Float[]> floats = uniform3f.get(i);
			shader.prog.setUniformf(floats.x, floats.y[0], floats.y[1], floats.y[2]);
		}
		for (int i = 0; i < uniform4f.size(); i++) {
			Pair<String, Float[]> floats = uniform4f.get(i);
			shader.prog.setUniformf(floats.x, floats.y[0], floats.y[1], floats.y[2], floats.y[3]);
		}
		//matrixes
		for (int i = 0; i < uniform3x3f.size(); i++) {
			Pair<String, Float[]> floats = uniform3x3f.get(i);
			shader.prog.setUniform(floats.x, );
		}
	}
}
