package com.gemengine.system.loaders;

import org.jsync.sync.SourceSync;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.AsynchronousAssetLoader;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;

@SuppressWarnings("rawtypes")
public class SourceLoader extends AsynchronousAssetLoader<SourceSync, SourceLoader.SourceParameter> {
	static public class SourceParameter extends AssetLoaderParameters<SourceSync> {
		private final String destinationFolder;
		private final String sourceFolder;

		public SourceParameter(String sourceFolder, String destinationFolder) {
			this.sourceFolder = sourceFolder;
			this.destinationFolder = destinationFolder;
		}
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
		return new SourceSync(path.replace('/', '.'), parameter.sourceFolder, parameter.destinationFolder);
	}
}
