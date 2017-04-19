package com.gemengine.system;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import org.eclipse.jgit.diff.DiffEntry;
import org.jsync.sync.Commiter;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.AssetLoader;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.assets.loaders.resolvers.LocalFileHandleResolver;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Logger;
import com.gemengine.system.base.TimedSystem;
import com.gemengine.system.helper.AssetSystemHelper;
import com.google.inject.Inject;
import com.google.inject.name.Named;

import lombok.NonNull;
import lombok.val;

public class AssetSystem extends TimedSystem {
	public final String assetsFolder = "assets/";
	private List<String> loadFolders;
	private static final Map<String, Class<?>> extensionToTypeMap = new HashMap<String, Class<?>>();
	private final Map<String, List<String>> folderToAsset;
	private final Map<String, String> assetToFolder;
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
			assetManager = new AssetManager(new InternalFileHandleResolver(), false);
		} else {
			assetManager = new AssetManager(new LocalFileHandleResolver(), false);
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
		assetToFolder = new HashMap<String, String>();
		loadFolders = new ArrayList<String>();
		loadFolders.add(null);
	}

	public void addExtensionMapping(Class<?> assetTypeClass, String extension) {
		extensionToTypeMap.put(extension, assetTypeClass);
	}

	public <T, P extends AssetLoaderParameters<T>> void addLoaderDefault(Class<T> result, AssetLoader<T, P> assetLoader,
			String extension) {
		assetManager.setLoader(result, assetLoader);
		addExtensionMapping(result, extension);
	}

	public <T, P extends AssetLoaderParameters<T>> void addLoaderOverride(Class<T> result,
			AssetLoader<T, P> assetLoader, String extension) {
		assetManager.setLoader(result, extension, assetLoader);
		addExtensionMapping(result, extension);
	}

	public FileHandleResolver getFileHandleResolver() {
		return assetManager.getFileHandleResolver();
	}

	public boolean isAssetLoaded(String path) {
		return assetManager.isLoaded(path);
	}

	public <T> T[] getAll(Class<T> type) {
		Array<T> elements = new Array<T>();
		assetManager.getAll(type, elements);
		return elements.toArray(type);
	}

	public void loadFolder(String folder) {
		if (loadFolders.contains(folder)) {
			return;
		}
		setEnable(true);
		loadFolders.add(folder);
		if (folderToAsset.get(folder) == null) {
			return;
		}
		for (String path : folderToAsset.get(folder)) {
			loadAsset(path);
		}
	}

	public void unloadFolder(String folder) {
		if (!loadFolders.contains(folder)) {
			return;
		}
		loadFolders.remove(folder);
		if (folderToAsset.get(folder) == null) {
			return;
		}
		for (String path : folderToAsset.get(folder)) {
			unloadAsset(path);
		}
	}

	public <T> String getAssetFileName(T asset) {
		return assetManager.getAssetFileName(asset);
	}

	public String[] getAssetNames() {
		return assetManager.getAssetNames().toArray(String.class);
	}

	public float getProgress() {
		return assetManager.getProgress();
	}

	public <T> T getAsset(String path) {
		return assetManager.get(path);
	}

	public void unloadAssets() {
		assetManager.dispose();
	};

	private void loadFolder() {
		if (useExternalFiles) {
			try {
				findExternalFiles();
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			findInternalFiles();
		}
		if (useBlockingLoad) {
			assetManager.finishLoading();
			if (!useExternalFiles) {
				setEnable(false);
			}
		}
	}

	@Override
	public void onInit() {
		loadFolder();
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
		} else {
			if (assetManager.update()) {
				setEnable(false);
			}
		}
		if (!useBlockingLoad) {
			assetManager.update();
		}
	}

	private void findExternalChanges() throws Exception {
		List<DiffEntry> entries = commiter.update();
		for (DiffEntry entry : entries) {
			for (String loadFolder : loadFolders) {
				switch (entry.getChangeType()) {
				case ADD:
				case COPY:
					placeAsset(assetsFolder + entry.getNewPath(), loadFolder);
					break;
				case DELETE:
					unplaceAsset(assetsFolder + entry.getOldPath(), loadFolder);
					break;
				case MODIFY:
				case RENAME:
					unplaceAsset(assetsFolder + entry.getOldPath(), loadFolder);
					placeAsset(assetsFolder + entry.getNewPath(), loadFolder);
					break;
				}
			}
		}
	}

	private void findExternalFiles() {
		try {
			for (String path : commiter.getFiles()) {
				for (String loadFolder : loadFolders) {
					placeAsset(assetsFolder + path, loadFolder);
				}
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
				val type = extensionToTypeMap.get(file.extension());
				if (type != null) {
					for (String loadFolder : loadFolders) {
						placeAsset(path, loadFolder);
					}
				}
			}
		}
	}

	private String getExtension(String path) {
		int extensionStart = path.lastIndexOf('.');
		if (extensionStart == -1)
			return "";
		return path.substring(extensionStart);
	}

	private Deque<FileHandle> getInternalFilesHandleFrom(String path) {
		return new ArrayDeque<FileHandle>(Arrays.asList(Gdx.files.internal(path).list()));
	}

	private void loadAsset(String path) {
		String extension = getExtension(path);
		val type = extensionToTypeMap.get(extension);
		if (type != null) {
			assetManager.load(path, type);
		}
	}

	private void placeAsset(@NonNull String path, String loadFolder) {
		String folder = assetToFolder.get(path);
		if (folder == null) {
			int folderPos = path.indexOf('/', assetsFolder.length());
			if (folderPos == -1) {
				folderPos = assetsFolder.length();
			}
			folder = path.substring(0, folderPos);
			assetToFolder.put(path, folder);
			List<String> filesInFolder = folderToAsset.get(folder);
			if (filesInFolder == null) {
				filesInFolder = new ArrayList<String>();
				folderToAsset.put(folder, filesInFolder);
			}
			filesInFolder.add(path);
		}
		if (loadFolder == null || folder.indexOf(loadFolder) == -1) {
			return;
		}
		loadAsset(path);
	}

	private void unplaceAsset(@NonNull String path, String loadFolder) {
		String folder = assetToFolder.get(path);
		if (folder == null || folder.indexOf(loadFolder) == -1) {
			return;
		}
		unloadAsset(path);
	}

	private void unloadAsset(String path) {
		assetManager.unload(path);
	}
}
