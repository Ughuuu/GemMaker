package com.ngeen.systems;

import java.util.Arrays;
import java.util.BitSet;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import com.ngeen.engine.EngineInfo;

public class SystemConfiguration {
	private Set<Class<?>> _All;

	private BitSet _Configuration = new BitSet(EngineInfo.TotalComponents);

	public SystemConfiguration() {
	}
	
	public SystemConfiguration(Set<Class<?>> All) {
		this._All = All;
	}
	
	private void configure(){
		_Configuration.clear();
		for(Class<?> cls : _All){
			_Configuration.set(EngineInfo.ComponentIndexMap.get(cls));
		}
	}

	public SystemConfiguration all(Class<?>... cls) {
		_All = new TreeSet<Class<?>>(new Comparator<Class<?>>() {

			@Override
			public int compare(Class<?> o1, Class<?> o2) {
				if(o1.hashCode() > o2.hashCode())
					return 1;

				if(o1.hashCode() < o2.hashCode())
					return -1;
				return 0;
			}
		});
		_All.addAll(Arrays.asList(cls));
		configure();
		return this;
	}
	
	public final Set<Class<?>> getConfigurationTypes(){
		return _All;
	}
	
	public final BitSet getConfiguration(){
		return _Configuration;
	}
	
	public boolean equals(SystemConfiguration obj){
		return _All.equals(obj.getConfiguration());		
	}
	
	public boolean equals(Set<Class<?>> obj){
		return _All.equals(obj);		
	}
	
	public void addClass(Class<?>... cls){
		_All.addAll(Arrays.asList(cls));
	}
	
	public void removeClass(Class<?>... cls){
		_All.removeAll(Arrays.asList(cls));
	}
}
