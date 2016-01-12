package com.ngeen.scene;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.ngeen.asset.Asset;
import com.ngeen.component.ComponentCamera;
import com.ngeen.component.ComponentMaterial;
import com.ngeen.component.ComponentMesh;
import com.ngeen.component.ComponentPoint;
import com.ngeen.component.ComponentSprite;
import com.ngeen.component.ComponentVariable;
import com.ngeen.debug.Debugger;
import com.ngeen.debug.TestAsset;
import com.ngeen.debug.TestComponents;
import com.ngeen.debug.TestEntity;
import com.ngeen.debug.TestSystemConfiguration;
import com.ngeen.engine.Ngeen;
import com.ngeen.entity.Entity;

public class TestScene extends Scene{
	Entity ent;
	
	void startTestSuite(){
		new TestSystemConfiguration(ng);
		new TestAsset(ng);
		new TestComponents(ng);
		new TestEntity(ng);
	}
	
	void doStuff(){
		startTestSuite();		
		
		ent = CreateObject("Hello");
		ent.addComponent(ComponentPoint.class).setPosition(new Vector3(10,0,0)).setScale(1);
		Asset<Texture> tex = ng.Loader.getAsset("LoadScene/textures/icon.png");
		
		ent.addComponent(ComponentVariable.class).setData("texture", tex);
		
		ent.addComponent(ComponentSprite.class);		
		
		Entity ent2 = CreateObject("Point");
		ent2.addComponent(ComponentPoint.class).setPosition(new Vector3(-100,0,0));
		findObject("~CAMERA").getComponent(ComponentCamera.class).Camera.translate(new Vector3(100,0,0));
		//findObject("~CAMERA").getComponent(ComponentCamera.class).Camera.rotate(10, 0, 0, 1);
		findObject("~CAMERA").getComponent(ComponentCamera.class).Camera.update();
		ng.XmlSave.Save();
		//ng.XmlSave.Load();
	}
	
	@Override
	public void onInit(){
		//doStuff();
		//Gdx.app.exit();
	}
	
	@Override
	public void onUpdate(float delta){
		//findObject("~CAMERA").getComponent(ComponentCamera.class).Camera.rotate(0.1f, 0, 0, 1);
		//findObject("~CAMERA").getComponent(ComponentCamera.class).Camera.update();
	}
}
