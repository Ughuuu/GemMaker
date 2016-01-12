package com.ngeen.debug;

import com.ngeen.component.ComponentCamera;
import com.ngeen.component.ComponentMaterial;
import com.ngeen.component.ComponentPoint;
import com.ngeen.component.ComponentVariable;
import com.ngeen.engine.Ngeen;
import com.ngeen.systems.SystemConfiguration;

public class TestSystemConfiguration {
	
	public TestSystemConfiguration(Ngeen ng){
		SystemConfiguration sys = new SystemConfiguration().all(ComponentCamera.class, ComponentPoint.class);
		assert(sys.getConfigurationTypes().contains(ComponentCamera.class));
		assert(sys.getConfigurationTypes().contains(ComponentPoint.class));
		sys.addClass(ComponentMaterial.class, ComponentVariable.class);
		assert(sys.getConfigurationTypes().contains(ComponentCamera.class));
		assert(sys.getConfigurationTypes().contains(ComponentPoint.class));
		assert(sys.getConfigurationTypes().contains(ComponentVariable.class));
		assert(sys.getConfigurationTypes().contains(ComponentMaterial.class));
		sys.removeClass(ComponentMaterial.class, ComponentVariable.class);
		assert(sys.getConfigurationTypes().contains(ComponentCamera.class));
		assert(sys.getConfigurationTypes().contains(ComponentPoint.class));
		assert(!sys.getConfigurationTypes().contains(ComponentVariable.class));
		assert(!sys.getConfigurationTypes().contains(ComponentMaterial.class));
		Debugger.log("TestSystemConfiguration()---PASS");
	}
}
