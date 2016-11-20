package com.gem.debug;

import com.gem.component.ComponentCamera;
import com.gem.component.ComponentPoint;
import com.gem.engine.Gem;
import com.gem.entity.Entity;

public class TestComponents {
	public TestComponents(Gem ng) {
		SimpleTest(ng);
		Debugger.log("TestComponents()---PASS");
	}

	private void SimpleTest(Gem ng) {
		Entity ent = ng.EntityBuilder.makeEntity("dragos");
		assert (ent != null);
		assert (ng.EntityBuilder.getByName("dragos") != null);
		Entity ent2 = ng.EntityBuilder.getByName("dragos");
		assert (ent == ent2);
		assert (ent.getId() == ent2.getId());
		ent.addComponent(ComponentCamera.class);
		assert (ent.getComponent(ComponentCamera.class) != null);
		ent.addComponent(ComponentPoint.class);
		assert (ent.getComponent(ComponentPoint.class) != null);
		ent.addComponent(ComponentPoint.class);
		assert (ent.getComponent(ComponentPoint.class) != null);
		assert (ent.getComponents(ComponentPoint.class).size() == 1);

		ent.removeComponent(ComponentPoint.class);
		assert (ent.getComponents(ComponentPoint.class).size() == 0);
		assert (ent.getComponent(ComponentPoint.class) == null);

		ent.addComponent(ComponentPoint.class);

		assert (ent.getComponents(ComponentPoint.class).size() == 1);
		assert (ent.getComponent(ComponentPoint.class) != null);

		ent.remove();

		assert (ng.EntityBuilder.getByName("dragos") == null);
	}
}
