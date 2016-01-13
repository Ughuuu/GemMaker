package com.ngeen.component;

import com.ngeen.debug.Debugger;
import com.ngeen.engine.Ngeen;
import com.ngeen.entity.Entity;

public class Script {
	protected Ngeen ng;
	protected Entity holder;
	
	public void onInit(){
		Debugger.log("onInit()");		
	}
	
	public void onUpdate(float delta){
		Debugger.log("onUpdate(): " + delta);
	}
}
