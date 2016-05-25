package com.example.app;

import com.ngeen.action.CommandFactory;
import com.ngeen.component.ComponentCamera;
import com.ngeen.component.ComponentPoint;
import com.ngeen.component.Script;
import com.ngeen.debug.*;

public class TestScript extends Script {

    ComponentCamera camera;
    ComponentPoint point;

    @Override
    public void onInit() {
        point = holder.getComponent(ComponentPoint.class);
    }

    @Override
    public void onUpdate(float delta) {
    }
}
