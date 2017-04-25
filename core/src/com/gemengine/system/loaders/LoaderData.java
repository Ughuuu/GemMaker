package com.gemengine.system.loaders;

import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.assets.AssetManager;

public class LoaderData {
	private final Class<?> type;
	private final AssetLoaderParameters<?> assetLoaderParameters;

	public LoaderData(Class<?> type) {
		this.type = type;
		this.assetLoaderParameters = null;
	}

	public LoaderData(Class<?> type, AssetLoaderParameters<?> assetLoaderParameters) {
		this.type = type;
		this.assetLoaderParameters = assetLoaderParameters;
	}

	@SuppressWarnings("unchecked")
	public <T> Class<T> getType() {
		return (Class<T>) type;
	}

	public void load(AssetManager assetManager, String path) {
		if (assetLoaderParameters != null) {
			assetManager.load(path, getType(), assetLoaderParameters);
		} else {
			assetManager.load(path, type);
		}
	}
}