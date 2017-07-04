package com.gemengine.test.helper;

import org.apache.logging.log4j.MarkerManager;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.graphics.GL20;
import com.gemengine.engine.Gem;
import com.gemengine.engine.GemConfiguration;
import com.gemengine.system.AssetSystem;
import com.gemengine.system.ComponentSystem;
import com.gemengine.system.EntitySystem;
import com.gemengine.system.ManagerSystem;
import com.gemengine.system.base.SystemBase;
import com.gemengine.system.manager.SystemManager;

public class TestHelper {	
	public static SystemManager createSystemManager(){
		GemConfiguration gemConfiguration = GemConfiguration
				.builder()
				.useBlockingLoad(true)
				.useDefaultLoaders(true)
				.useExternalFiles(true).build();
		SystemManager systemManager = new SystemManager(gemConfiguration);
		systemManager.replaceType(AssetSystem.class);
		systemManager.replaceType(ComponentSystem.class);
		systemManager.replaceType(EntitySystem.class);
		systemManager.replaceType(ManagerSystem.class);
		systemManager.onInit();
		return systemManager;
	}
}
