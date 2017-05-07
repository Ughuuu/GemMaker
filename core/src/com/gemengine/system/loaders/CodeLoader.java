package com.gemengine.system.loaders;

import org.jsync.sync.ClassSync;

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
/**
 * Code loader class. This generates an instance of
 * {@link org.jsync.sync.ClassSync} from a .class file
 * 
 * @author Dragos
 *
 * @param <T>
 */
public class CodeLoader<T> extends AsynchronousAssetLoader<ClassSync, CodeLoader.CodeParameter> {
	static public class CodeParameter extends AssetLoaderParameters<ClassSync> {
		private final ClassLoader classLoader;

		public CodeParameter(ClassLoader classLoader) {
			this.classLoader = classLoader;
		}
	}

	private static final String completeCodeFolder = AssetSystem.assetsFolder + ManagerSystem.codeFolder;

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
		String completePath = file.pathWithoutExtension();
		String folder = AssetSystem.assetsFolder;
		if (completePath.contains(completeCodeFolder)) {
			folder = completeCodeFolder;
		}
		String path = completePath.substring(folder.length()).replace('/', '.');
		return new ClassSync<T>(parameter.classLoader, path, folder);
	}
}
