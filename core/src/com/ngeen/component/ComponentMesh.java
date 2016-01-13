package com.ngeen.component;

import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.utils.XmlWriter;
import com.badlogic.gdx.utils.XmlReader.Element;
import com.ngeen.asset.MeshFactory;
import com.ngeen.debug.Debugger;
import com.ngeen.engine.Ngeen;
import com.ngeen.entity.Entity;

public class ComponentMesh extends ComponentBase{
	public Mesh _Vertices;
	BoundingBox Box;
	
	public ComponentMesh(Ngeen ng, Entity ent) {
		super(ng, ent);
	}
	
	public Mesh createMesh(int size, int ind){
		return _Vertices = _Ng._MeshBuilder.makeMesh((getOwner().getComponent(ComponentMaterial.class).getShader()), size, ind);
	}
	
	private void computeBox(){
		try{
		_Vertices.calculateBoundingBox(Box);
		}catch(Exception e){
			Debugger.log(e);
		}
	}
	
	public ComponentMesh setVertices(Mesh Vertices){
		_Vertices = Vertices;
		computeBox();
		return this;
	}
	
	public Mesh getVertices(){
		return _Vertices;
	}
	
	@Override
	protected void Save(XmlWriter element) throws Exception {
		element.element("Component")
		.attribute("_Type", this.getClass().getName())
		//element.attribute("_ShaderName", _ShaderName)
		       .pop();
	}

	@Override
	protected void Load(Element element) throws Exception {
		//_ShaderName = element.get("_ShaderName");
		//setShader(_ShaderName);
	}
}
