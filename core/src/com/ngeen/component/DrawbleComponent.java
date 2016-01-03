package com.ngeen.component;

import java.util.Map;

import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.ngeen.asset.AssetVariable;
import com.ngeen.engine.Ngeen;
import com.ngeen.misc.Pair;
import com.ngeen.misc.ShaderData;

public class DrawbleComponent {
	private Map<String, Integer> _Data;
	private int _ShaderId;

	public DrawbleComponent(int shader) {
		this._ShaderId = shader;
	}

	public ShaderProgram getShader() {
		//return Constant.MANAGER.get(, ShaderProgram.class);
		return null;
	}
	
	public void setShader(int shader_id){
		
	}
}
