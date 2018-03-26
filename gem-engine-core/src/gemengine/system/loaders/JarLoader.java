package gemengine.system.loaders;

import java.io.IOException;
import java.util.jar.JarFile;

import org.jsync.sync.ClassSync;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.AsynchronousAssetLoader;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;

import lombok.extern.log4j.Log4j2;

@SuppressWarnings("rawtypes")
/**
 * Code loader class. This generates an instance of
 * {@link org.jsync.sync.ClassSync} from a .class file
 * 
 * @author Dragos
 *
 * @param <T>
 */
@Log4j2
public class JarLoader extends AsynchronousAssetLoader<JarFile, JarLoader.JarParameter> {
	static public class JarParameter extends AssetLoaderParameters<JarFile> {
	}

	public JarLoader(FileHandleResolver resolver) {
		super(resolver);
	}

	@Override
	public Array<AssetDescriptor> getDependencies(String fileName, FileHandle file, JarParameter parameter) {
		return null;
	}

	@Override
	public void loadAsync(AssetManager manager, String fileName, FileHandle file, JarParameter parameter) {
	}

	@Override
	public JarFile loadSync(AssetManager manager, String fileName, FileHandle file, JarParameter parameter) {
		try {
			return new JarFile(fileName);
		} catch (IOException e) {
			throw new IllegalStateException(e.getMessage());
		}
	}
}
