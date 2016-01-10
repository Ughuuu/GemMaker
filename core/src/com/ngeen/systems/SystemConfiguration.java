package com.ngeen.systems;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class SystemConfiguration {
	private Set<Class<?>> All;

	/**
	 * Class of current cell.
	 */
	private Class<?> cls;

	public SystemConfiguration() {
	}
	
	public SystemConfiguration(Set<Class<?>> All) {
		this.All = All;
	}

	public SystemConfiguration all(Class<?>... cls) {
		All = new TreeSet<Class<?>>(new Comparator<Class<?>>() {

			@Override
			public int compare(Class<?> o1, Class<?> o2) {
				if(o1.hashCode() > o2.hashCode())
					return 1;

				if(o1.hashCode() < o2.hashCode())
					return -1;
				return 0;
			}
		});
		All.addAll(Arrays.asList(cls));
		return this;
	}
	
	public final Set<Class<?>> getConfiguration(){
		return All;
	}
	
	public boolean equals(SystemConfiguration obj){
		return All.equals(obj.getConfiguration());		
	}
	
	public boolean equals(Set<Class<?>> obj){
		return All.equals(obj);		
	}
	
	public void addClass(Class<?>... cls){
		All.addAll(Arrays.asList(cls));
	}
	
	public void removeClass(Class<?>... cls){
		All.removeAll(Arrays.asList(cls));
	}
}
