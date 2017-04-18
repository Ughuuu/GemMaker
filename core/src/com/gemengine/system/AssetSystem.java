package com.gemengine.system;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Collection;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import org.eclipse.jgit.diff.DiffEntry;
import org.jsync.sync.Commiter;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.AssetLoader;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.assets.loaders.resolvers.LocalFileHandleResolver;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;
import com.gemengine.system.base.TimedSystem;
import com.gemengine.system.helper.AssetSystemHelper;
import com.google.inject.Inject;
import com.google.inject.name.Named;

import lombok.NonNull;
import lombok.val;

public class AssetSystem extends TimedSystem {
	public final String assetsFolder = "assets/";
	private static final Map<String, Class<?>> extensionToLoaderMap = new HashMap<String, Class<?>>();
	private final Map<String, Boolean> unknownAssets = new HashMap<String, Boolean>();
	private final Map<String, List<String>> folderToAsset;
	private final AssetManager assetManager;
	private final Commiter commiter;
	
	private final boolean useBlockingLoad;
	private final boolean useExternalFiles;

	@Inject
	AssetSystem(@Named("useExternalFiles") boolean useExternalFiles,
			@Named("useBlockingLoad") boolean useBlockingLoad) {
		super(100, true, 0);
		this.useExternalFiles = useExternalFiles;
		if (useExternalFiles) {
			assetManager = new AssetManager(new InternalFileHandleResolver());
		} else {
			assetManager = new AssetManager(new LocalFileHandleResolver());
		}
		this.useBlockingLoad = useBlockingLoad;
		Commiter commiter = null;
		try {
			commiter = new Commiter(assetsFolder, "master");
		} catch (Exception e) {
			e.printStackTrace();
		}
		this.commiter = commiter;
		AssetSystemHelper.setDefaultLoaders(this);
		folderToAsset = new HashMap<String, List<String>>();
	}

	public void addExtensionMapping(Class<?> assetLoaderClass, String extension) {
		extensionToLoaderMap.put(extension, assetLoaderClass);
	}

	public <T, P extends AssetLoaderParameters<T>> void addLoaderDefault(Class<T> result, AssetLoader<T, P> assetLoader,
			String extension) {
		assetManager.setLoader(result, assetLoader);
		extensionToLoaderMap.put(extension, assetLoader.getClass());
	}

	public <T, P extends AssetLoaderParameters<T>> void addLoaderOverride(Class<T> result,
			AssetLoader<T, P> assetLoader, String extension) {
		assetManager.setLoader(result, extension, assetLoader);
		extensionToLoaderMap.put(extension, assetLoader.getClass());
	}

	public FileHandleResolver getFileHandleResolver() {
		return assetManager.getFileHandleResolver();
	}

	public boolean isAssetLoaded(String path) {
		return assetManager.isLoaded(path);
	}
	
	public <T> T[] getAll(Class<T> type){
		Array<T> elements = new Array<T>();
		assetManager.getAll(type, elements);
		return elements.items;
	}
	
	public <T> String getAssetFileName(T asset){
		return assetManager.getAssetFileName(asset);
	}
	
	public String[] getAssetFileName(){
		return assetManager.getAssetNames().items;
	}

	public float getProgress(){
		return assetManager.getProgress();
	}
	
	public <T> T getAsset(String path) {
		return assetManager.get(path);
	}
	
	public void unloadAssets(){
		assetManager.dispose();
	};

	@Override
	public void onInit() {
		if (useExternalFiles) {
			try {
				findExternalFiles();
				findExternalChanges();
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			findInternalFiles();
		}
		if (useBlockingLoad) {
			assetManager.finishLoading();
		}
	}

	@Override
	public void onUpdate(float delta) {
		if (useExternalFiles) {
			try {
				// always check file changes
				findExternalChanges();
				if (useBlockingLoad) {
					assetManager.finishLoading();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (!useBlockingLoad) {
				assetManager.update();
			}
		} else {
			// disable this if loading is done and not using external files
			setEnable(false);
		}
	}

	private void findExternalChanges() throws Exception {
		List<DiffEntry> entries = commiter.update();
		for (DiffEntry entry : entries) {
			String path;
			switch (entry.getChangeType()) {
			case ADD:
			case COPY:
				loadAsset(entry.getNewPath());
				break;
			case DELETE:
				unloadAsset(entry.getOldPath());
				break;
			case MODIFY:
			case RENAME:
				unloadAsset(entry.getOldPath());
				loadAsset(entry.getNewPath());
				break;
			}
		}
	}

	private void findExternalFiles() {
		try {
			for (String path : commiter.getFiles()) {
				loadAsset(path);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void findInternalFiles() {
		Queue<FileHandle> fileQueue = getInternalFilesHandleFrom(assetsFolder);
		FileHandle file;
		while ((file = fileQueue.poll()) != null) {
			if (file.isDirectory()) {
				fileQueue.addAll(getInternalFilesHandleFrom(file.path()));
			} else {
				String path = file.path();
				val loader = extensionToLoaderMap.get(file.extension());
				if (loader != null) {
					assetManager.load(path, loader);
					unknownAssets.put(path, false);
				} else {
					unknownAssets.put(path, true);
				}
			}
		}
	}

	private String getExtension(String path) {
		int extensionStart = path.lastIndexOf('.');
		if (extensionStart == -1)
			return "";
		return path.substring(extensionStart + 1);
	}

	private Deque<FileHandle> getInternalFilesHandleFrom(String path) {
		return new ArrayDeque<FileHandle>(Arrays.asList(Gdx.files.internal(path).list()));
	}

	private void loadAsset(@NonNull String path) {
		val loader = extensionToLoaderMap.get(getExtension(path));
		if (loader != null) {
			assetManager.load(path, loader);
			unknownAssets.put(path, false);
		} else {
			unknownAssets.put(path, true);
		}
	}

	private void unloadAsset(String path) {
		assetManager.unload(path);
		unknownAssets.remove(path);
	}
}
