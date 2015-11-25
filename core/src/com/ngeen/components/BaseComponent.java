package com.ngeen.components;

public class BaseComponent {
	protected int id, parent = -1;
	protected boolean enable = true;
	private static int unique_id = 0;

	/**
	 * Create a BaseComponent with a unique id.
	 */
	public BaseComponent() {
		id = unique_id++;
	}
}
