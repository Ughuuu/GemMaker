package com.ngeen.components;

import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.ngeen.engine.Constant;

public class DrawbleComponent {
	
	private String shader_id;

	public DrawbleComponent(String shader) {
		this.shader_id = shader;
	}

	public ShaderProgram getShader() {
		return Constant.MANAGER.get(shader_id, ShaderProgram.class);
	}
	
	public void setShader(int shader_id){
		
	}
}
