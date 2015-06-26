package com.ngeen.helper;

import java.util.HashMap;
import java.util.Map;

import com.artemis.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Vector3;
import com.ngeen.components.CameraComponent;
import com.ngeen.holder.Constant;

public class RenderHelper {
	public SpriteBatch batch;

	public RenderHelper() {
		batch = new SpriteBatch();
	}	

	void createCamera(){
		
	}
	
	void setCamera(Camera camera){
		Constant.CAMERA = camera;
		batch.setProjectionMatrix(camera.combined);
	}
}
