package com.ngeen.systems;

import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Matrix4;
import com.ngeen.asset.Asset;
import com.ngeen.component.*;
import com.ngeen.debug.Debugger;
import com.ngeen.engine.Ngeen;
import com.ngeen.entity.Entity;

public class SystemSprite extends SystemBase {
	private Matrix4 _CameraView;
	protected SpriteBatch _SpriteBatch;
	
	public SystemSprite(Ngeen ng, SystemConfiguration conf, SpriteBatch batch) {
		super(ng, conf);
		_SpriteBatch = batch;
	}

	@Override
	public void onBeforeUpdate(){
		_CameraView = _Ng.EntityBuilder.getByName("~CAMERA").getComponent(ComponentCamera.class).Camera.combined;
		_SpriteBatch.begin();
		//_SpriteBatch.disableBlending();
		 _SpriteBatch.setProjectionMatrix(_CameraView);
		 _SpriteBatch.setTransformMatrix(new Matrix4());
	}
	
	private Matrix4 getModel(Entity ent) {
		Matrix4 pos = ent.getComponent(ComponentPoint.class).getMatrix();
		return pos;
	}

	@Override
	public void onUpdate(Entity ent) {
		ComponentSprite sprComp = ent.getComponent(ComponentSprite.class);
		ComponentPoint pos = ent.getComponent(ComponentPoint.class);
		Sprite spr = sprComp.getSprite();
		
		if(spr.getTexture()==null){
			Debugger.log(ent.getName() + " doesn't have a right texture.");
			return;
		}
		
		if(pos.Update){
			pos.Update = false;
			spr.setPosition(pos.getPosition().x-spr.getWidth()/2, pos.getPosition().y - spr.getHeight()/2);
			spr.setScale(pos.getScale().x, pos.getScale().y);
			spr.setRotation(pos.getRotation().z);
		}
		spr.draw(_SpriteBatch);
	}
	
	@Override
	public void onAfterUpdate(){
		_SpriteBatch.end();
	}
}