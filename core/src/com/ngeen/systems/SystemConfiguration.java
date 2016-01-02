package com.ngeen.systems;

public class SystemConfiguration {
	/**
	 * Next system configuration in chain.
	 */
	private SystemConfiguration next = null;
	
	/**
	 * Either and or or.
	 */
	private boolean nextAnd = false;
	
	/**
	 * Class of current cell.
	 */
	private Class<?> cls;
	
	
	public SystemConfiguration(Class<?> cls){
		this.cls = cls;
	}
	
	public SystemConfiguration add(boolean func, Class<?> cls){
		nextAnd = func;
		this.next = new SystemConfiguration(cls);
		return next;
	}
}
