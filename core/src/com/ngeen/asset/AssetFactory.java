package com.ngeen.asset;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.ngeen.debug.Debugger;
import com.ngeen.engine.Ngeen;

public class AssetFactory {
	private final Ngeen _Ng;

	/**
	 * 0 - texture 1 - texture atlas 2 - fonts 3 - sound 4 - music 5 - shader
	 */
	private final String[][] _Types = { { "png", "jpg", "jpeg", "gif" }, { "pack", "atlas" }, { "fnt" },
			{ "wav", "ogg", "mp3" }, { "wav", "ogg", "mp3" }, { ".vert" } };

	private final Class<?>[] _ClassType = { Texture.class, TextureAtlas.class, BitmapFont.class, Sound.class,
			Music.class, ShaderProgram.class };

	/**
	 * All underlying assets are kept here.
	 */
	private AssetManager _Manager;

	private List<String> _UpdateFolders;

	/**
	 * If this factory still loads objects or not.
	 */
	private boolean _Loading = false;

	private Map<Integer, Asset> _AssetMap;

	private Map<String, Integer> _AssetNameMap;

	/**
	 * Map from Stage Name Folder(Ex. LoadStage, GameStage, etc.) in data to All
	 * the files in it. The string also has an index number to follow it,
	 * representing the Types it holds. Ex LoadStage0, LoadStage1..
	 */
	private Map<String, List<String>> _Folders = new HashMap<String, List<String>>();

	public AssetFactory(Ngeen ng) {
		_Ng = ng;
		_Manager = new AssetManager();
		_AssetMap = new HashMap<Integer, Asset>();
		_AssetNameMap = new HashMap<String, Integer>();
		_UpdateFolders = new ArrayList<String>();
	}

	/**
	 * Check if given file is of specific given type. This is done only when
	 * specified. Will not be done in Release mode.
	 * 
	 * @param name
	 *            Name of the file
	 * @param i
	 *            Index of _Types array of arrays.
	 * @return If it is of specified type or not.
	 */
	private boolean checkExt(String name, int i) {
		for (int j = 0; j < _Types[i].length; j++) {
			if (name.compareTo(_Types[i][j]) == 0 || name.compareTo(_Types[i][j].toUpperCase()) == 0) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Get the coresponding index for the given file extension.
	 * 
	 * @param name
	 *            File extension.
	 * @return Coresponding index in data types table.
	 */
	private int getExt(String name) {
		AssetManager a;
		for (int i = 0; i < _Types.length; i++) {
			if (checkExt(name, i) == true)
				return i;
		}
		return -1;
	}

	private void loadAsset(String path, int resType) {
		if (resType == 5) {
			path = (path).substring(0, (path).length() - 5);
		}
		_Manager.load(path, _ClassType[resType]);
	}

	public void unloadAsset(String path, int resType, String folder) {
		if (resType == 5) {
			path = (path).substring(0, (path).length() - 5);
		}
		try {
			_Manager.unload(path);
			_Folders.get(folder + resType).remove(path);
		} catch (Exception e) {
			Debugger.println(path + " is not loaded.");// Known bug. Sorry.
		}
	}

	public void scoutFiles() {
		_UpdateFolders.add("");
		FileHandle dirHandle;
		String prePath = "data/";
		String folder = "", actualFolder = "";

		// Desktop or Android. IOS or Windows Phone not tested.
		if (Gdx.app.getType() == ApplicationType.Android) {
			dirHandle = Gdx.files.internal(prePath);
		} else {
			prePath = "./bin/data/";
			dirHandle = Gdx.files.internal(prePath);
		}

		for (FileHandle entry : dirHandle.list()) {
			String name = entry.name();
			String extension = entry.extension();

			if (entry.isDirectory()) {// this has to be a class name.
				enqueFolder(name, name);
			} else {
				int extensionKey = getExt(extension);
				List<String> list = null;
				if (_Folders.containsKey(folder + extensionKey) == false) {
					list = new ArrayList<String>();
					_Folders.put(folder + extensionKey, list);
				}else{
					list = _Folders.get(folder + extensionKey);
				}
				loadAsset(prePath + name, extensionKey);
				list.add(name);
			}
		}
	}

	/**
	 * Adds folder to the map. Can be used to load or save later.
	 * 
	 * @param folder
	 */
	public void enqueFolder(String folder, String actualFolder) {
		_UpdateFolders.add(folder);
		FileHandle dirHandle;
		String prePath = "data/" + actualFolder;

		// Desktop or Android. IOS or Windows Phone not tested.
		if (Gdx.app.getType() == ApplicationType.Android) {
			dirHandle = Gdx.files.internal(prePath);
		} else {
			prePath = "./bin/data/" + actualFolder;
			dirHandle = Gdx.files.internal(prePath);
		}

		for (FileHandle entry : dirHandle.list()) {
			String name = entry.name();
			String extension = entry.extension();

			if (entry.isDirectory()) {// this has to be a class name.
				enqueFolder(folder, actualFolder + "/" + name);
			} else {
				int extensionKey = getExt(extension);
				List<String> list = null;
				if (_Folders.containsKey(folder + extensionKey) == false) {
					list = new ArrayList<String>();
					_Folders.put(folder + extensionKey, list);
				}else{
					list = _Folders.get(folder + extensionKey);
				}
				loadAsset(prePath + "/" + name, extensionKey);
				list.add(actualFolder + "/" + name);
			}
		}
	}

	/**
	 * Loads a folder previously enqued.
	 * 
	 * @param folder
	 *            Folder name that is in the map.
	 */
	public void loadFolder(String folder) {
		String prePath = "";

		// Desktop or Android. IOS or Windows Phone not tested.
		if (Gdx.app.getType() == ApplicationType.Android) {
			prePath = "data/";
		} else {
			prePath = "./bin/data/";
		}

		for (int i = 0; i < _Types.length; i++) {
			List<String> names = _Folders.get(folder + i);
			if (names != null) {
				for (String name : names) {
					loadAsset(prePath + name, i);
				}
			}
		}
	}

	private void addAssets(String folder) {
		String prePath = "";

		// Desktop or Android. IOS or Windows Phone not tested.
		if (Gdx.app.getType() == ApplicationType.Android) {
			prePath = "data/";
		} else {
			prePath = "./bin/data/";
		}

		for (int i = 0; i < _Types.length; i++) {
			List<String> names = _Folders.get(folder + i);
			if (names != null) {
				for (String name : names) {
					Asset addNew = new Asset(_Ng, prePath + name, _Manager.get(prePath + name), i, folder);
					_AssetMap.put(addNew.getId(), addNew);
					_AssetNameMap.put(addNew.getPath(), addNew.getId());
				}
			}
			_Folders.remove(folder + i);
		}
	}

	/**
	 * Has to be called to keep the loading going.
	 * 
	 * @return
	 */
	public boolean done() {
		boolean loading = _Manager.update();
		if (loading) {
			for (int i = 0; i < _UpdateFolders.size(); i++) {
				String folder = _UpdateFolders.get(i);
				addAssets(folder);
			}
			_UpdateFolders.clear();
		}
		return loading;
	}

	/**
	 * Free all the assets.
	 */
	public void dispose() {
		_Manager.dispose();
	}

	public void getAsset(String path) {

	}

	/**
	 * Free all the assets.
	 */
	public void disposeFolder(String folder) {
		String prePath = "";

		// Desktop or Android. IOS or Windows Phone not tested.
		if (Gdx.app.getType() == ApplicationType.Android) {
			prePath = "data/";
		} else {
			prePath = "./bin/data/";
		}

		for (int i = 0; i < _Types.length; i++) {
			List<String> names = _Folders.get(folder + i);
			if (names != null) {
				for (String name : names) {
					unloadAsset(prePath + name, i, folder);
				}
			}
			_Folders.remove(folder + i);
		}
	}

	public Asset getAssetById(int id) {
		return _AssetMap.get(id);
	}

	public Asset getAssetById(String path) {
		return _AssetMap.get(_AssetNameMap.get(path));
	}
}
