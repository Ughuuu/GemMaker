package com.gemengine.system.loaders;

import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.assets.AssetManager;

import lombok.Getter;
import lombok.Setter;

/**
 * This is used to hold data about loaders.
 * 
 * @author Dragos
 *
 */
public class LoaderData<T> {
	@Getter
	private final Class<T> type;
	private final AssetLoaderParameters<T> assetLoaderParameters;
	@Setter
	@Getter
	private String folder;

	/**
	 * Construct a loader data instance with the type of the loaded asset.
	 * 
	 * @param type
	 *            The type of the loaded asset.
	 */
	public LoaderData(Class<T> type) {
		this.type = type;
		this.assetLoaderParameters = null;
	}

	/**
	 * Construct a loader data instance with the type of the loaded asset and
	 * the asset loader parameter of choice.
	 * 
	 * @param type
	 *            The type of the loaded asset.
	 * @param assetLoaderParameters
	 *            The asset loader parameter, which will be given every time a
	 *            new asset is loaded.
	 */
	public LoaderData(Class<T> type, AssetLoaderParameters<T> assetLoaderParameters) {
		this.type = type;
		this.assetLoaderParameters = assetLoaderParameters;
	}

	/**
	 * load a new asset using this loader data. Don't call this, this is called
	 * from Asset System.
	 * 
	 * @param assetManager
	 *            The asset manager
	 * @param path
	 *            The path.
	 */
	public void load(AssetManager assetManager, String path) {
		if (assetLoaderParameters != null) {
			assetManager.load(path, getType(), assetLoaderParameters);
		} else {
			assetManager.load(path, type);
		}
	}
}