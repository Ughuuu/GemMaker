package com.ngeen.asset;

import java.util.List;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.ngeen.engine.Ngeen;

/**
 * The base class for assets. Has to be extended.
 * 
 * @author Dragos
 *
 */
public class Asset<T> {
	/**
	 * Unique id of this Asset. Not unique with other types. First id will be 0.
	 */
	private int _ResId;
	private int _Id;
	protected final Ngeen ng;
	private final String _Path;
	private final T _Asset;
	private final String _Folder;

	private static int _UniqueId = 0;

	public Asset(Ngeen ng, String _Path, T _Asset, int _ResId, String _Folder) {
		this.ng = ng;
		this._Path = _Path;
		this._Asset = _Asset;
		this._ResId = _ResId;
		this._Folder = _Folder;
		if(_Folder.equals("/")){
			_Folder = "";
		}
		_Id = _UniqueId;
		_UniqueId++;
	}

	public final T getData() {
		return _Asset;
	}

	public void dispose() {
		ng.Loader.unloadAsset(_Path, _ResId, _Folder);
	}

	/**
	 * Get the id of the object.
	 */
	public final int getId() {
		return _Id;
	}

	public final String getPath() {
		return _Path;
	}
	
	public String getFolder(){
		return _Folder;
	}
}
