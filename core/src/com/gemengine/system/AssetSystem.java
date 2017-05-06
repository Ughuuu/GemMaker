package com.gemengine.system;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import org.apache.logging.log4j.MarkerManager;
import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.diff.DiffEntry.ChangeType;
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
import com.gemengine.system.base.AssetListener;
import com.gemengine.system.base.TimedSystem;
import com.gemengine.system.helper.AssetSystemHelper;
import com.gemengine.system.helper.Messages;
import com.gemengine.system.loaders.LoaderData;
import com.google.inject.Inject;
import com.google.inject.name.Named;

import lombok.NonNull;
import lombok.val;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class AssetSystem extends TimedSystem {
	private static final Map<String, List<LoaderData>> extensionToLoaderMap = new HashMap<String, List<LoaderData>>();
	public final static String assetsFolder = Messages.getString("AssetSystem.AssetsFolder"); //$NON-NLS-1$
	private List<String> loadFolders;
	private final Map<String, List<String>> folderToAsset;
	private final Map<String, String> assetToFolder;
	private final AssetManager assetManager;
	private final Commiter commiter;
	private final boolean useBlockingLoad;
	private final boolean useExternalFiles;
	private final Set<AssetListener> assetListeners;

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
			commiter = new Commiter(assetsFolder, Messages.getString("AssetSystem.GitBranch")); //$NON-NLS-1$
		} catch (Throwable t) {
			log.fatal(MarkerManager.getMarker("gem"), "AssetSystem fail git client", t);
		}
		this.commiter = commiter;
		if (useDefaultLoaders) {
			AssetSystemHelper.setDefaultLoaders(this);
		}
		folderToAsset = new HashMap<String, List<String>>();
		assetToFolder = new HashMap<String, String>();
		loadFolders = new ArrayList<String>();
		assetListeners = new HashSet<AssetListener>();
		loadFolders.add(null);
		loadFolder();
	}

	public void addAssetListener(AssetListener assetListener) {
		assetListeners.add(assetListener);
	}

	public <T, P extends AssetLoaderParameters<T>> void addLoaderDefault(LoaderData loaderData, String folder,
			AssetLoader<T, P> assetLoader, String... extensions) {
		loaderData.setFolder(folder);
		assetManager.setLoader(loaderData.getType(), assetLoader);
		for (String extension : extensions) {
			addExtensionMapping(loaderData, extension);
		}
	}

	public <T, P extends AssetLoaderParameters<T>> void addLoaderOverride(LoaderData loaderData, String folder,
			AssetLoader<T, P> assetLoader, String... extensions) {
		loaderData.setFolder(folder);
		for (String extension : extensions) {
			assetManager.setLoader(loaderData.getType(), extension, assetLoader);
			addExtensionMapping(loaderData, extension);
		}
	}

	public <T> T[] all(Class<T> type) {
		Array<T> elements = new Array<T>();
		assetManager.getAll(type, elements);
		return elements.toArray(type);
	}

	public <T> T asset(String path) {
		return assetManager.get(path);
	}

	public <T> String assetFileName(T asset) {
		return assetManager.getAssetFileName(asset);
	}

	public String[] assetNames() {
		return assetManager.getAssetNames().toArray(String.class);
	}

	public FileHandleResolver fileHandleResolver() {
		return assetManager.getFileHandleResolver();
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

	public float progress() {
		return assetManager.getProgress();
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

	private void findExternalChanges() throws Exception {
		List<DiffEntry> entries = commiter.update();
		for (DiffEntry entry : entries) {
			String oldPath = assetsFolder + entry.getOldPath();
			String newPath = assetsFolder + entry.getNewPath();
			switch (entry.getChangeType()) {
			case DELETE:
				unplaceAsset(oldPath);
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
			for (val assetListener : assetListeners) {
				assetListener.onChange(entry.getChangeType(), oldPath, newPath);
			}
		}
	}

	private void findExternalFiles() {
		try {
			for (String path : commiter.getFiles()) {
				for (String loadFolder : loadFolders) {
					placeAsset(assetsFolder + path, loadFolder);
					for (val assetListener : assetListeners) {
						assetListener.onChange(ChangeType.ADD, assetsFolder + path, assetsFolder + path);
					}
				}
			}
		} catch (Throwable t) {
			log.fatal(MarkerManager.getMarker("gem"), "AssetSystem find external files", t);
		}
	}

	private void findInternalFiles() {
		Queue<FileHandle> fileQueue = internalFilesHandleFrom(assetsFolder);
		FileHandle file;
		while ((file = fileQueue.poll()) != null) {
			if (file.isDirectory()) {
				fileQueue.addAll(internalFilesHandleFrom(file.path()));
			} else {
				String path = file.path();
				val type = extensionToLoaderMap.get(file.extension());
				if (type != null) {
					for (String loadFolder : loadFolders) {
						placeAsset(path, loadFolder);
						for (val assetListener : assetListeners) {
							assetListener.onChange(ChangeType.ADD, path, path);
						}
					}
				}
			}
		}
	}

	private String getLastFolder(String path) {
		int folderPosLast = path.lastIndexOf(Messages.getString("AssetSystem.FileSeparator")); //$NON-NLS-1$
		int folderPosFirst = path.indexOf(Messages.getString("AssetSystem.FileSeparator")); //$NON-NLS-1$
		return path.substring(folderPosFirst + 1, folderPosLast + 1);
	}

	private Deque<FileHandle> internalFilesHandleFrom(String path) {
		return new ArrayDeque<FileHandle>(Arrays.asList(Gdx.files.internal(path).list()));
	}

	private void loadAsset(String path) {
		String extension = AssetSystemHelper.getExtension(path);
		val types = extensionToLoaderMap.get(extension);
		if (types == null)
			return;
		final String folder = getLastFolder(path);
		LoaderData defaultLoaderData = null;
		for (LoaderData loaderData : types) {
			String loaderFolder = loaderData.getFolder();
			if (loaderFolder == null) {
				defaultLoaderData = loaderData;
				continue;
			}
			if (loaderData.getFolder().equals(folder)) {
				loaderData.load(assetManager, path);
				log.debug(MarkerManager.getMarker("gem"), "Asset System load file {} ---> {}", path,
						loaderData.getType().getName());
				return;
			}
		}
		if (defaultLoaderData != null) {
			defaultLoaderData.load(assetManager, path);
			log.debug(MarkerManager.getMarker("gem"), "Asset System load file {} ---> {}", path,
					defaultLoaderData.getType().getName());
		} else {
			log.warn(MarkerManager.getMarker("gem"), "Asset System no loader for file {} {}", path, types);
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
			int folderPos = path.lastIndexOf(Messages.getString("AssetSystem.FileSeparator")); //$NON-NLS-1$
			folder = path.substring(0, folderPos + 1);
			assetToFolder.put(path, folder);
			StringBuilder sb = new StringBuilder();
			for (String subFolder : folder.split(Messages.getString("AssetSystem.FileSeparator"))) { //$NON-NLS-1$
				sb.append(subFolder);
				sb.append(Messages.getString("AssetSystem.FileSeparator")); //$NON-NLS-1$
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
		if (isAssetLoaded(path)) {
			assetManager.unload(path);
			log.debug(MarkerManager.getMarker("gem"), "Asset System unload {}", path);
		}
	}

	private void unplaceAsset(@NonNull String path) {
		String folder = assetToFolder.remove(path);
		if (folder == null) {
			return;
		}
		StringBuilder sb = new StringBuilder();
		for (String subFolder : folder.split(Messages.getString("AssetSystem.FileSeparator"))) { //$NON-NLS-1$
			sb.append(subFolder);
			sb.append(Messages.getString("AssetSystem.FileSeparator")); //$NON-NLS-1$
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
