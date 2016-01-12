package com.ngeen.asset;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g3d.Attribute;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.ngeen.debug.Debugger;
import com.ngeen.engine.Ngeen;

public class MeshFactory {
	private final Ngeen _Ng;
	public MeshFactory(Ngeen ng){
		_Ng = ng;
	}	

	private int getUsage(String name){
		if(name.indexOf(ShaderProgram.BINORMAL_ATTRIBUTE)!=-1){
			return Usage.BiNormal;
		}
		if(name.indexOf(ShaderProgram.COLOR_ATTRIBUTE)!=-1){
			return Usage.ColorPacked;
		}
		if(name.indexOf(ShaderProgram.NORMAL_ATTRIBUTE)!=-1){
			return Usage.Normal;
		}
		if(name.indexOf(ShaderProgram.POSITION_ATTRIBUTE)!=-1){
			return Usage.Position;
		}
		if(name.indexOf(ShaderProgram.TANGENT_ATTRIBUTE)!=-1){
			return Usage.Tangent;
		}
		if(name.indexOf(ShaderProgram.TEXCOORD_ATTRIBUTE)!=-1){
			return Usage.TextureCoordinates;
		}
		Debugger.log("Unknown attribute, " + name);
		return -1;
	}
	
	private int getComponents(int type){
		if(type == GL20.GL_FLOAT)
			return 1;
		if(type == GL20.GL_FLOAT_VEC2)
			return 2;
		if(type == GL20.GL_FLOAT_VEC3)
			return 3;
		if(type == GL20.GL_FLOAT_VEC4)
			return 4;
		if(type == GL20.GL_INT)
			return 1;
		if(type == GL20.GL_INT_VEC2)
			return 2;
		if(type == GL20.GL_INT_VEC3)
			return 3;
		if(type == GL20.GL_INT_VEC4)
			return 4;
		Debugger.log("Undefinded type, " + type);
		return 1;
	}
	
	public Mesh makeMesh(ShaderProgram prog, int verts, int indices){
		if(prog == null)
			return null;
		String[] atr = prog.getAttributes();
		Debugger.log(atr.length);
		Set<VertexAttribute> attributes = new TreeSet<VertexAttribute>(new Comparator<VertexAttribute>() {
			@Override
			public int compare(VertexAttribute o1, VertexAttribute o2) {
				return Integer.signum(getUsage(o1.alias) - getUsage(o2.alias));
			}
		});
		for(int i=0;i<atr.length;i++){
			String name = atr[i];
			int loc = prog.getAttributeLocation(name);
			int type = prog.getAttributeType(name);
			int size = prog.getAttributeSize(name);
			VertexAttribute attr = new VertexAttribute(getUsage(name), getComponents(type), name);
			attributes.add(attr);
		}
		return new Mesh(true, verts, indices, attributes.toArray(new VertexAttribute[0]));
	}
}
