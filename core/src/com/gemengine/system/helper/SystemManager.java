package com.gemengine.system.helper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.gemengine.system.base.SystemBase;
import com.gemengine.system.base.TimedSystem;
import com.google.inject.Module;

public class SystemManager extends TypeManager<SystemBase> {
	private final List<SystemBase> baseSystems;
	private final Comparator<SystemBase> priorityComparator = new Comparator<SystemBase>() {
		@Override
		public int compare(SystemBase system1, SystemBase system2) {
			if (system1.getPriority() < system2.getPriority()) {
				return -1;
			}
			if (system1.getPriority() > system2.getPriority()) {
				return 1;
			}
			return 0;
		}
	};
	private final List<TimedSystem> timedSystems;

	public SystemManager() {
		baseSystems = new ArrayList<SystemBase>();
		timedSystems = new ArrayList<TimedSystem>();
	}

	public void onInit() {
		for(SystemBase system : baseSystems){
			system.onInit();
		}
	}

	public void onUpdate(float delta) {
		for(TimedSystem system : timedSystems){
			system.onUpdate(delta);
		}
	}

	@Override
	protected void elementAdd(SystemBase element) {
		baseSystems.add(element);
		if(element instanceof TimedSystem){
			timedSystems.add((TimedSystem)element);
			Collections.sort(timedSystems, priorityComparator);
		}
		Collections.sort(baseSystems, priorityComparator);
	}

	@Override
	protected void elementDelete(SystemBase element) {
		baseSystems.remove(element);
		if(element instanceof TimedSystem){
			timedSystems.remove(element);
			Collections.sort(timedSystems, priorityComparator);
		}
		Collections.sort(baseSystems, priorityComparator);
	}

	@Override
	protected Module getModule() {
		return new SystemManagerModule(this);
	}
}
