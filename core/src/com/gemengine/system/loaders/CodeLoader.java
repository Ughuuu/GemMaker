package com.gemengine.system.loaders;

import org.jsync.sync.Sync;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.AsynchronousAssetLoader;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;

public class CodeLoader<T> extends AsynchronousAssetLoader<Sync, CodeLoader.CodeParameter> {
	static public class CodeParameter extends AssetLoaderParameters<Sync> {
		private final ClassLoader classLoader;
		private final String destinationFolder;
		private final String sourceFolder;

		public CodeParameter(ClassLoader classLoader, String sourceFolder, String destinationFolder) {
			this.classLoader = classLoader;
			this.sourceFolder = sourceFolder;
			this.destinationFolder = destinationFolder;
		}
	}

	public CodeLoader(FileHandleResolver resolver) {
		super(resolver);
	}

	@Override
	public Array<AssetDescriptor> getDependencies(String fileName, FileHandle file, CodeParameter parameter) {
		return null;
	}

	@Override
	public void loadAsync(AssetManager manager, String fileName, FileHandle file, CodeParameter parameter) {
	}

	@Override
	public Sync<T> loadSync(AssetManager manager, String fileName, FileHandle file, CodeParameter parameter) {
		String path = file.pathWithoutExtension();
		return new Sync<T>(parameter.classLoader, path.replace('/', '.'), parameter.sourceFolder,
				parameter.destinationFolder);
	}
}
