package com.ngeen.scene;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.ngeen.asset.Asset;
import com.ngeen.asset.script.ExampleScript;
import com.ngeen.component.ComponentCamera;
import com.ngeen.component.ComponentMaterial;
import com.ngeen.component.ComponentMesh;
import com.ngeen.component.ComponentPoint;
import com.ngeen.component.ComponentScript;
import com.ngeen.component.ComponentSprite;
import com.ngeen.component.ComponentUILayout;
import com.ngeen.component.ComponentUIStage;
import com.ngeen.component.ui.layout.ComponentUITable;
import com.ngeen.component.ui.widget.ComponentUILabel;
import com.ngeen.debug.Debugger;
import com.ngeen.debug.TestAsset;
import com.ngeen.debug.TestComponents;
import com.ngeen.debug.TestEntity;
import com.ngeen.debug.TestSystemConfiguration;
import com.ngeen.engine.Ngeen;
import com.ngeen.entity.Entity;

public class TestScene extends Scene {
	Entity ent;

	void startTestSuite() {
		new TestSystemConfiguration(ng);
		new TestAsset(ng);
		new TestComponents(ng);
		new TestEntity(ng);
	}

	void doStuff() {
		startTestSuite();

		ent = CreateObject("Hello");
		ent.addComponent(ComponentPoint.class).setPosition(new Vector3(-100, 0, 0)).setScale(1);
		ent.addComponent(ComponentSprite.class).setTexture("LoadScene/textures/ok.png");
		ent.addComponent(ComponentScript.class).setScript(ExampleScript.class);

		Entity stage = CreateObject("Stage");
		stage.addComponent(ComponentPoint.class).setPosition(new Vector3(300,0,0));
		stage.addComponent(ComponentUIStage.class);
		
		Entity table = CreateObject("Table");
		table.addComponent(ComponentPoint.class).setPosition(new Vector3(200,0,0));
		table.addComponent(ComponentUITable.class);
		table.setParent("Stage");
		
		Entity widget = CreateObject("Widget");
		widget.addComponent(ComponentPoint.class).setPosition(new Vector3(100,0,0));
		widget.addComponent(ComponentUILabel.class);
		widget.setParent("Table");
	}

	@Override
	public void onInit() {
	}

	@Override
	public void onUpdate(float delta) {
	}
}
