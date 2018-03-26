package gemengine.system.loaders;

import gemengine.system.AssetSystem;
import gemengine.system.ManagerSystem;
import org.jsync.sync.SourceSync;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.AsynchronousAssetLoader;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;

@SuppressWarnings("rawtypes")
/**
 * Source loader class. This generates an instance of
 * {@link org.jsync.sync.SourceSync} from a .java file
 * 
 * @author Dragos
 *
 * @param <T>
 */
public class SourceLoader extends AsynchronousAssetLoader<SourceSync, SourceLoader.SourceParameter> {
	static public class SourceParameter extends AssetLoaderParameters<SourceSync> {
	}

	private static final String completeCodeFolder = AssetSystem.assetsFolder + ManagerSystem.codeFolder;

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
		String completePath = file.pathWithoutExtension();
		String folder = AssetSystem.assetsFolder;
		if (completePath.contains(completeCodeFolder)) {
			folder = completeCodeFolder;
		}
		String path = file.pathWithoutExtension().substring(folder.length()).replace('/', '.');
		return new SourceSync(path, folder, folder);
	}
}
