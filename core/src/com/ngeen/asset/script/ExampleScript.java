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
		point = holder.getComponent(ComponentPoint.class);
	}

	public void onUpdate(float delta) {
		float x = (float) (50.0f * Math.sin(System.currentTimeMillis()/50)/3);
		float y = (float) (50 * Math.cos(System.currentTimeMillis()/50)/10);
		point.setPosition(new Vector3(x+50, y+50, 0.0f));
	}
}
