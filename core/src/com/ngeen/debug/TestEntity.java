package com.ngeen.debug;

import com.badlogic.gdx.math.Vector3;
import com.ngeen.component.ComponentPoint;
import com.ngeen.engine.Ngeen;

public class TestEntity {
	private void SimpleTest(Ngeen ng){
		ng.EntityBuilder.makeEntity("e1");
		ng.EntityBuilder.makeEntity("e2");
		ng.EntityBuilder.makeEntity("e3");
		ng.EntityBuilder.makeEntity("e4");
		ng.EntityBuilder.makeEntity("e5");
		
		assert(ng.EntityBuilder.getByName("e1")!=null);
		assert(ng.EntityBuilder.getByName("e2")!=null);
		assert(ng.EntityBuilder.getByName("e3")!=null);
		assert(ng.EntityBuilder.getByName("e4")!=null);
		assert(ng.EntityBuilder.getByName("e5")!=null);

		ng.EntityBuilder.removeEntity("e1");
		ng.EntityBuilder.removeEntity("e2");
		ng.EntityBuilder.removeEntity("e3");
		ng.EntityBuilder.removeEntity("e4");
		
		assert(ng.EntityBuilder.getByName("e1")==null);
		assert(ng.EntityBuilder.getByName("e2")==null);
		assert(ng.EntityBuilder.getByName("e3")==null);
		assert(ng.EntityBuilder.getByName("e4")==null);
		
		
		ng.EntityBuilder.makeEntity("e1");
		ng.EntityBuilder.makeEntity("e2");
		ng.EntityBuilder.makeEntity("e3");
		ng.EntityBuilder.makeEntity("e4");
		ng.EntityBuilder.makeEntity("e5");

		assert(ng.EntityBuilder.getByName("e1")!=null);
		assert(ng.EntityBuilder.getByName("e2")!=null);
		assert(ng.EntityBuilder.getByName("e3")!=null);
		assert(ng.EntityBuilder.getByName("e4")!=null);
		assert(ng.EntityBuilder.getByName("e5")!=null);
		
		ng.EntityBuilder.removeEntity("e1");
		ng.EntityBuilder.removeEntity("e2");
		ng.EntityBuilder.removeEntity("e3");
		ng.EntityBuilder.removeEntity("e4");
		ng.EntityBuilder.removeEntity("e5");
	}
	
	private void StressTest(Ngeen ng){
		for(int i=0;i<2000;i++){
			ng.EntityBuilder.makeEntity("test"+i).addComponent(ComponentPoint.class);
		}
		for(int i=0;i<2000;i++){
			ng.EntityBuilder.removeEntity("test"+i);
		}
	}
	
	public TestEntity(Ngeen ng){
		SimpleTest(ng);
		StressTest(ng);
		Debugger.log("TestEntity()---PASS");
	}
}
