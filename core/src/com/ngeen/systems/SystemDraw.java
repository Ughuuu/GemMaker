package com.ngeen.systems;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Matrix4;
import com.ngeen.component.*;
import com.ngeen.debug.Debugger;
import com.ngeen.engine.Ngeen;
import com.ngeen.entity.Entity;

/**
 * @hidden
 * @author Dragos
 *
 */
public class SystemDraw extends SystemBase {
	Matrix4 cam;
	
	public SystemDraw(Ngeen ng, SystemConfiguration conf) {
		super(ng, conf);
	}

	@Override
	public void onBeforeUpdate(){
		Entity ent = _Ng.EntityBuilder.getByName("~CAMERA");
		ComponentCamera comp = ent.getComponent(ComponentCamera.class);
		Camera camera = comp.Camera;
		 cam = camera.combined;
	}
	
	private Matrix4 getModel(Entity ent) {
		Matrix4 pos = ent.getComponent(ComponentPoint.class).getMatrix();
		return pos;
	}

	@Override
	public void onUpdate(Entity ent) {
		Debugger.log(_Config);
		Matrix4 model = getModel(ent);
		Mesh mesh = ent.getComponent(ComponentMesh.class)._Vertices;
		ShaderProgram prog = ent.getComponent(ComponentMaterial.class).getShader();
		prog.setUniformMatrix("u_projView", cam);
		prog.setUniformMatrix("u_Model", model);

		
		//ent.addComponent(ComponentVariable.class).setData("u_sampler0", tex.getData().glTarget);
		
		prog.setUniformi("u_sampler0", 0);
		mesh.bind(prog);
		mesh.render(prog, GL20.GL_TRIANGLES);
	}
}