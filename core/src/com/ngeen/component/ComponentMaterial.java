package com.ngeen.component;

import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.utils.XmlReader.Element;
import com.badlogic.gdx.utils.XmlWriter;
import com.ngeen.asset.Asset;
import com.ngeen.engine.Ngeen;
import com.ngeen.entity.Entity;

public class ComponentMaterial extends ComponentBase {
	private Asset<ShaderProgram> _Shader;
	private String _ShaderName;

	public ComponentMaterial(Ngeen ng, Entity ent) {
		super(ng, ent);
	}

	public ShaderProgram getShader() {
		if (_Shader == null) {
			return null;
		}
		return _Shader.getData();
	}

	@Override
	protected void Load(Element element) throws Exception {
		_ShaderName = element.getChildByName("_ShaderName").get("String");
		setShader(_ShaderName);
	}

	@Override
	protected void Save(XmlWriter element) throws Exception {
		element.element("Component").attribute("_Type", _Type.getName()).element("_ShaderName")
				.attribute("String", _ShaderName).pop().pop();
	}

	public ComponentMaterial setShader(Asset<ShaderProgram> shader) {
		_ShaderName = shader.getFolder() + shader.getPath();
		_Shader = shader;
		return this;
	}

	public ComponentMaterial setShader(String shaderName) {
		_ShaderName = shaderName;
		_Shader = _Ng.Loader.getAsset(shaderName);
		return this;
	}
}
