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
	private SpriteBatch _SpriteBatch;
	
	public SystemSprite(Ngeen ng, SystemConfiguration conf) {
		super(ng, conf);
		_SpriteBatch = new SpriteBatch();
	}

	@Override
	public void onBeforeUpdate(){
		_CameraView = _Ng.EntityBuilder.getByName("~CAMERA").getComponent(ComponentCamera.class).Camera.combined;
		_SpriteBatch.begin();
		//_SpriteBatch.disableBlending();
		 _SpriteBatch.setProjectionMatrix(_CameraView);
	}
	
	private Matrix4 getModel(Entity ent) {
		Matrix4 pos = ent.getComponent(ComponentPoint.class).getMatrix();
		return pos;
	}

	@Override
	public void onUpdate(Entity ent) {
		ComponentSprite sprComp = ent.getComponent(ComponentSprite.class);
		ComponentVariable tex = ent.getComponent(ComponentVariable.class);
		ComponentPoint pos = ent.getComponent(ComponentPoint.class);
		Asset<Texture> text = (Asset<Texture>) tex.getData("texture");
		Sprite spr = sprComp.getSprite();
		
		if(tex.Update){
			tex.Update = false;
			pos.Update = true;
			sprComp.setTexture(text);
			spr = sprComp.getSprite();
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