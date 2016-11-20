package com.gem.component;

import com.gem.debug.Debugger;
import com.gem.engine.Gem;
import com.gem.entity.Entity;

import lombok.Getter;

public class Script {
	@Getter
	protected Entity holder;
	protected Gem gem;

	public void onInit() {
		Debugger.log("onInit()");
	}

	public void onUpdate(float delta) {
		Debugger.log("onUpdate(): " + delta);
	}
}
