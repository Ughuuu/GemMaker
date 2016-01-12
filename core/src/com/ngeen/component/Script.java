package com.ngeen.component;

import com.ngeen.debug.Debugger;
import com.ngeen.engine.Ngeen;

public class Script {
	protected Ngeen ng;
	
	public void onInit(){
		Debugger.log("onInit()");		
	}
	
	public void onUpdate(float delta){
		Debugger.log("onUpdate(): " + delta);
	}
}
