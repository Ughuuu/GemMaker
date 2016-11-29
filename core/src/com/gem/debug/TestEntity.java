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
		ng.entityBuilder.makeEntity("e1");
		ng.entityBuilder.makeEntity("e2");
		ng.entityBuilder.makeEntity("e3");
		ng.entityBuilder.makeEntity("e4");
		ng.entityBuilder.makeEntity("e5");

		assert (ng.entityBuilder.getByName("e1") != null);
		assert (ng.entityBuilder.getByName("e2") != null);
		assert (ng.entityBuilder.getByName("e3") != null);
		assert (ng.entityBuilder.getByName("e4") != null);
		assert (ng.entityBuilder.getByName("e5") != null);

		ng.entityBuilder.getByName("e1").remove();
		ng.entityBuilder.getByName("e2").remove();
		ng.entityBuilder.getByName("e3").remove();
		ng.entityBuilder.getByName("e4").remove();

		assert (ng.entityBuilder.getByName("e1") == null);
		assert (ng.entityBuilder.getByName("e2") == null);
		assert (ng.entityBuilder.getByName("e3") == null);
		assert (ng.entityBuilder.getByName("e4") == null);

		ng.entityBuilder.makeEntity("e1");
		ng.entityBuilder.makeEntity("e2");
		ng.entityBuilder.makeEntity("e3");
		ng.entityBuilder.makeEntity("e4");
		ng.entityBuilder.makeEntity("e5");

		assert (ng.entityBuilder.getByName("e1") != null);
		assert (ng.entityBuilder.getByName("e2") != null);
		assert (ng.entityBuilder.getByName("e3") != null);
		assert (ng.entityBuilder.getByName("e4") != null);
		assert (ng.entityBuilder.getByName("e5") != null);

		ng.entityBuilder.getByName("e1").remove();
		ng.entityBuilder.getByName("e2").remove();
		ng.entityBuilder.getByName("e3").remove();
		ng.entityBuilder.getByName("e4").remove();
		ng.entityBuilder.getByName("e5").remove();
	}

	private void StressTest(Gem ng) {
		for (int i = 0; i < 2000; i++) {
			ng.entityBuilder.makeEntity("test" + i).addComponent(ComponentPoint.class);
		}
		for (int i = 0; i < 2000; i++) {
			ng.entityBuilder.getByName("test" + i).remove();
		}
	}
}
