package com.gemengine.system;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jgit.diff.DiffEntry.ChangeType;
import org.jsync.sync.ClassSync;
import org.jsync.sync.SourceSync;

import com.gemengine.component.Component;
import com.gemengine.system.base.AssetListener;
import com.gemengine.system.base.ComponentListener;
import com.gemengine.system.base.SystemBase;
import com.gemengine.system.base.TimedSystem;
import com.gemengine.system.helper.AssetSystemHelper;
import com.gemengine.system.loaders.CodeLoader;
import com.gemengine.system.loaders.LoaderData;
import com.gemengine.system.loaders.SourceLoader;
import com.gemengine.system.manager.SystemManager;
import com.google.inject.Inject;

import lombok.val;

public class ManagerSystem extends TimedSystem implements AssetListener {
	private final AssetSystem assetSystem;
	private final SystemManager systemManager;
	private boolean reload = true;
	private ChangeType changeType;
	private final Map<String, ComponentListener> listeners = new HashMap<String, ComponentListener>();

	@Inject
	ManagerSystem(AssetSystem assetSystem, SystemManager systemManager) {
		super(300, true, 1);
		this.assetSystem = assetSystem;
		this.systemManager = systemManager;
	}

	public void addListener(ComponentListener componentListener) {
		listeners.put(componentListener.getClass().getName(), componentListener);
	}

	private void setCodeSync(ClassLoader classsLoader, AssetSystem assetSystem, String folder) {
		val codeData = new LoaderData(ClassSync.class, new CodeLoader.CodeParameter(classsLoader));
		assetSystem.addLoaderDefault(codeData, new CodeLoader<SystemBase>(assetSystem.getFileHandleResolver()),
				".class");
	}

	private void setSourceSync(AssetSystem assetSystem, String folder) {
		val sourceData = new LoaderData(SourceSync.class);
		SourceSync.options = "-8";
		assetSystem.addLoaderDefault(sourceData, new SourceLoader(assetSystem.getFileHandleResolver()), ".java");
	}

	@Override
	public void onInit() {
		setSourceSync(assetSystem, AssetSystem.assetsFolder);
		setCodeSync(ManagerSystem.class.getClassLoader(), assetSystem, AssetSystem.assetsFolder);
		assetSystem.addAssetListener(this);
		assetSystem.loadFolder("assets/system/");
	}

	void compileSources() {
		SourceSync[] syncs = assetSystem.getAll(SourceSync.class);
		if (syncs.length != 0) {
			if (!SourceSync.updateSource(syncs)) {
			}
			if (!syncs[0].getCompileError().equals("")) {
				System.out.println(syncs[0].getCompileError());
			}
		}
	}

	private static boolean extendsType(Class<?> type, Class<?> extendsType) {
		if (type.equals(Object.class)) {
			return false;
		}
		return type.equals(extendsType) || extendsType(type.getSuperclass(), extendsType);
	}

	@SuppressWarnings("unchecked")
	void updateCode() {
		if (!reload) {
			return;
		}
		reload = false;
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
					if (extendsType(cls, SystemBase.class)) {
						systemManager.replaceType((Class<? extends SystemBase>) cls);
					} else if (extendsType(cls, Component.class)) {
						for (val listener : listeners.entrySet()) {
							listener.getValue().onUpdate(cls);
						}
					}
				} catch (Throwable e) {
					// System.out.println("Class not found.");
				}
			}
		}
	}

	@Override
	public void onUpdate(float delta) {
		compileSources();
		updateCode();
	}

	@Override
	public void onChange(ChangeType changeType, String oldName, String newName) {
		String extension = AssetSystemHelper.getExtension(oldName);
		if (extension.equals(".class")) {
			reload = true;
		}
	}
}