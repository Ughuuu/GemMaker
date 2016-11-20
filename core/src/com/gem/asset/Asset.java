package com.gem.asset;

import com.gem.engine.Gem;

import lombok.Getter;

/**
 * The base class for assets. It is a generic class. You usualy get an asset of
 * a type from the asset factory located in your class extending Ngeen. You have
 * references to it in pretty much every class you work in(Script, etc.)
 * <p>
 * The types Asset can use are written in AssetFactory.
 * <p>
 * To get the data, use {@link #getAsset() getData}.
 *
 * @author Dragos
 */
public class Asset<T> {
	private static int UniqueId = 0;
	protected final Gem gem;
	@Getter
	private final T Asset;
	@Getter
	private final String Folder;
	@Getter
	private final String Path;
	/**
	 * Unique id of this Asset. Not unique with other types. First id will be 0.
	 */
	@Getter
	private int Id;
	@Getter
	private int ResId;

	/**
	 * This constructs an Asset. Do not use unsupervised. This method is called
	 * by a factory class.
	 *
	 * @param ng
	 *            -
	 * @param _Path
	 *            -
	 * @param _Asset
	 *            -
	 * @param _ResId
	 *            -
	 * @param _Folder
	 *            -
	 */
	public Asset(Gem ng, String _Path, T _Asset, int _ResId, String _Folder) {
		this.gem = ng;
		this.Path = _Path;
		this.Asset = _Asset;
		this.ResId = _ResId;
		this.Folder = _Folder;
		if (_Folder.equals("/")) {
			_Folder = "";
		}
		Id = UniqueId;
		UniqueId++;
	}

	/**
	 * If you call this, and i don't suggest it, you will no longer have one
	 * asset. The best way to do this is to load/unload folders, so it is harder
	 * to make mistakes.
	 */
	public void dispose() {
		gem.Loader.unloadAsset(Path, ResId, Folder);
	}

	public void reload() {
		gem.Loader.unloadAsset(Path, ResId, Folder);
		gem.Loader.loadAsset(Path, ResId);
	}
}
