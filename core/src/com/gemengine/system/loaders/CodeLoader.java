package com.gemengine.system.loaders;

import org.jsync.sync.ClassSync;
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
public class CodeLoader<T> extends AsynchronousAssetLoader<ClassSync, CodeLoader.CodeParameter> {
	static public class CodeParameter extends AssetLoaderParameters<ClassSync> {
		private final ClassLoader classLoader;

		public CodeParameter(ClassLoader classLoader) {
			this.classLoader = classLoader;
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
		String path = file.pathWithoutExtension().substring(AssetSystem.assetsFolder.length()).replace('/', '.');
		return new ClassSync<T>(parameter.classLoader, path, AssetSystem.assetsFolder);
	}
}
