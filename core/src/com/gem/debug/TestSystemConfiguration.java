package com.gem.debug;

import com.gem.component.ComponentCamera;
import com.gem.component.ComponentMaterial;
import com.gem.component.ComponentMesh;
import com.gem.component.ComponentPoint;
import com.gem.engine.Gem;
import com.gem.systems.SystemConfiguration;

public class TestSystemConfiguration {

	public TestSystemConfiguration(Gem ng) {
		SystemConfiguration sys = new SystemConfiguration().all(ComponentCamera.class, ComponentPoint.class);
		sys.addClass(ComponentMaterial.class, ComponentMesh.class);
		sys.removeClass(ComponentMaterial.class, ComponentMesh.class);
		Debugger.log("TestSystemConfiguration()---PASS");
	}
}
