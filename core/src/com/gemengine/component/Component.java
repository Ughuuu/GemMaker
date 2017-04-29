package com.gemengine.component;

import lombok.Getter;
import lombok.Setter;

public abstract class Component {
	private static int lastId;

	@Getter
	@Setter
	private boolean enable;
	@Getter
	private final int id;

	private Component() {
		id = lastId++;
	}
}
