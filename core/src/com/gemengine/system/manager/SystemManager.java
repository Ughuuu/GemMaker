package com.gemengine.system.manager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import com.gemengine.engine.GemConfiguration;
import com.gemengine.system.AssetSystem;
import com.gemengine.system.ManagerSystem;
import com.gemengine.system.base.SystemBase;
import com.gemengine.system.base.TimedSystem;
import com.google.inject.Module;

import lombok.Getter;

public class SystemManager extends TypeManager<SystemBase> {
	private static enum State {
		Added, Started, Destroyed
	}

	private final Set<SystemBase> baseSystems;
	private final Map<SystemBase, State> systemToState;
	protected int toStart = 0;
	private final Set<TimedSystem> timedSystems;
	private final GemConfiguration configuration;
	@Getter
	private final List<Class<? extends SystemBase>> excludeList = new ArrayList<>(
			Arrays.asList(AssetSystem.class, ManagerSystem.class));

	public SystemManager(GemConfiguration configuration) {
		super(SystemBase.class);
		this.configuration = configuration;
		baseSystems = new TreeSet<SystemBase>();
		systemToState = new HashMap<SystemBase, State>();
		timedSystems = new TreeSet<TimedSystem>();
		doMapping();
	}

	@Override
	public void onInit() {
		super.onInit();
		for (SystemBase system : baseSystems) {
			system.onInit();
			systemToState.put(system, State.Started);
		}
		toStart = 0;
	}

	@Override
	public void onUpdate(float delta) {
		super.onUpdate(delta);
		for (TimedSystem system : timedSystems) {
			State state = systemToState.get(system);
			if (state == State.Started) {
				try {
					system.onUpdate(delta);
				} catch (Throwable t) {
					t.printStackTrace();
					system.setEnable(false);
				}
			}
		}
		if (toStart == 0) {
			return;
		}
		toStart = 0;
		for (SystemBase system : baseSystems) {
			State state = systemToState.get(system);
			if (state == State.Added) {
				try {
					system.onInit();
					systemToState.put(system, State.Started);
				} catch (Throwable t) {
					t.printStackTrace();
					system.setEnable(false);
				}
			}
		}
	}

	@Override
	protected void elementAdd(SystemBase element) {
		systemToState.put(element, State.Added);
		toStart++;
		baseSystems.add(element);
		if (element instanceof TimedSystem) {
			timedSystems.add((TimedSystem) element);
		}
	}

	@Override
	protected void elementCopy(SystemBase oldElement, SystemBase newElement) {
		State state = systemToState.get(oldElement);
		if (state == null) {
			elementAdd(newElement);
		} else {
			elementDelete(oldElement);
			elementAdd(newElement);
			toStart--;
			systemToState.put(newElement, state);
		}
	}

	@Override
	protected void elementDelete(SystemBase element) {
		baseSystems.remove(element);
		if (element instanceof TimedSystem) {
			timedSystems.remove(element);
		}
	}

	@Override
	protected Module getModule() {
		return new SystemManagerModule(this, configuration);
	}
}
