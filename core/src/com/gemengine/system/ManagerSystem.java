package com.gemengine.system;

import java.io.File;
import java.lang.reflect.Modifier;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.Map;
import java.util.jar.JarFile;

import org.apache.logging.log4j.MarkerManager;
import org.eclipse.jgit.diff.DiffEntry.ChangeType;
import org.jsync.sync.ClassSync;
import org.jsync.sync.SourceSync;

import com.badlogic.gdx.utils.TimeUtils;
import com.gemengine.component.Component;
import com.gemengine.listener.AssetListener;
import com.gemengine.listener.ComponentListener;
import com.gemengine.system.base.SystemBase;
import com.gemengine.system.base.TimedSystem;
import com.gemengine.system.helper.AssetSystemHelper;
import com.gemengine.system.helper.Messages;
import com.gemengine.system.loaders.CodeLoader;
import com.gemengine.system.loaders.JarLoader;
import com.gemengine.system.loaders.LoaderData;
import com.gemengine.system.loaders.SourceLoader;
import com.gemengine.system.manager.SystemManager;
import com.gemengine.system.manager.TypeManager;
import com.google.inject.Inject;

import lombok.val;
import lombok.extern.log4j.Log4j2;

/**
 * The Manager System is used to compile new systems and add them to the
 * existing ones. It may also receive errors from them, which it logs.
 * 
 * @author Dragos
 *
 */
@Log4j2
public class ManagerSystem extends TimedSystem implements AssetListener {
	public static final String codeFolder = Messages.getString("ManagerSystem.CodeFolder"); //$NON-NLS-1$
	private final AssetSystem assetSystem;
	private final SystemManager systemManager;
	private boolean reloadJar = true;
	private boolean reloadJava = true;
	private boolean reloadClass = true;
	private final TimingSystem timingSystem;
	private URL[] libUrls;

	private final Map<String, ComponentListener> listeners = new HashMap<String, ComponentListener>();

	@Inject
	ManagerSystem(AssetSystem assetSystem, SystemManager systemManager, TimingSystem timingSystem) {
		super(300, true, 1);
		this.timingSystem = timingSystem;
		this.assetSystem = assetSystem;
		this.systemManager = systemManager;
	}

	/**
	 * Add a listener here if you want to listen for component type class
	 * change.
	 * 
	 * @param componentListener
	 */
	public void addListener(ComponentListener componentListener) {
		listeners.put(componentListener.getClass().getName(), componentListener);
	}

	@Override
	public void onChange(ChangeType changeType, String oldName, String newName) {
		String extension = AssetSystemHelper.getExtension(oldName);
		switch (extension) {
		case ".class":
			reloadClass = true;
		case ".java":
			reloadJava = true;
		case ".jar":
			reloadJar = true;
			log.debug(changeType + " " + oldName + " " + newName);
		default:
			break;
		}
	}

	@Override
	public void onInit() {
		assetSystem.addAssetListener(this);
		setSourceSync(assetSystem);
		setCodeSync(ManagerSystem.class.getClassLoader(), assetSystem);
		setJarSync(assetSystem);
		// assetSystem.loadFolder("assets/libs/");
		assetSystem.loadFolder("assets/");
	}

	@Override
	public void onUpdate(float delta) {
		if (!reloadJava && !reloadJar && !reloadClass || !assetSystem.isLoaded()) {
			return;
		}
		log.info(MarkerManager.getMarker("gem"), "---------------------------------------------------");
		if (reloadJar) {
			reloadJar = false;
			updateJars();
			return;
		}
		if (reloadJava) {
			reloadJava = false;
			compileSources();
			return;
		}
		if (reloadClass) {
			reloadClass = false;
			updateCode();
			return;
		}
	}

	private void compileSources() {
		SourceSync[] syncs = assetSystem.getAll(SourceSync.class);
		if (syncs.length != 0) {
			log.info(MarkerManager.getMarker("gem"), "Manager System reload java");
			if (!SourceSync.updateSource(syncs)) {
			}
			if (!syncs[0].getCompileError().equals("")) {
				log.warn(MarkerManager.getMarker("gem"), "Manager System compile error {}", syncs[0].getCompileError());
			}
		}
	}

	private void setJarSync(AssetSystem assetSystem) {
		val codeData = new LoaderData(JarFile.class, new JarLoader.JarParameter());
		assetSystem.addLoaderDefault(codeData, null, new JarLoader(assetSystem.getFileHandleResolver()), ".jar");
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
	void updateJars() {
		JarFile[] jars = assetSystem.getAll(JarFile.class);
		if (jars.length != 0) {
			log.info(MarkerManager.getMarker("gem"), "Manager System reload jar");
			libUrls = new URL[jars.length];
			for (int i = 0; i < jars.length; i++) {
				try {
					libUrls[i] = new File(jars[i].getName()).toURI().toURL();
				} catch (Throwable t) {
					log.warn(MarkerManager.getMarker("gem"), "Manager System update", t);
				}
			}
			URLClassLoader libsLoader = new URLClassLoader(libUrls);
			for (val jar : jars) {
				try {
					val entries = jar.entries();
					while (entries.hasMoreElements()) {
						val entry = entries.nextElement();
						String fileName = entry.getName();
						if (AssetSystemHelper.getExtension(fileName).equals(".class")) {
							Class<?> cls = Class.forName(
									AssetSystemHelper.getWithoutExtension(fileName).replace('/', '.'), true,
									libsLoader);
							if (fileName.contains("system/")) {
								loadSystem(cls);
							}
						}
					}
				} catch (Throwable t) {
					log.warn(MarkerManager.getMarker("gem"), "Manager System update", t);
				}
			}
		}
	}

	private boolean loadSystem(Class<?> cls) {
		if (TypeManager.extendsType(cls, SystemBase.class) && !Modifier.isAbstract(cls.getModifiers())) {
			// For now clean all systems and regenerate them.
			// Later on only regenerate those that modified.
			systemManager.replaceType((Class<? extends SystemBase>) cls);
			SystemBase system = systemManager.getType(cls.getName());
			if (system != null) {
				system.setEnable(true);
			}
			return true;
		}
		return false;
	}

	@SuppressWarnings("unchecked")
	void updateCode() {
		ClassSync[] syncs = assetSystem.getAll(ClassSync.class);
		if (syncs.length != 0) {
			if (libUrls != null) {
				URLClassLoader libsLoader = new URLClassLoader(libUrls);
				for (int i = 0; i < syncs.length; i++) {
					if (libsLoader != null) {
						syncs[i].setClassLoader(libsLoader);
					}
				}
			}
			try {
				ClassSync.updateClass(syncs);
			} catch (Throwable t) {
				t.printStackTrace();
				return;
			}
			log.info(MarkerManager.getMarker("gem"), "Manager System reload class");
			for (val sync : syncs) {
				try {
					Class<?> cls = sync.getClassType();
					if (loadSystem(cls)) {

					} else if (TypeManager.extendsType(cls, Component.class)) {
						for (val listener : listeners.entrySet()) {
							// listener.getValue().onTypeChange(cls);
						}
					}
				} catch (Throwable t) {
					log.warn(MarkerManager.getMarker("gem"), "Class not found " + sync.getClassName(), t);
				}
			}
		}
	}
}