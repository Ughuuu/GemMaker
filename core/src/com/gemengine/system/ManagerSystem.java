package com.gemengine.system;

import org.eclipse.jgit.diff.DiffEntry.ChangeType;
import org.jsync.sync.ClassSync;
import org.jsync.sync.SourceSync;

import com.gemengine.system.base.AssetListener;
import com.gemengine.system.base.SystemBase;
import com.gemengine.system.base.TimedSystem;
import com.gemengine.system.loaders.CodeLoader;
import com.gemengine.system.loaders.SourceLoader;
import com.gemengine.system.manager.SystemManager;
import com.google.inject.Inject;

import lombok.val;

public class ManagerSystem extends TimedSystem {
	public static final String systemSourceFolder = "system/";
	private final AssetSystem assetSystem;
	private final SystemManager systemManager;

	@Inject
	ManagerSystem(AssetSystem assetSystem, SystemManager systemManager) {
		super(300, true, 1);
		this.assetSystem = assetSystem;
		this.systemManager = systemManager;
		val sourceData = new AssetSystem.LoaderData(SourceSync.class,
				new SourceLoader.SourceParameter("./assets/", "."));
		SourceSync.options = "-8";
		assetSystem.addLoaderDefault(sourceData, new SourceLoader(assetSystem.getFileHandleResolver()), ".java");
		assetSystem.addTypeFolder(sourceData, "system/");
		val codeData = new AssetSystem.LoaderData(ClassSync.class,
				new CodeLoader.CodeParameter(SystemBase.class.getClassLoader(), "."));
		assetSystem.addLoaderDefault(codeData, new CodeLoader<SystemBase>(assetSystem.getFileHandleResolver()),
				".class");
		assetSystem.addTypeFolder(codeData, "system/");
	}

	@Override
	public void onInit() {
		assetSystem.loadFolder("assets/");
	}

	void compileSources() {
		SourceSync[] syncs = assetSystem.getAll(SourceSync.class);
		if (syncs.length != 0) {
			if (!SourceSync.updateSource(syncs)) {
				return;
			}
			if (!syncs[0].getCompileError().equals("")) {
				System.out.println(syncs[0].getCompileError());
			}
		}
	}

	@SuppressWarnings("unchecked")
	void updateCode() {
		ClassSync<SystemBase>[] syncs = assetSystem.getAll(ClassSync.class);
		if (syncs.length != 0) {
			try {
				ClassSync.updateClass(syncs);
			} catch (Throwable t) {
				t.printStackTrace();
				return;
			}
			for (val sync : syncs) {
				try {
					Class<?> cls = sync.getClassType();
					if (!cls.isAssignableFrom(SystemBase.class)) {
						continue;
					}
					if (cls != null) {
						systemManager.replaceType((Class<? extends SystemBase>) cls);
					}
				} catch (Throwable e) {
					e.printStackTrace();
				}
			}
		}
	}

	@Override
	public void onUpdate(float delta) {
		compileSources();
		updateCode();
	}
}