package com.ngeen.engine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;

public class Loader {
	/**
	 * 0 - texture 1 - texture atlas 2 - fonts 3 - sound 4 - music 5 - shader
	 */
	private final String[][] _Types = { { "png", "jpg", "jpeg", "gif" }, 
			{ "pack", "atlas" }, 
			{ "fnt" },
			{ "wav", "ogg", "mp3" }, 
			{ "wav", "ogg", "mp3" }, 
			{ ".vert" } };
	
	private final Class<?>[] _ClassType = {
		Texture.class, TextureAtlas.class, BitmapFont.class, 
		Sound.class, Music.class, ShaderProgram.class
	};
	
	/**
	 * Name of first stage ever made. Load Stage.
	 * Used only if assets are found directly in data folder.
	 * If so, they are considered to be in ./data/LoadStage/
	 */
	private final String _LoadStage = "LoadStage";
	
	/**
	 * All underlying assets are kept here.
	 */
	private AssetManager _Manager;
	
	/**
	 * If this factory still loads objects or not.
	 */
	private boolean _Loading = false;

	/**
	 * Map from Stage Name Folder(Ex. LoadStage, GameStage, etc.) in data to All the files in it.
	 * The string also has an index number to follow it, representing the Types it holds.
	 */
	private Map<String, List<FileHandle> > _Folders = new HashMap<String, List<FileHandle> >();
	
	/**
	 * Check if given file is of specific given type.
	 * This is done only when specified. Cannot be done in Release mode.
	 * @param name Name of the file
	 * @param i Index of _Types array of arrays.
	 * @return If it is of specified type or not.
	 */
	private boolean checkExt(String name, int i) {
		for (int j = 0; j < _Types[i].length; j++) {
			if (name.compareTo(_Types[i][j]) == 0 || 
					name.compareTo(_Types[i][j].toUpperCase()) == 0) {
				return true;
			}
		}
		return false;
	}
	
	private void loadAsset(String path, int resType){

		switch (resType) {
		case 0: {
			_Manager.load(path, Texture.class);
			break;
		}
		case 1: {
			_Manager.load(path, TextureAtlas.class);
			break;
		}
		case 2: {
			_Manager.load(path, BitmapFont.class);
			break;
		}
		case 3: {
			_Manager.load(path, Sound.class);
			break;
		}
		case 4: {
			_Manager.load(path, Music.class);
			break;
		}
		case 5: {
			_Manager.load((path).substring(0, (path).length() - 5), ShaderProgram.class);
			break;
		}
		}
	}
	
	private static void preLoad() {
		FileHandle dirHandle;
		
		//Desktop or Android. IOS or Windows Phone not tested.
		if (Gdx.app.getType() == ApplicationType.Android) {
			dirHandle = Gdx.files.internal("data");
		} else {
			dirHandle = Gdx.files.internal("./bin/data");
		}
		
		getHandles(dirHandle, handles);
		for (FileHandle entry : dirHandle.list()) {
			if(entry.isDirectory()){//this has to be a class name.
				
			}
			String name = entry.name();
			entry.path();
			if (!checkExt(entry.extension(), resource))
				continue;
		}
	}

	public static void preLoadAll(String folder) {
		folderName.add(folder);
		if (folderName.size() > 1 && loading) {
			return;
		}
		continueLoading();
	}

	private static void continueLoading() {
		loading = true;
		String actualFolder = folderName.get(0);
		preLoad("data/texture/" + actualFolder + "/", 0);
		preLoad("data/spritesheet/" + actualFolder + "/", 1);
		preLoad("data/font/" + actualFolder + "/", 2);
		preLoad("data/sound/" + actualFolder + "/", 3);
		preLoad("data/music/" + actualFolder + "/", 4);
		preLoad("data/shaders/" + actualFolder + "/", 5);
	}

	public static boolean done() {
		final AssetManager manager = Constant.MANAGER;
		if (manager.update() == true && loading == true) {
			String folder = folderName.get(0);
			folderName.remove(0);
			loading = false;

			if (folderName.isEmpty()) {
				return true;
			} else {
				continueLoading();
			}
		}
		return false;
	}

	public void dispose() {
		_Manager.dispose();
	}
}
