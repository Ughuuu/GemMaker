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

	private void checkChanges(){
		assetSystem.
	}
	
	@Override
	public void onInit(){
		checkChanges();
	}
	
	@Override
	public void onUpdate(float delta) {
		checkChanges();
	}
}