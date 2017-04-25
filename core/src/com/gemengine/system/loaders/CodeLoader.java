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
	private final String sourceSystemFolder = AssetSystem.assetsFolder + ManagerSystem.systemSourceFolder;
	private final String sourceComponentFolder = AssetSystem.assetsFolder + ManagerSystem.componentSourceFolder;

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
		String path = file.pathWithoutExtension();
		if (path.contains(sourceSystemFolder)) {
			path = file.pathWithoutExtension().substring(sourceSystemFolder.length());
			return new ClassSync<T>(parameter.classLoader, path.replace('/', '.'), sourceSystemFolder);
		} else if (path.contains(sourceComponentFolder)) {
			path = file.pathWithoutExtension().substring(sourceComponentFolder.length());
			return new ClassSync<T>(parameter.classLoader, path.replace('/', '.'), sourceComponentFolder);
		}
		return new ClassSync<T>(parameter.classLoader,
				path.replace('/', '.').substring(AssetSystem.assetsFolder.length()), AssetSystem.assetsFolder);
	}
}
