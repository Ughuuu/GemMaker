package com.gemengine.system;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
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

import lombok.Getter;
import lombok.NonNull;
import lombok.Synchronized;
import lombok.val;

public class AssetSystem extends TimedSystem {
	private static final Map<String, List<LoaderData>> extensionToLoaderMap = new HashMap<String, List<LoaderData>>();
	private static final Map<String, LoaderData> folderToLoaderMap = new HashMap<String, LoaderData>();
	public final static String assetsFolder = "assets/";
	private List<String> loadFolders;
	private final Map<String, List<String>> folderToAsset;
	private final Map<String, String> assetToFolder;
	private final AssetManager assetManager;
	private final Commiter commiter;

	private final boolean useBlockingLoad;
	private final boolean useExternalFiles;

	public static class LoaderData {
		private final Class<?> type;
		private final AssetLoaderParameters<?> assetLoaderParameters;

		public <T> Class<T> getType() {
			return (Class<T>) type;
		}

		public LoaderData(Class<?> type) {
			this.type = type;
			this.assetLoaderParameters = null;
		}

		public LoaderData(Class<?> type, AssetLoaderParameters<?> assetLoaderParameters) {
			this.type = type;
			this.assetLoaderParameters = assetLoaderParameters;
		}

		public void load(AssetManager assetManager, String path) {
			if (assetLoaderParameters != null) {
				assetManager.load(path, getType(), assetLoaderParameters);
			} else {
				assetManager.load(path, type);
			}
		}
	}

	@Inject
	AssetSystem(@Named("useExternalFiles") boolean useExternalFiles, @Named("useBlockingLoad") boolean useBlockingLoad,
			@Named("useDefaultLoaders") boolean useDefaultLoaders) {
		super(300, true, 0);
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
		if (useDefaultLoaders) {
			AssetSystemHelper.setDefaultLoaders(this);
		}
		folderToAsset = new HashMap<String, List<String>>();
		assetToFolder = new HashMap<String, String>();
		loadFolders = new ArrayList<String>();
		loadFolders.add(null);
	}

	public <T, P extends AssetLoaderParameters<T>> void addLoaderDefault(LoaderData loaderData,
			AssetLoader<T, P> assetLoader, String... extensions) {
		assetManager.setLoader(loaderData.getType(), assetLoader);
		for (String extension : extensions) {
			addExtensionMapping(loaderData, extension);
		}
	}

	public <T, P extends AssetLoaderParameters<T>> void addLoaderOverride(LoaderData loaderData,
			AssetLoader<T, P> assetLoader, String... extensions) {
		for (String extension : extensions) {
			assetManager.setLoader(loaderData.getType(), extension, assetLoader);
			addExtensionMapping(loaderData, extension);
		}
	}

	public void addTypeFolder(LoaderData loaderData, String folder) {
		folderToLoaderMap.put(folder, loaderData);
	}

	public <T> T[] getAll(Class<T> type) {
		Array<T> elements = new Array<T>();
		assetManager.getAll(type, elements);
		return elements.toArray(type);
	}

	public <T> T getAsset(String path) {
		return assetManager.get(path);
	}

	public <T> String getAssetFileName(T asset) {
		return assetManager.getAssetFileName(asset);
	}

	public String[] getAssetNames() {
		return assetManager.getAssetNames().toArray(String.class);
	}

	public FileHandleResolver getFileHandleResolver() {
		return assetManager.getFileHandleResolver();
	}

	public float getProgress() {
		return assetManager.getProgress();
	}

	public boolean isAssetLoaded(String path) {
		return assetManager.isLoaded(path);
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
		if (useBlockingLoad) {
			assetManager.finishLoading();
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

	public void unloadAssets() {
		assetManager.dispose();
	};

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
		if (useBlockingLoad) {
			assetManager.finishLoading();
		}
	}

	private void addExtensionMapping(LoaderData loaderData, String extension) {
		List<LoaderData> types = extensionToLoaderMap.get(extension);
		if (types == null) {
			types = new ArrayList<LoaderData>();
			extensionToLoaderMap.put(extension, types);
		}
		types.add(loaderData);
	}

	@Synchronized
	private void findExternalChanges() throws Exception {
		List<DiffEntry> entries = commiter.update();
		for (DiffEntry entry : entries) {
			switch (entry.getChangeType()) {
			case DELETE:
				unplaceAsset(assetsFolder + entry.getOldPath());
				break;
			case MODIFY:
			case RENAME:
				unplaceAsset(assetsFolder + entry.getOldPath());
				break;
			default:
				break;
			}
			for (String loadFolder : loadFolders) {
				switch (entry.getChangeType()) {
				case ADD:
				case COPY:
					placeAsset(assetsFolder + entry.getNewPath(), loadFolder);
					break;
				case MODIFY:
				case RENAME:
					placeAsset(assetsFolder + entry.getNewPath(), loadFolder);
					break;
				default:
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
				val type = extensionToLoaderMap.get(file.extension());
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

	private String getLastFolder(String path) {
		int folderPosLast = path.lastIndexOf('/');
		int folderPosFirst = path.indexOf('/');
		return path.substring(folderPosFirst + 1, folderPosLast + 1);
	}

	private void loadAsset(String path) {
		String extension = getExtension(path);
		val types = extensionToLoaderMap.get(extension);
		if (types == null)
			return;
		if (types.size() != 1) {
			final String folder = getLastFolder(path);
			LoaderData loaderData = folderToLoaderMap.get(folder);
			if (loaderData != null) {
				loaderData.load(assetManager, path);
			}
		} else if (types.size() == 1) {
			types.get(0).load(assetManager, path);
		}
	}

	private void loadFolder() {
		if (useExternalFiles) {
			try {
				findExternalChanges();
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

	private void placeAsset(@NonNull String path, String loadFolder) {
		String folder = assetToFolder.get(path);
		if (folder == null) {
			int folderPos = path.lastIndexOf('/');
			folder = path.substring(0, folderPos + 1);
			assetToFolder.put(path, folder);
			StringBuilder sb = new StringBuilder();
			for (String subFolder : folder.split("/")) {
				sb.append(subFolder);
				sb.append('/');
				String subfolderFullPath = sb.toString();
				List<String> filesInFolder = folderToAsset.get(subfolderFullPath);
				if (filesInFolder == null) {
					filesInFolder = new ArrayList<String>();
					folderToAsset.put(subfolderFullPath, filesInFolder);
				}
				filesInFolder.add(path);
			}
		}
		if (loadFolder == null || folder.indexOf(loadFolder) == -1) {
			return;
		}
		loadAsset(path);
	}

	private void unloadAsset(String path) {
		if (isAssetLoaded(path))
			assetManager.unload(path);
	}

	private void unplaceAsset(@NonNull String path) {
		String folder = assetToFolder.remove(path);
		if (folder == null) {
			return;
		}
		StringBuilder sb = new StringBuilder();
		for (String subFolder : folder.split("/")) {
			sb.append(subFolder);
			sb.append('/');
			String subfolderFullPath = sb.toString();
			List<String> filesInFolder = folderToAsset.get(subfolderFullPath);
			filesInFolder.remove(path);
			if (filesInFolder.size() == 0) {
				folderToAsset.remove(subfolderFullPath);
			}
		}
		unloadAsset(path);
	}
}
