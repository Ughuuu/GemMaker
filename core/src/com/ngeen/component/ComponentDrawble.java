package com.ngeen.component;

import java.util.Map;

import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.ngeen.engine.Ngeen;
import com.ngeen.misc.Pair;
import com.ngeen.misc.ShaderData;

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
