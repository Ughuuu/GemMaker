package com.gemengine.system.loaders;

import org.jsync.sync.ClassSync;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.AsynchronousAssetLoader;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;

@SuppressWarnings("rawtypes")
public class CodeLoader<T> extends AsynchronousAssetLoader<ClassSync, CodeLoader.CodeParameter> {
	static public class CodeParameter extends AssetLoaderParameters<ClassSync> {
		private final ClassLoader classLoader;
		private final String destinationFolder;

		public CodeParameter(ClassLoader classLoader, String destinationFolder) {
			this.classLoader = classLoader;
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
	public ClassSync<T> loadSync(AssetManager manager, String fileName, FileHandle file, CodeParameter parameter) {
		String path = file.pathWithoutExtension();
		return new ClassSync<T>(parameter.classLoader, path.replace('/', '.'), parameter.destinationFolder);
	}
}
