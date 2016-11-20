package com.gem.systems;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Matrix4;
import com.gem.component.ComponentCamera;
import com.gem.component.ComponentMaterial;
import com.gem.component.ComponentMesh;
import com.gem.component.ComponentPoint;
import com.gem.debug.Debugger;
import com.gem.engine.Gem;
import com.gem.entity.Entity;

/**
 * @author Dragos
 * @hidden
 */
public class SystemDraw extends SystemBase {
	Matrix4 cam;

	public SystemDraw(Gem ng, SystemConfiguration conf) {
		super(ng, conf);
	}

	@Override
	public void onBeforeUpdate() {
		Entity ent = gem.EntityBuilder.getByName("~CAMERA");
		ComponentCamera comp = ent.getComponent(ComponentCamera.class);
		Camera camera = comp.Camera;
		cam = camera.combined;
	}

	@Override
	public void onUpdate(Entity ent) {
		Debugger.log(config);
		Matrix4 model = getModel(ent);
		Mesh mesh = ent.getComponent(ComponentMesh.class)._Vertices;
		ShaderProgram prog = ent.getComponent(ComponentMaterial.class).getShader();
		prog.setUniformMatrix("u_projView", cam);
		prog.setUniformMatrix("u_Model", model);

		// ent.addComponent(ComponentVariable.class).setData("u_sampler0",
		// tex.getData().glTarget);

		prog.setUniformi("u_sampler0", 0);
		mesh.bind(prog);
		mesh.render(prog, GL20.GL_TRIANGLES);
	}

	private Matrix4 getModel(Entity ent) {
		Matrix4 pos = ent.getComponent(ComponentPoint.class).getMatrix();
		return pos;
	}
}