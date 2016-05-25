package com.ngeen.debug;

import com.ngeen.component.ComponentCamera;
import com.ngeen.component.ComponentMaterial;
import com.ngeen.component.ComponentMesh;
import com.ngeen.component.ComponentPoint;
import com.ngeen.engine.Ngeen;
import com.ngeen.systems.SystemConfiguration;

public class TestSystemConfiguration {

    public TestSystemConfiguration(Ngeen ng) {
        SystemConfiguration sys = new SystemConfiguration().all(ComponentCamera.class, ComponentPoint.class);
        sys.addClass(ComponentMaterial.class, ComponentMesh.class);
        sys.removeClass(ComponentMaterial.class, ComponentMesh.class);
        Debugger.log("TestSystemConfiguration()---PASS");
    }
}
