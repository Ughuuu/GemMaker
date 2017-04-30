package com.gemengine.system;

import java.lang.reflect.Modifier;
import java.util.HashMap;
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
	public static final String codeFolder = "code/";
	private static boolean extendsType(Class<?> type, Class<?> extendsType) {
		if (type == null || type.equals(Object.class)) {
			return false;
		}
		return type.equals(extendsType) || extendsType(type.getSuperclass(), extendsType);
	}
	private final AssetSystem assetSystem;
	private final SystemManager systemManager;
	private boolean reload = true;

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

	@Override
	public void onChange(ChangeType changeType, String oldName, String newName) {
		String extension = AssetSystemHelper.getExtension(oldName);
		System.out.println(extension + " File changed " + changeType.toString());
		if (extension.equals(".class")) {
			reload = true;
		}
	}

	@Override
	public void onInit() {
		setSourceSync(assetSystem);
		setCodeSync(ManagerSystem.class.getClassLoader(), assetSystem);
		assetSystem.addAssetListener(this);
		assetSystem.loadFolder("assets/");
	}

	@Override
	public void onUpdate(float delta) {
		compileSources();
		updateCode();
	}

	private void setCodeSync(ClassLoader classsLoader, AssetSystem assetSystem) {
		val codeData = new LoaderData(ClassSync.class, new CodeLoader.CodeParameter(classsLoader));
		assetSystem.addLoaderDefault(codeData, new CodeLoader<SystemBase>(assetSystem.getFileHandleResolver()),
				".class");
	}

	private void setSourceSync(AssetSystem assetSystem) {
		val sourceData = new LoaderData(SourceSync.class);
		SourceSync.options = "-8";
		assetSystem.addLoaderDefault(sourceData, new SourceLoader(assetSystem.getFileHandleResolver()), ".java");
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

	@SuppressWarnings("unchecked")
	void updateCode() {
		if (!reload) {
			return;
		}
		System.out.println("System Reload Triggered");
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
					if (extendsType(cls, SystemBase.class) && !Modifier.isAbstract(cls.getModifiers())) {
						systemManager.replaceType((Class<? extends SystemBase>) cls);
					} else if (extendsType(cls, Component.class)) {
						for (val listener : listeners.entrySet()) {
							listener.getValue().onUpdate(cls);
						}
					}
				} catch (Throwable t) {
					t.printStackTrace();
					// System.out.println("Class not found.");
				}
			}
		}
	}
}