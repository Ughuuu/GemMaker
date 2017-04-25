package com.gemengine.component;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

public abstract class Component {
	@Getter
	@Setter
	private boolean enable;
}
