package com.gemengine.system.helper;

import com.gemengine.system.base.SystemBase;
import com.google.inject.AbstractModule;
import com.google.inject.name.Names;

import lombok.val;

public class SystemManagerModule extends AbstractModule{
		private final SystemManager systemManager;
		
		public SystemManagerModule(SystemManager systemManager){
			this.systemManager = systemManager;
		}
		
		@SuppressWarnings("unchecked")
		private <T extends SystemBase> Class<T> genericCast(Class<?> cls) {
			return (Class<T>) cls;
		}

		private <T extends SystemBase> void makeMapping(Class<T> systemClass, T system) {
			bind(systemClass).toInstance(system);
		}

		@Override
		protected void configure() {
			bind(Boolean.class).annotatedWith(Names.named("useExternalFiles")).toInstance(true);
			bind(Boolean.class).annotatedWith(Names.named("useBlockingLoad")).toInstance(true);
			bind(SystemManager.class).toInstance(systemManager);
			for (val typeEntry : systemManager.types.entrySet()) {
				Class<? extends SystemBase> key = typeEntry.getKey();
				SystemBase value = typeEntry.getValue();
				makeMapping(genericCast(key), value);
			}
		}
	}