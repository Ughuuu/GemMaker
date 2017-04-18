package com.gemengine.component;

import lombok.Getter;
import lombok.Setter;

public abstract class Component {
	@Getter	@Setter private boolean enable;
	@Getter	private int id;
}
