package com.ngeen.component;

import java.util.Map;

import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.utils.XmlReader.Element;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlWriter;
import com.ngeen.asset.Asset;
import com.ngeen.asset.Pair;
import com.ngeen.asset.ShaderData;
import com.ngeen.engine.Ngeen;

public class ComponentMaterial extends ComponentBase {
	private Asset<ShaderProgram> _Shader;
	private String _ShaderName;

	public ComponentMaterial(Ngeen ng) {
		super(ng);
	}
	
	public ShaderProgram getShader() {
		if(_Shader==null){
			return null;
		}
		return _Shader.getData();
	}

	public void setShader(Asset<ShaderProgram> shader) {
		_ShaderName = shader.getFolder() + shader.getPath();
		_Shader = shader;
	}
	
	public void setShader(String shaderName) {
		_ShaderName = shaderName;
		_Shader = _Ng.Loader.getAsset(shaderName);
	}

	@Override
	protected void Save(XmlWriter element) throws Exception {
		element.element("Component")
		.attribute("_Type", "ComponentMaterial")
		.attribute("_ShaderName", _ShaderName)
		       .pop();
	}

	@Override
	protected void Load(Element element) throws Exception {
		_ShaderName = element.get("_ShaderName");
		setShader(_ShaderName);
	}	
}
