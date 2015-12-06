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
