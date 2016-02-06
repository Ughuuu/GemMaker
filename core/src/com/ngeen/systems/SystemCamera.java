package com.ngeen.systems;

import com.ngeen.component.ComponentCamera;
import com.ngeen.component.ComponentPoint;
import com.ngeen.engine.Ngeen;
import com.ngeen.entity.Entity;

/**
 * @hidden
 * @author Dragos
 *
 */
public class SystemCamera extends SystemBase {

	public SystemCamera(Ngeen ng, SystemConfiguration conf) {
		super(ng, conf);
	}

	@Override
	public void onUpdate(Entity ent) {
		ComponentCamera cam = ent.getComponent(ComponentCamera.class);
		ComponentPoint pos = ent.getComponent(ComponentPoint.class);

		cam.Camera.position.set(pos.getPosition());
		cam.Camera.view.set(pos.getMatrix());
		cam.Camera.update();
	}

}
