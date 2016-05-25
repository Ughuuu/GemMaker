package com.example.app;

import com.badlogic.gdx.math.Vector2;
import com.ngeen.component.ComponentPoint;
import com.ngeen.component.Script;

public class ExampleScript extends Script {
    Vector2 center = new Vector2();
    ComponentPoint point;
    float spd;

    @Override
    public void onInit() {
        point = holder.getComponent(ComponentPoint.class);
        // String ser = holder.saveComponent(ComponentUIBase.class);
        // String ser2 = ng.EntityBuilder.saveEntity(holder);
        // holder.remove();
        // System.out.println(holder.getName());
    }

    @Override
    public void onUpdate(float delta) {
        // holder.remove();
        // point.getPosition().add(1,0,0);
        // point.setPosition(point.getPosition());
    }
}
