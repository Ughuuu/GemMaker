package com.gemengine.system.helper;

import java.util.HashSet;
import java.util.Set;

import com.gemengine.component.Component;

public class ListenerHelper {
	public static Set<String> createConfiguration(Class<? extends Component>... components) {
		Set<String> configuration = new HashSet<String>();
		for (Class<? extends Component> component : components) {
			configuration.add(component.getName());
		}
		return configuration;
	}
}
