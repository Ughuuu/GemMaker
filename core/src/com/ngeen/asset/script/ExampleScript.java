package com.ngeen.asset.script;

import java.util.concurrent.TimeUnit;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector3;
import com.ngeen.component.ComponentPoint;
import com.ngeen.component.ComponentSprite;
import com.ngeen.component.Script;
import com.ngeen.debug.Debugger;

public class ExampleScript extends Script {
	ComponentPoint point;
	float dir = -1;

	public void onInit() {
		point = holder.getComponent(ComponentPoint.class).setPosition(new Vector3(100,2,5));
	}

	public void onUpdate(float delta) {
		
	}
}
