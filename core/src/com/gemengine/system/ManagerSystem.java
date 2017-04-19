package com.gemengine.system;

import org.jsync.sync.Sync;

import com.gemengine.system.base.SystemBase;
import com.gemengine.system.base.TimedSystem;
import com.gemengine.system.helper.CodeLoader;
import com.google.inject.Inject;

public class ManagerSystem extends TimedSystem {
	public static final String componentSourceFolder = "system/";
	private final AssetSystem assetSystem;

	@Inject
	public ManagerSystem(AssetSystem assetSystem) {
		super(100, true, 1);
		this.assetSystem = assetSystem;
		assetSystem.addLoaderDefault(Sync.class, new CodeLoader<SystemBase>(assetSystem.getFileHandleResolver()),
				".java");
	}

	@Override
	public void onInit() {
		assetSystem.loadFolder("assets/");
	}

	@Override
	public void onUpdate(float delta) {
		for (String name : assetSystem.getAssetNames()) {
			System.out.println(name);
		}
	}
}