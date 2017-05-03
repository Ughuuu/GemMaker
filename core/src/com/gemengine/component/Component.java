package com.gemengine.component;

import lombok.Getter;
import lombok.Setter;

public abstract class Component {
	@Getter
	private static int lastId;
	@Getter
	@Setter
	private boolean enable;
	@Getter
	private final int id;

	public Component() {
		id = lastId++;
	}
	
	public void onCreate(){
	}
}
