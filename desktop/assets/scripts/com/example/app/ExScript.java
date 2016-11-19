package com.example.app;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.ngeen.component.ComponentPoint;
import com.ngeen.component.Script;

public class ExScript extends Script {
    ComponentPoint point;
    Vector2 center = new Vector2();
    float spd;

    @Override
    public void onInit() {
        point = holder.getComponent(ComponentPoint.class);
        Vector3 pct = point.getPosition();
    }

    @Override
    public void onUpdate(float delta) {
	Vector3 p = point.getPosition();
	/p.x--;
	point.setPosition(p);
    }
}

