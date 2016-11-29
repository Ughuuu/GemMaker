package com.gem.component;

import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.utils.XmlReader.Element;
import com.badlogic.gdx.utils.XmlWriter;
import com.gem.asset.Asset;
import com.gem.engine.Gem;
import com.gem.entity.ComponentSpokesman;
import com.gem.entity.Entity;

public class ComponentMaterial extends ComponentBase {
	private Asset<ShaderProgram> shaderAsset;
	private String shaderName;

	public ComponentMaterial(Gem ng, Entity ent, ComponentFactory factory, ComponentSpokesman _ComponentSpokesman) {
		super(ng, ent, factory, _ComponentSpokesman);
	}

	public ShaderProgram getShader() {
		if (shaderAsset == null) {
			return null;
		}
		return shaderAsset.getAsset();
	}

	public ComponentMaterial setShader(Asset<ShaderProgram> shader) {
		shaderName = shader.getFolder() + shader.getPath();
		shaderAsset = shader;
		return this;
	}

	public ComponentMaterial setShader(String shaderName) {
		this.shaderName = shaderName;
		shaderAsset = gem.loader.getAsset(shaderName);
		return this;
	}

	@Override
	protected ComponentBase Load(Element element) throws Exception {
		shaderName = element.getChildByName("Shader").get("Name");
		setShader(shaderName);
		return this;
	}

	@Override
	protected void Save(XmlWriter element) throws Exception {
		element.element("Component").attribute("Type", Type.getName()).element("Shader").attribute("Name", shaderName)
				.pop().pop();
	}

	@Override
	protected void visitComponent(ComponentBase component, ComponentFactory factory) {
		component.notifyWithComponent(this);
	}
}
