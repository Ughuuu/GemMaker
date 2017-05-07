package com.gemengine.system.manager;

import com.gemengine.engine.GemConfiguration;
import com.gemengine.system.base.SystemBase;
import com.google.inject.AbstractModule;
import com.google.inject.name.Names;

import lombok.val;

/**
 * The module given to guice injector
 * 
 * @author Dragos
 *
 */
public class SystemManagerModule extends AbstractModule {
	private final SystemManager systemManager;
	private final GemConfiguration configuration;

	public SystemManagerModule(SystemManager systemManager, GemConfiguration configuration) {
		this.systemManager = systemManager;
		this.configuration = configuration;
	}

	@SuppressWarnings("unchecked")
	private <T> Class<T> genericCast(Class<?> cls) {
		return (Class<T>) cls;
	}

	private <T> void makeMapping(Class<T> systemClass, T system) {
		bind(systemClass).toInstance(system);
	}

	@Override
	protected void configure() {
		bind(Boolean.class).annotatedWith(Names.named("useExternalFiles"))
				.toInstance(configuration.isUseExternalFiles());
		bind(Boolean.class).annotatedWith(Names.named("useBlockingLoad")).toInstance(configuration.isUseBlockingLoad());
		bind(Boolean.class).annotatedWith(Names.named("useDefaultLoaders"))
				.toInstance(configuration.isUseDefaultLoaders());
		bind(SystemManager.class).toInstance(systemManager);
		for (val typeEntry : systemManager.types.entrySet()) {
			SystemBase value = typeEntry.getValue();
			makeMapping(genericCast(value.getClass()), value);
		}
	}
}