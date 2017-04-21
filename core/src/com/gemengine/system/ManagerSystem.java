package com.gemengine.system;

import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.SimpleFormatter;

import org.jsync.sync.Sync;

import com.badlogic.gdx.audio.Sound;
import com.gemengine.system.base.SystemBase;
import com.gemengine.system.base.TimedSystem;
import com.gemengine.system.loaders.CodeLoader;
import com.gemengine.system.manager.SystemManager;
import com.google.inject.Inject;

import lombok.val;
import lombok.extern.java.Log;

public class ManagerSystem extends TimedSystem {
	public static final String systemSourceFolder = "system/";
	private final AssetSystem assetSystem;
	private final SystemManager systemManager;

	@Inject
	ManagerSystem(AssetSystem assetSystem, SystemManager systemManager) {
		super(300, true, 1);
		this.assetSystem = assetSystem;
		this.systemManager = systemManager;
		val loaderData = new AssetSystem.LoaderData(Sync.class,
				new CodeLoader.CodeParameter(SystemBase.class.getClassLoader(), "./assets/", "."));
		assetSystem.addLoaderDefault(loaderData, new CodeLoader<SystemBase>(assetSystem.getFileHandleResolver()),
				".java");
		assetSystem.addTypeFolder(loaderData, "system/");
		Sync.options = "-8";
	}

	@Override
	public void onInit() {
		assetSystem.loadFolder("assets/");
	}

	@SuppressWarnings("unchecked")
	@Override
	public void onUpdate(float delta) {
		Sync<SystemBase>[] syncs = assetSystem.getAll(Sync.class);
		if (syncs.length == 0) {
			return;
		}
		Sync.updateAll(syncs);
		for (val sync : syncs) {
			Class<? extends SystemBase> cls;
			try {
				cls = (Class<? extends SystemBase>) sync.getClassType();
				if (cls != null) {
					systemManager.replaceType(cls);
				}
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
	}
}