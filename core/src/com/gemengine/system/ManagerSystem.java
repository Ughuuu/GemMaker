package com.gemengine.system;

import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.MarkerManager;
import org.eclipse.jgit.diff.DiffEntry.ChangeType;
import org.jsync.sync.ClassSync;
import org.jsync.sync.SourceSync;

import com.gemengine.component.Component;
import com.gemengine.system.base.AssetListener;
import com.gemengine.system.base.ComponentListener;
import com.gemengine.system.base.SystemBase;
import com.gemengine.system.base.TimedSystem;
import com.gemengine.system.helper.AssetSystemHelper;
import com.gemengine.system.helper.Messages;
import com.gemengine.system.loaders.CodeLoader;
import com.gemengine.system.loaders.LoaderData;
import com.gemengine.system.loaders.SourceLoader;
import com.gemengine.system.manager.SystemManager;
import com.gemengine.system.manager.TypeManager;
import com.google.inject.Inject;

import lombok.val;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class ManagerSystem extends TimedSystem implements AssetListener {
	public static final String codeFolder = Messages.getString("ManagerSystem.CodeFolder"); //$NON-NLS-1$
	private final AssetSystem assetSystem;
	private final SystemManager systemManager;
	private boolean reload = true;

	private final Map<String, ComponentListener> listeners = new HashMap<String, ComponentListener>();

	@Inject
	ManagerSystem(AssetSystem assetSystem, SystemManager systemManager) {
		super(300, true, 1);
		this.assetSystem = assetSystem;
		this.systemManager = systemManager;
		assetSystem.addAssetListener(this);
	}

	public void addListener(ComponentListener componentListener) {
		listeners.put(componentListener.getClass().getName(), componentListener);
	}

	@Override
	public void onChange(ChangeType changeType, String oldName, String newName) {
		String extension = AssetSystemHelper.getExtension(oldName);
		if (extension.equals(Messages.getString("ManagerSystem.ClassFileExtension"))) { //$NON-NLS-1$
			reload = true;
		}
	}

	@Override
	public void onInit() {
		setSourceSync(assetSystem);
		setCodeSync(ManagerSystem.class.getClassLoader(), assetSystem);
		assetSystem.loadFolder("assets/");
	}

	@Override
	public void onUpdate(float delta) {
		compileSources();
		updateCode();
	}

	private void compileSources() {
		SourceSync[] syncs = assetSystem.getAll(SourceSync.class);
		if (syncs.length != 0) {
			if (!SourceSync.updateSource(syncs)) {
			}
			if (!syncs[0].getCompileError().equals("")) {
				log.warn(MarkerManager.getMarker("gem"), "Manager System compile error {}", syncs[0].getCompileError());
			}
		}
	}

	private void setCodeSync(ClassLoader classsLoader, AssetSystem assetSystem) {
		val codeData = new LoaderData(ClassSync.class, new CodeLoader.CodeParameter(classsLoader));
		assetSystem.addLoaderDefault(codeData, null, new CodeLoader<SystemBase>(assetSystem.getFileHandleResolver()),
				Messages.getString("ManagerSystem.ClassFileExtension")); //$NON-NLS-1$
	}

	private void setSourceSync(AssetSystem assetSystem) {
		val sourceData = new LoaderData(SourceSync.class);
		SourceSync.options = Messages.getString("ManagerSystem.CompileOptions"); //$NON-NLS-1$
		assetSystem.addLoaderDefault(sourceData, null, new SourceLoader(assetSystem.getFileHandleResolver()),
				Messages.getString("ManagerSystem.SourceFileExtension")); //$NON-NLS-1$
	}

	@SuppressWarnings("unchecked")
	void updateCode() {
		if (!reload) {
			return;
		}
		log.info(MarkerManager.getMarker("gem"), "---------------------------------------------------");
		log.info(MarkerManager.getMarker("gem"), "Manager System reload triggered");
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
					if (TypeManager.extendsType(cls, SystemBase.class) && !Modifier.isAbstract(cls.getModifiers())) {
						// For now clean all systems and regenerate them.
						// Later on only regenerate those that modified.
						systemManager.replaceType((Class<? extends SystemBase>) cls);
						SystemBase system = systemManager.getType(cls.getName());
						if (system != null) {
							system.setEnable(true);
						}
					} else if (TypeManager.extendsType(cls, Component.class)) {
						for (val listener : listeners.entrySet()) {
							// listener.getValue().onTypeChange(cls);
						}
					}
				} catch (Throwable t) {
					log.warn(MarkerManager.getMarker("gem"), "Manager System update", t);
				}
			}
		}
	}
}