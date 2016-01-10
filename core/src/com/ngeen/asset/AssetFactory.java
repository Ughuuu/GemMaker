package com.ngeen.asset;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeSet;

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

	private final Map<Class<?>, Integer> _ClassTypeIndex = new HashMap<Class<?>, Integer>() {
		{
			put(Texture.class, 0);
			put(TextureAtlas.class, 0);
			put(BitmapFont.class, 0);
			put(Sound.class, 0);
			put(Music.class, 0);
			put(ShaderProgram.class, 0);
		}
	};

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

	private String _PrePath;

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

		if (Gdx.app.getType() == ApplicationType.Android) {
			_PrePath = "data/";
		} else {
			_PrePath = "./bin/data/";
		}
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
		for (int i = 0; i < _Types.length; i++) {
			if (checkExt(name, i) == true)
				return i;
		}
		return -1;
	}

	private void loadAsset(String path, int resType) {
		_Manager.load(path, _ClassType[resType]);
	}

	public void unloadAsset(String path, int resType, String folder) {
		try {
			_Manager.unload(_PrePath + folder + path);
			_Folders.get(folder + resType).remove(path);
			int ind = _AssetNameMap.remove(folder + path);
			_AssetMap.remove(ind);
		} catch (Exception e) {
			Debugger.println(path + " is not loaded.");// Known bug. Sorry.
		}
	}

	public void scoutFiles() {
		_UpdateFolders.add("");
		FileHandle dirHandle = Gdx.files.internal(_PrePath);
		String folder = "", actualFolder = "";

		for (FileHandle entry : dirHandle.list()) {
			String name = entry.name();
			String extension = entry.extension();

			if (entry.isDirectory()) {// this has to be a class name.
				enqueFolder(name + "/", name + "/");
			} else {
				int extensionKey = getExt(extension);
				List<String> list = null;
				if (_Folders.containsKey(folder + extensionKey) == false) {
					list = new ArrayList<String>();
					_Folders.put(folder + extensionKey, list);
				} else {
					list = _Folders.get(folder + extensionKey);
				}
				loadAsset(_PrePath + name, extensionKey);
				list.add(name);
			}
		}
	}
	
	public void enqueFolder(String folder){
		enqueFolder(folder,folder);
	}

	/**
	 * Adds folder to the map. Can be used to load or save later.
	 * 
	 * @param folder
	 */
	private void enqueFolder(String folder, String actualFolder) {
		_UpdateFolders.add(folder);
		FileHandle dirHandle = Gdx.files.internal(_PrePath + actualFolder);

		for (FileHandle entry : dirHandle.list()) {
			String name = entry.name();
			String extension = entry.extension();

			if (entry.isDirectory()) {// this has to be a class name.
				enqueFolder(folder, actualFolder + name);
			} else {
				int extensionKey = getExt(extension);
				List<String> list = null;
				if (_Folders.containsKey(folder + extensionKey) == false) {
					list = new ArrayList<String>();
					_Folders.put(folder + extensionKey, list);
				} else {
					list = _Folders.get(folder + extensionKey);
				}
				loadAsset(_PrePath + actualFolder + name, extensionKey);
				list.add(name);
			}
		}
	}

	/**
	 * Loads a folder previously enqued.
	 * 
	 * @param folder
	 *            Folder name that is in the map.
	 */
	public void addFolder(String folder) {
		folder += "/";
		for (int i = 0; i < _Types.length; i++) {
			List<String> names = _Folders.get(folder + i);
			if (names != null) {
				for (String name : names) {
					loadAsset(_PrePath + name, i);
				}
			}
		}
	}

	private void addAssets(String folder) {
		for (int i = 0; i < _Types.length; i++) {
			List<String> names = _Folders.get(folder + i);
			if (names != null) {
				for (String name : names) {
					Asset addNew = new Asset(_Ng, name, _Manager.get(_PrePath + folder + name), i, folder);
					_AssetMap.put(addNew.getId(), addNew);
					_AssetNameMap.put(addNew.getFolder() + addNew.getPath(), addNew.getId());
				}
			}
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
	 * Has to be called to keep the loading going.
	 * 
	 * @return
	 */
	public boolean finish() {
		_Manager.finishLoading();
		for (int i = 0; i < _UpdateFolders.size(); i++) {
			String folder = _UpdateFolders.get(i);
			addAssets(folder);
		}
		_UpdateFolders.clear();
		return true;
	}

	public float getPercentage() {
		return _Manager.getProgress();
	}

	/**
	 * Free all the assets.
	 */
	public void dispose() {
		_Manager.dispose();
	}

	public List<String> getAssetsNameFolder(String folder) {
		List<String> ret = new ArrayList<String>();
		for (int i = 0; i < _Types.length; i++) {
			List<String> names = _Folders.get(folder + i);
			if (names != null) {
				for (String name : names) {
					ret.add(name);
				}
			}
		}
		return ret;
	}

	/**
	 * Set of String because we might have doubles.
	 * 
	 * @param folder
	 * @return
	 */
	public Set<String> getFolderNames() {
		Set<String> ret = new TreeSet<String>();
		for (Entry<String, List<String>> folderNamePair : _Folders.entrySet()) {
			String n = folderNamePair.getKey();
			ret.add(n.substring(0, n.length() - 1));
		}
		return ret;
	}

	/**
	 * Free all the assets.
	 */
	public void disposeFolder(String folder) {

		for (int i = 0; i < _Types.length; i++) {
			List<String> names = _Folders.get(folder + i);
			if (names != null) {
				while (names.size() != 0) {
					unloadAsset(names.get(0), i, folder);
				}
			}
			_Folders.remove(folder + i);
		}
	}

	public <T> Asset<T> getAssetById(int id) {
		Asset<T> asset = _AssetMap.get(id);
		return asset;
	}

	public <T> Asset<T> getAsset(String path) {
		Integer id = _AssetNameMap.get(path);
		if (id == null)
			return null;
		return getAssetById(id);
	}
}
