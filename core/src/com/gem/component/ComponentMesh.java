package com.gem.component;

import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.util.Arrays;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.utils.XmlReader.Element;
import com.badlogic.gdx.utils.XmlWriter;
import com.gem.debug.Debugger;
import com.gem.engine.Gem;
import com.gem.entity.ComponentSpokesman;
import com.gem.entity.Entity;

import lombok.val;

public class ComponentMesh extends ComponentBase {
	private Mesh mesh;
	BoundingBox Box;
	private float[] vert;
	private short[] ind;

	public ComponentMesh(Gem ng, Entity ent, ComponentFactory factory, ComponentSpokesman _ComponentSpokesman) {
		super(ng, ent, factory, _ComponentSpokesman);
	}

	public final Mesh getMesh() {
		return mesh;
	}

	public ComponentMesh setIndices(short[] indices) {
		mesh.setIndices(indices);
		return this;
	}
	
	public ComponentMesh setVertices(float[] verts) {
		mesh.setVertices(verts);
		return this;
	}

	public BoundingBox getBoundingBox() {
		try {
			mesh.calculateBoundingBox(Box);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Box;
	}

	@Override
	protected void notifyWithComponent(ComponentMaterial component) {
		mesh = gem.meshBuilder.makeMesh(component.getShader(), vert == null ? 0 :vert.length, ind == null ? 0 : ind.length);
		if(ind != null){
			setIndices(ind);
		}
		if(vert != null){
			setVertices(vert);
		}
		ind = null;
		vert = null;
	}
	
	@Override
	protected ComponentBase Load(Element element) throws Exception {
		try{
			val indices = element.getChildByName("Indices").getText();
			if(indices != null){
				val indicesArray = indices.split(", ");
			    ind = new short[indicesArray.length];
			    for (int i = 0; i < ind.length; i++) {
			    	ind[i] = Short.parseShort(indicesArray[i]);
			    }
			}
		    
			val vertices = element.getChildByName("Vertices").getText();
			if(vertices != null){
				val verticesArray = vertices.split(", ");
			    vert = new float[verticesArray.length];
			    for (int i = 0; i < vert.length; i++) {
			    	vert[i] = Float.parseFloat(verticesArray[i]);
			    }
			}
		}catch(Exception e){
			vert = new float[]{0,0,Color.WHITE.toFloatBits(),
					0,1, Color.WHITE.toFloatBits(),
					1,0,Color.RED.toFloatBits()};
		}
		return this;
	}

	@Override
	protected void Save(XmlWriter element) throws Exception {
		ShortBuffer indices = mesh.getIndicesBuffer();
		FloatBuffer vertices = mesh.getVerticesBuffer();
		ind = new short[mesh.getMaxIndices()];
		vert = new float[mesh.getMaxVertices()];
		
		while(indices.hasRemaining()){			
			ind[indices.position()] = indices.get();
		}
		while(vertices.hasRemaining()){			
			vert[vertices.position()] = vertices.get();
		}
		
		element.element("Component").attribute("Type", Type.getName())
				.element("Indices").text(Arrays.toString(ind).replace("[", "").replace("]", "")).pop()
				.element("Vertices").text(Arrays.toString(vert).replace("[", "").replace("]", "")).pop()
				.pop();
		ind = null;
		vert = null;
	}

	@Override
	protected void visitComponent(ComponentBase component, ComponentFactory factory) {
		component.notifyWithComponent(this);
	}
}
