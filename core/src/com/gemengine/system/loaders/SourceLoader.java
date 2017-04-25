package com.gemengine.system.loaders;

import org.jsync.sync.SourceSync;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.AsynchronousAssetLoader;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;
import com.gemengine.system.AssetSystem;
import com.gemengine.system.ManagerSystem;

@SuppressWarnings("rawtypes")
public class SourceLoader extends AsynchronousAssetLoader<SourceSync, SourceLoader.SourceParameter> {
	private final String sourceSystemFolder = AssetSystem.assetsFolder + ManagerSystem.systemSourceFolder;
	private final String sourceComponentFolder = AssetSystem.assetsFolder + ManagerSystem.componentSourceFolder;

	static public class SourceParameter extends AssetLoaderParameters<SourceSync> {
	}

	public SourceLoader(FileHandleResolver resolver) {
		super(resolver);
	}

	@Override
	public Array<AssetDescriptor> getDependencies(String fileName, FileHandle file, SourceParameter parameter) {
		return null;
	}

	@Override
	public void loadAsync(AssetManager manager, String fileName, FileHandle file, SourceParameter parameter) {
	}

	@Override
	public SourceSync loadSync(AssetManager manager, String fileName, FileHandle file, SourceParameter parameter) {
		String path = file.pathWithoutExtension();
		if (path.contains(sourceSystemFolder)) {
			path = file.pathWithoutExtension().substring(sourceSystemFolder.length());
			return new SourceSync(path.replace('/', '.'), sourceSystemFolder, sourceSystemFolder);
		} else if (path.contains(sourceComponentFolder)) {
			path = file.pathWithoutExtension().substring(sourceComponentFolder.length());
			return new SourceSync(path.replace('/', '.'), sourceComponentFolder, sourceComponentFolder);
		}
		return new SourceSync(path.replace('/', '.').substring(AssetSystem.assetsFolder.length()),
				AssetSystem.assetsFolder, AssetSystem.assetsFolder);
	}
}
