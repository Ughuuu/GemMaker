package com.ngeen.systems;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import com.ngeen.engine.EngineInfo;

public class SystemConfiguration {
	private Set<Class<?>> _All;
	private List<BitSet> _Configuration = new ArrayList<BitSet>();
	private Set<Class<?>> _Or;

	private boolean _Unset = true;

	public SystemConfiguration() {
	}

	public SystemConfiguration(Set<Class<?>> All) {
		this._All = All;
	}

	public void addClass(Class<?>... cls) {
		_All.addAll(Arrays.asList(cls));
	}

	public SystemConfiguration all(Class<?>... cls) {
		_Unset = false;
		_All = new TreeSet<Class<?>>(new Comparator<Class<?>>() {

			@Override
			public int compare(Class<?> o1, Class<?> o2) {
				if (o1.hashCode() > o2.hashCode())
					return 1;

				if (o1.hashCode() < o2.hashCode())
					return -1;
				return 0;
			}
		});
		_All.addAll(Arrays.asList(cls));
		configure();
		return this;
	}

	public boolean equals(Set<Class<?>> obj) {
		return _All.equals(obj);
	}

	public boolean equals(SystemConfiguration obj) {
		return _All.equals(obj.getConfiguration());
	}

	public final List<BitSet> getConfiguration() {
		if (_Unset) {
			return null;
		}
		return _Configuration;
	}

	public SystemConfiguration or(Class<?>... cls) {
		_Unset = false;
		_Or = new TreeSet<Class<?>>(new Comparator<Class<?>>() {

			@Override
			public int compare(Class<?> o1, Class<?> o2) {
				if (o1.hashCode() > o2.hashCode())
					return 1;

				if (o1.hashCode() < o2.hashCode())
					return -1;
				return 0;
			}
		});
		_Or.addAll(Arrays.asList(cls));
		configure();
		return this;
	}

	public void removeClass(Class<?>... cls) {
		_All.removeAll(Arrays.asList(cls));
	}

	private void configure() {
		_Configuration.clear();
		if (_Or == null) {
			_Configuration.add(new BitSet((EngineInfo.TotalComponents)));
			for (Class<?> cls : _All) {
				_Configuration.get(0).set(EngineInfo.ComponentIndexMap.get(cls));
			}
		} else {
			int i = 0;
			for (Class<?> cls : _Or) {
				_Configuration.add(new BitSet((EngineInfo.TotalComponents)));
				for (Class<?> cls2 : _All) {
					_Configuration.get(i).set(EngineInfo.ComponentIndexMap.get(cls2));
				}
				_Configuration.get(i).set(EngineInfo.ComponentIndexMap.get(cls));
				i++;
			}
		}
	}
}
