package com.gem.component;

import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.utils.XmlReader.Element;
import com.badlogic.gdx.utils.XmlWriter;
import com.gem.debug.Debugger;
import com.gem.engine.Gem;
import com.gem.entity.ComponentSpokesman;
import com.gem.entity.Entity;

public class ComponentMesh extends ComponentBase {
	public Mesh _Vertices;
	BoundingBox Box;

	public ComponentMesh(Gem ng, Entity ent, ComponentFactory factory, ComponentSpokesman _ComponentSpokesman) {
		super(ng, ent, factory, _ComponentSpokesman);
	}

	public Mesh createMesh(int size, int ind) {
		return _Vertices = Ng._MeshBuilder.makeMesh((getOwner().getComponent(ComponentMaterial.class).getShader()),
				size, ind);
	}

	public Mesh getVertices() {
		return _Vertices;
	}

	public ComponentMesh setVertices(Mesh Vertices) {
		_Vertices = Vertices;
		computeBox();
		return this;
	}

	private void computeBox() {
		try {
			_Vertices.calculateBoundingBox(Box);
		} catch (Exception e) {
			Debugger.log(e);
		}
	}

	@Override
	protected ComponentBase Load(Element element) throws Exception {
		// _ShaderName = element.get("_ShaderName");
		// setShader(_ShaderName);
		return this;
	}

	@Override
	protected void Save(XmlWriter element) throws Exception {
		element.element("Component").attribute("Type", Type.getName())
				// element.attribute("_ShaderName", _ShaderName)
				.pop();
	}

	@Override
	protected void visitComponent(ComponentBase component, ComponentFactory factory) {
		component.notifyWithComponent(this);
	}
}
