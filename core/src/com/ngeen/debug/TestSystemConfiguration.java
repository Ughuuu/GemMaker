package com.ngeen.debug;

import com.ngeen.component.ComponentCamera;
import com.ngeen.component.ComponentDrawble;
import com.ngeen.component.ComponentPoint;
import com.ngeen.component.ComponentVariable;
import com.ngeen.engine.Ngeen;
import com.ngeen.systems.SystemConfiguration;

public class TestSystemConfiguration {
	
	public TestSystemConfiguration(Ngeen ng){
		SystemConfiguration sys = new SystemConfiguration().all(ComponentCamera.class, ComponentPoint.class);
		assert(sys.getConfiguration().contains(ComponentCamera.class));
		assert(sys.getConfiguration().contains(ComponentPoint.class));
		sys.addClass(ComponentDrawble.class, ComponentVariable.class);
		assert(sys.getConfiguration().contains(ComponentCamera.class));
		assert(sys.getConfiguration().contains(ComponentPoint.class));
		assert(sys.getConfiguration().contains(ComponentVariable.class));
		assert(sys.getConfiguration().contains(ComponentDrawble.class));
		sys.removeClass(ComponentDrawble.class, ComponentVariable.class);
		assert(sys.getConfiguration().contains(ComponentCamera.class));
		assert(sys.getConfiguration().contains(ComponentPoint.class));
		assert(!sys.getConfiguration().contains(ComponentVariable.class));
		assert(!sys.getConfiguration().contains(ComponentDrawble.class));
		Debugger.log("TestSystemConfiguration()---PASS");
	}
}
