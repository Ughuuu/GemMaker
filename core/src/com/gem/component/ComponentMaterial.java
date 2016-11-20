package com.gem.component;

import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.utils.XmlReader.Element;
import com.badlogic.gdx.utils.XmlWriter;
import com.gem.asset.Asset;
import com.gem.engine.Gem;
import com.gem.entity.ComponentSpokesman;
import com.gem.entity.Entity;

public class ComponentMaterial extends ComponentBase {
	private Asset<ShaderProgram> _Shader;
	private String _ShaderName;

	public ComponentMaterial(Gem ng, Entity ent, ComponentFactory factory, ComponentSpokesman _ComponentSpokesman) {
		super(ng, ent, factory, _ComponentSpokesman);
	}

	public ShaderProgram getShader() {
		if (_Shader == null) {
			return null;
		}
		return _Shader.getAsset();
	}

	public ComponentMaterial setShader(Asset<ShaderProgram> shader) {
		_ShaderName = shader.getFolder() + shader.getPath();
		_Shader = shader;
		return this;
	}

	public ComponentMaterial setShader(String shaderName) {
		_ShaderName = shaderName;
		_Shader = Ng.Loader.getAsset(shaderName);
		return this;
	}

	@Override
	protected ComponentBase Load(Element element) throws Exception {
		_ShaderName = element.getChildByName("Shader").get("Name");
		setShader(_ShaderName);
		return this;
	}

	@Override
	protected void Save(XmlWriter element) throws Exception {
		element.element("Component").attribute("Type", Type.getName()).element("Shader").attribute("Name", _ShaderName)
				.pop().pop();
	}

	@Override
	protected void visitComponent(ComponentBase component, ComponentFactory factory) {
		component.notifyWithComponent(this);
	}
}
