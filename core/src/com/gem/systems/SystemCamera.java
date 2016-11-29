package com.gem.systems;

import com.badlogic.gdx.Gdx;
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

import lombok.val;

/**
 * @author Dragos
 * @hidden
 */
public class SystemCamera extends SystemBase {
	private boolean resizeGlobal = false, resize = false;
	int resizeWidth, resizeHeight;
	
	public SystemCamera(Gem ng, SystemConfiguration conf) {
		super(ng, conf);
	}

	@Override
	public void onBeforeUpdate() {
		if(resizeGlobal){
			resize = true;
		}
	}

	@Override
	public void onUpdate(Entity ent) {
		val camera = ent.getComponent(ComponentCamera.class);
		val viewport = camera.getViewport();
		if(resize){
			viewport.apply();
		}
	}
	
	@Override
	public void onAfterUpdate(){
		resize = false;
	}

	public void resize(int w, int h) {
		resizeGlobal = true;
		resizeWidth = w;
		resizeHeight = h;
	}
}