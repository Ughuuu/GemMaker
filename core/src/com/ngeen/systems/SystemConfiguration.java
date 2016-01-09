package com.ngeen.systems;

import java.util.List;

public class SystemConfiguration {
	private Class<?>[] All;
	private List<Class<?>[]> Ones;

	/**
	 * Class of current cell.
	 */
	private Class<?> cls;

	public SystemConfiguration() {
	}

	public SystemConfiguration all(Class<?>... cls) {
		All = cls;
		return this;
	}

	public SystemConfiguration one(Class<?>... cls) {
		Ones.add(cls);
		return this;
	}
}
