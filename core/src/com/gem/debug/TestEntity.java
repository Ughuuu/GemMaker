package com.gem.debug;

import com.gem.component.ComponentPoint;
import com.gem.engine.Gem;

public class TestEntity {
	public TestEntity(Gem ng) {
		SimpleTest(ng);
		StressTest(ng);
		Debugger.log("TestEntity()---PASS");
	}

	private void SimpleTest(Gem ng) {
		ng.EntityBuilder.makeEntity("e1");
		ng.EntityBuilder.makeEntity("e2");
		ng.EntityBuilder.makeEntity("e3");
		ng.EntityBuilder.makeEntity("e4");
		ng.EntityBuilder.makeEntity("e5");

		assert (ng.EntityBuilder.getByName("e1") != null);
		assert (ng.EntityBuilder.getByName("e2") != null);
		assert (ng.EntityBuilder.getByName("e3") != null);
		assert (ng.EntityBuilder.getByName("e4") != null);
		assert (ng.EntityBuilder.getByName("e5") != null);

		ng.EntityBuilder.getByName("e1").remove();
		ng.EntityBuilder.getByName("e2").remove();
		ng.EntityBuilder.getByName("e3").remove();
		ng.EntityBuilder.getByName("e4").remove();

		assert (ng.EntityBuilder.getByName("e1") == null);
		assert (ng.EntityBuilder.getByName("e2") == null);
		assert (ng.EntityBuilder.getByName("e3") == null);
		assert (ng.EntityBuilder.getByName("e4") == null);

		ng.EntityBuilder.makeEntity("e1");
		ng.EntityBuilder.makeEntity("e2");
		ng.EntityBuilder.makeEntity("e3");
		ng.EntityBuilder.makeEntity("e4");
		ng.EntityBuilder.makeEntity("e5");

		assert (ng.EntityBuilder.getByName("e1") != null);
		assert (ng.EntityBuilder.getByName("e2") != null);
		assert (ng.EntityBuilder.getByName("e3") != null);
		assert (ng.EntityBuilder.getByName("e4") != null);
		assert (ng.EntityBuilder.getByName("e5") != null);

		ng.EntityBuilder.getByName("e1").remove();
		ng.EntityBuilder.getByName("e2").remove();
		ng.EntityBuilder.getByName("e3").remove();
		ng.EntityBuilder.getByName("e4").remove();
		ng.EntityBuilder.getByName("e5").remove();
	}

	private void StressTest(Gem ng) {
		for (int i = 0; i < 2000; i++) {
			ng.EntityBuilder.makeEntity("test" + i).addComponent(ComponentPoint.class);
		}
		for (int i = 0; i < 2000; i++) {
			ng.EntityBuilder.getByName("test" + i).remove();
		}
	}
}
