package com.gem.asset;

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
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.gem.component.Script;
import com.gem.debug.Debugger;
import com.gem.engine.EngineInfo;
import com.gem.engine.Gem;

import lombok.val;

/**
 * This method has all the necessary assets to a game. It can hold types like
 * _Types = { { "png", "jpg", "jpeg", "gif" }, { "pack", "atlas" }, { "fnt" }, {
 * "wav", "ogg", "mp3" }, { "wav", "ogg", "mp3" }, { "vert" } } In order to load
 * all files in all folders located at the data folder, relative to your project
 * classpath, use the function {@link #scoutFiles() scoutFiles}
 * <p>
 * Usualy, files are loaded when the scene loads, searching for the folder with
 * the name of your scene.
 * <p>
 * If you do wish to load files on your own, use finish() to do synchronous
 * loading(call it and the loading will be done when the function returns, or
 * asynchronous loading with the function done() (call it multiple times,
 * returns true if loading is finished).
 *
 * @author Dragos
 * @composed 1 has * Asset
 */
public class AssetFactory {
	public static String prePath;
	private final Class<?>[] classType = { Texture.class, TextureAtlas.class, BitmapFont.class, Sound.class,
			Music.class, ShaderProgram.class , Script.class};
	private final Gem gem;
	/**
	 * 0 - texture 1 - texture atlas 2 - fonts 3 - sound 4 - music 5 - shader
	 */
	private final String[][] types = { { "png", "jpg", "jpeg", "gif" }, { "pack", "atlas" }, { "fnt" },
			{ "wav", "ogg", "mp3" }, { "wav", "ogg", "mp3" }, { "vert" }};
	private Map<Integer, Asset> assetMap;
	private Map<String, Integer> assetNameMap;
	/**
	 * Map from Stage Name Folder(Ex. LoadStage, GameStage, etc.) in data to All
	 * the files in it. The string also has an index number to follow it,
	 * representing the Types it holds. Ex LoadStage0, LoadStage1..
	 */
	private Map<String, List<String>> folders = new HashMap<String, List<String>>();
	/**
	 * All underlying assets are kept here.
	 */
	private AssetManager manager;
	private List<String> updateFolders;

	public AssetFactory(Gem ng) {
		gem = ng;
		manager = new AssetManager();
		manager.setLoader(ShaderProgram.class, new ShaderLoader(new InternalFileHandleResolver()));
		assetMap = new HashMap<Integer, Asset>();
		assetNameMap = new HashMap<String, Integer>();
		updateFolders = new ArrayList<String>();

		if (Gdx.app.getType() == ApplicationType.Android || EngineInfo.Applet == true || EngineInfo.Android == true) {
			prePath = "data/";
		} else {
			prePath = "data/";
			// _PrePath = "./bin/data/"; // On eclipse we also need this?
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
		for (int i = 0; i < types.length; i++) {
			List<String> names = folders.get(folder + i);
			if (names != null) {
				for (String name : names) {
					loadAsset(prePath + name, i);
				}
			}
		}
	}

	/**
	 * Free all the assets.
	 */
	public void dispose() {
		manager.dispose();
	}

	/**
	 * Free all the assets.
	 */
	public void disposeFolder(String folder) {

		for (int i = 0; i < types.length; i++) {
			List<String> names = folders.get(folder + i);
			if (names != null) {
				while (names.size() != 0) {
					unloadAsset(names.get(0), i, folder);
				}
			}
			folders.remove(folder + i);
		}
	}

	/**
	 * Has to be called to keep the loading going.
	 *
	 * @return true if loading is finished.
	 */
	public boolean done() {
		boolean loading = manager.update();
		if (loading) {
			for (int i = 0; i < updateFolders.size(); i++) {
				String folder = updateFolders.get(i);
				addAssets(folder);
			}
			updateFolders.clear();
		}
		return loading;
	}

	public void enqueFolder(String folder) {
		enqueFolder(folder, "");
	}

	/**
	 * Has to be called to keep the loading going.
	 *
	 * @return
	 */
	public boolean finish() {
		manager.finishLoading();
		for (int i = 0; i < updateFolders.size(); i++) {
			String folder = updateFolders.get(i);
			addAssets(folder);
		}
		updateFolders.clear();
		return true;
	}

	public <T> Asset<T> getAsset(String path) {
		Integer id = assetNameMap.get(path);
		if (id == null) {
			//for (val trace : Thread.currentThread().getStackTrace()) {
			//	Debugger.log(trace);
			//}
			//Debugger.log("Asset not found " + path);
			return null;
		}
		return getAssetById(id);
	}

	public <T> Asset<T> getAssetById(int id) {
		Asset<T> asset = assetMap.get(id);
		return asset;
	}

	public List<String> getAssetsNameFolder(String folder) {
		List<String> ret = new ArrayList<String>();
		for (int i = 0; i < types.length; i++) {
			List<String> names = folders.get(folder + i);
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
		for (Entry<String, List<String>> folderNamePair : folders.entrySet()) {
			String n = folderNamePair.getKey();
			ret.add(n.substring(0, n.length() - 1));
		}
		return ret;
	}

	public float getPercentage() {
		return manager.getProgress();
	}

	public void scoutFiles() {
		updateFolders.add("");
		FileHandle dirHandle = Gdx.files.internal(prePath);
		String folder = "", actualFolder = "";

		for (FileHandle entry : dirHandle.list()) {
			String name = entry.name();
			String extension = entry.extension();

			if (entry.isDirectory()) {// this has to be a class name.
				enqueFolder(name + "/", "");
			} else {
				int extensionKey = getExt(extension);
				List<String> list = null;
				if (folders.containsKey(folder + extensionKey) == false) {
					list = new ArrayList<String>();
					folders.put(folder + extensionKey, list);
				} else {
					list = folders.get(folder + extensionKey);
				}
				loadAsset(prePath + name, extensionKey);
				list.add(name);
			}
		}
	}

	public void unloadAsset(String path, int resType, String folder) {
		try {
			manager.unload(prePath + folder + path);
			folders.get(folder + resType).remove(path);
			int ind = assetNameMap.remove(folder + path);
			assetMap.remove(ind);
		} catch (Exception e) {
			e.printStackTrace();
			Debugger.println(path + " is not loaded.");// Known bug. Sorry.
		}
	}

	private void addAssets(String folder) {
		for (int i = 0; i < types.length; i++) {
			List<String> names = folders.get(folder + i);
			if (names != null) {
				for (String name : names) {
					Asset addNew = new Asset(gem, name, manager.get(prePath + folder + name), i, folder);
					assetMap.put(addNew.getId(), addNew);
					assetNameMap.put(addNew.getFolder() + addNew.getPath(), addNew.getId());
				}
			}
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
	public boolean checkExt(String name, int i) {
		for (int j = 0; j < types[i].length; j++) {
			if (name.compareTo(types[i][j]) == 0 || name.compareTo(types[i][j].toUpperCase()) == 0) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Adds folder to the map. Can be used to load or save later.
	 *
	 * @param folder
	 */
	private void enqueFolder(String folder, String actualFolder) {
		updateFolders.add(folder);
		FileHandle dirHandle = Gdx.files.internal(prePath + folder + actualFolder);

		for (FileHandle entry : dirHandle.list()) {
			String name = entry.name();
			String extension = entry.extension();

			if (entry.isDirectory()) {// this has to be a class name.
				enqueFolder(folder, actualFolder + name + "/");
			} else {
				int extensionKey = getExt(extension);
				if (extensionKey == -1) {
					continue;
				}
				List<String> list = null;
				if (folders.containsKey(folder + extensionKey) == false) {
					list = new ArrayList<String>();
					folders.put(folder + extensionKey, list);
				} else {
					list = folders.get(folder + extensionKey);
				}
				loadAsset(prePath + folder + actualFolder + name, extensionKey);
				list.add(actualFolder + name);
			}
		}
	}

	/**
	 * Get the coresponding index for the given file extension.
	 *
	 * @param name
	 *            File extension.
	 * @return Coresponding index in data types table.
	 */
	public int getExt(String name) {
		for (int i = 0; i < types.length; i++) {
			if (checkExt(name, i) == true)
				return i;
		}
		return -1;
	}

	public void loadAsset(String path, int resType) {
		if (resType == -1) {
			return;
		}
		manager.load(path, classType[resType]);
	}
}
