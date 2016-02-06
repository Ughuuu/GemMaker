package com.ngeen.asset;

import com.ngeen.engine.Ngeen;

/**
 * The base class for assets. It is a generic class. You usualy get an asset of
 * a type from the asset factory located in your class extending Ngeen. You have
 * references to it in pretty much every class you work in(Script, etc.)
 * 
 * The types Asset can use are written in AssetFactory.
 * 
 * To get the data, use {@link #getData() getData}.
 * 
 * @author Dragos
 *
 */
public class Asset<T> {
	private static int _UniqueId = 0;
	private final T _Asset;
	private final String _Folder;
	private int _Id;
	private final String _Path;
	/**
	 * Unique id of this Asset. Not unique with other types. First id will be 0.
	 */
	private int _ResId;

	protected final Ngeen ng;

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
	public Asset(Ngeen ng, String _Path, T _Asset, int _ResId, String _Folder) {
		this.ng = ng;
		this._Path = _Path;
		this._Asset = _Asset;
		this._ResId = _ResId;
		this._Folder = _Folder;
		if (_Folder.equals("/")) {
			_Folder = "";
		}
		_Id = _UniqueId;
		_UniqueId++;
	}

	/**
	 * If you call this, and i don't suggest it, you will no longer have one
	 * asset. The best way to do this is to load/unload folders, so it is harder
	 * to make mistakes.
	 */
	public void dispose() {
		ng.Loader.unloadAsset(_Path, _ResId, _Folder);
	}

	/**
	 * Get the data held by the asset(Texture, Sound, etc.)
	 * 
	 * @return The data held by this asset.
	 */
	public final T getData() {
		return _Asset;
	}

	/**
	 * Get the folder holding this object, relative to your data folder, that is
	 * in your project classpath.
	 * 
	 * @return Folder path.
	 */
	public String getFolder() {
		return _Folder;
	}

	/**
	 * Get the id of the object.
	 */
	public final int getId() {
		return _Id;
	}

	/**
	 * Get the object path without the folder it is in. That is, the path
	 * relative to the folder _Folder path. Get the folder path with
	 * {@link #getFolder() getFolder}
	 * 
	 * @return The path to this asset relative to it's holding folder.
	 */
	public final String getPath() {
		return _Path;
	}
}
