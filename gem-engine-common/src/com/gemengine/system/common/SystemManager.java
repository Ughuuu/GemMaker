package com.gemengine.system.core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import gemengine.system.AssetSystem;
import gemengine.system.ManagerSystem;
import gemengine.system.base.SystemBase;
import org.apache.logging.log4j.MarkerManager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import gemengine.system.base.TimedSystem;
import com.google.inject.Module;

/**
 * Implementation of {@link TypeManager} with
 * generic parameter {@link SystemBase}. This is used
 * by calling replaceType with the system you want to replace by a new system.
 * This is done in {@link ManagerSystem}
 * 
 * @author Dragos
 *
 */
public class SystemManager {
	private static enum State {
		Added, Started, Destroyed
	}

	private final List<SystemBase> baseSystems;
	private final Map<SystemBase, State> systemToState;
	private final List<TimedSystem> timedSystems;
	private final List<Class<? extends SystemBase>> excludeList = new ArrayList<>(
			Arrays.asList(AssetSystem.class, ManagerSystem.class));
	private final InputMultiplexer inputMultiplexer = new InputMultiplexer();

	public SystemManager() {
		baseSystems = new ArrayList<SystemBase>();
		systemToState = new HashMap<SystemBase, State>();
		timedSystems = new ArrayList<TimedSystem>();
	}

	@Override
	public void onInit() {
		super.onInit();
		for (SystemBase system : baseSystems) {
			try {
				system.onInit();
				systemToState.put(system, State.Started);
			} catch (Throwable t) {
				log.warn(MarkerManager.getMarker("gem"), "System Manager start", t);
				system.setEnable(false);
			}
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
					log.warn(MarkerManager.getMarker("gem"), "System Manager update", t);
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
					log.warn(MarkerManager.getMarker("gem"), "System Manager init", t);
					system.setEnable(false);
				}
			}
		}
	}

	/**
	 * Called when the system is disposed of. Preceded by a call to
	 * {@link #onPause()}.
	 */
	public void onDispose() {
		for (SystemBase system : baseSystems) {
			try {
				system.onExit();
			} catch (Throwable t) {
				log.warn(MarkerManager.getMarker("gem"), "System Manager init", t);
				system.setEnable(false);
			}
		}
	}

	/**
	 * Called when the {@link Application} is paused, usually when it's not
	 * active or visible on screen. An Application is also paused before it is
	 * destroyed.
	 */
	public void onPause() {
		for (SystemBase system : baseSystems) {
			try {
				system.onPause();
			} catch (Throwable t) {
				log.warn(MarkerManager.getMarker("gem"), "System Manager init", t);
				system.setEnable(false);
			}
		}
	}

	/**
	 * Called when the {@link Application} is resumed from a paused state,
	 * usually when it regains focus.
	 */
	public void onResume() {
		for (SystemBase system : baseSystems) {
			try {
				system.onResume();
			} catch (Throwable t) {
				log.warn(MarkerManager.getMarker("gem"), "System Manager init", t);
				system.setEnable(false);
			}
		}
	}

	/**
	 * Called when the {@link Application} is resized. This can happen at any
	 * point during a non-paused state but will never happen before a call to
	 * {@link #onInit()}.
	 * 
	 * @param width
	 *            the new width in pixels
	 * @param height
	 *            the new height in pixels
	 */
	public void onResize(int width, int height) {
		for (SystemBase system : baseSystems) {
			try {
				system.onResize(width, height);
			} catch (Throwable t) {
				log.warn(MarkerManager.getMarker("gem"), "System Manager init", t);
				system.setEnable(false);
			}
		}
	}

	public void addInputProcessor(InputProcessor inputProcessor) {
		inputMultiplexer.addProcessor((InputProcessor) inputProcessor);
		Gdx.input.setInputProcessor(inputMultiplexer);
	}

	public void removeInputProcessor(InputProcessor inputProcessor) {
		inputMultiplexer.removeProcessor((InputProcessor) inputProcessor);
		if (inputMultiplexer.getProcessors().size == 0) {
			inputMultiplexer.setProcessors(null);
		}
	}

	@Override
	protected void elementAdd(SystemBase element) {
		systemToState.put(element, State.Added);
		toStart++;
		baseSystems.add(element);
		Collections.sort(baseSystems);
		if (element instanceof TimedSystem) {
			timedSystems.add((TimedSystem) element);
			Collections.sort(timedSystems);
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
		}
	}

	@Override
	protected void elementDelete(SystemBase element) {
		systemToState.remove(element);
		baseSystems.remove(element);
		Collections.sort(baseSystems);
		if (element instanceof TimedSystem) {
			timedSystems.remove(element);
			Collections.sort(timedSystems);
		}
	}

	@Override
	protected Module getModule() {
		return new SystemManagerModule(this, configuration);
	}
}
