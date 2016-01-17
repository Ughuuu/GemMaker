package com.ngeen.component;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.ngeen.component.ComponentPoint;
import com.ngeen.component.Script;
import com.ngeen.debug.Debugger;
import com.ngeen.engine.EngineInfo;
import com.ngeen.entity.Entity;

public class Test2 extends Script {
	ComponentPoint point;
	Vector2 center = new Vector2();
	float spd;

	@Override
	public void onInit() {
	}

	@Override
	public void onUpdate(float delta) {
		point = holder.getComponent(ComponentPoint.class);
		Entity parent = holder.getParent();
		if (parent != null) {
			Vector3 cnt = parent.getComponent(ComponentPoint.class).getPosition();
			center = new Vector2(cnt.x, cnt.y);
		}

		Vector3 pct = point.getPosition();
		Vector2 v2 = new Vector2(pct.x, pct.y).sub(center);
		float angle = v2.angle();
		v2.setAngle(angle + 50 / v2.len());
		v2.add(center);
		pct = new Vector3(v2.x, v2.y, 0);
		point.setPosition(pct);
		point.setScale(100/v2.len());
		//Debugger.log(ng.EntityBuilder.getByName("~CAMERA").getComponents(ComponentCamera.class));
	}
}
