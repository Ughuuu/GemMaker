package com.ngeen.component;

import java.util.Map;

import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.ngeen.asset.Pair;
import com.ngeen.asset.ShaderData;
import com.ngeen.engine.Ngeen;

public class ComponentDrawble extends ComponentBase {
	private Map<String, Integer> _Data;
	private int _ShaderId;

	public ComponentDrawble(Ngeen ng) {
		super(ng);
	}

	public ShaderProgram getShader() {
		// return Constant.MANAGER.get(, ShaderProgram.class);
		return null;
	}

	public void setShader(int shader_id) {
		_ShaderId = shader_id;
	}
}
