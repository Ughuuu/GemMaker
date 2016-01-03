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
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;

public class Loader {

	/**
	 * 0 - texture 1 - texture atlas 2 - fonts 3 - sound 4 - music 5 - shaders
	 */
	private static final String[][] types = { { "png", "jpg", "jpeg", "gif" }, { "pack", "atlas" }, { "fnt" },
			{ "wav", "ogg", "mp3" }, { "wav", "ogg", "mp3" }, { ".vert" } };
	private static ArrayList<String> folderName = new ArrayList<String>();
	private static boolean loading = false;

	private static boolean checkExt(String name, int i) {
		for (int j = 0; j < types[i].length; j++) {
			if (name.compareTo(types[i][j]) == 0 || name.compareTo(types[i][j].toUpperCase()) == 0) {
				return true;
			}
		}
		return false;
	}

	private static void preLoad(String loc, int resource) {
		final AssetManager manager = Constant.MANAGER;
		FileHandle dirHandle;
		String path = "";
		if (Gdx.app.getType() == ApplicationType.Android) {
			path = loc;
			dirHandle = Gdx.files.internal(path);
		} else {
			path = "./bin/" + loc;
			dirHandle = Gdx.files.internal(path);
		}
		for (FileHandle entry : dirHandle.list()) {
			String name = entry.name();
			if (!checkExt(entry.extension(), resource))
				continue;
			switch (resource) {
			case 0: {
				manager.load(path + name, Texture.class);
				break;
			}
			case 1: {
				manager.load(path + name, TextureAtlas.class);
				break;
			}
			case 2: {
				manager.load(path + name, BitmapFont.class);
				break;
			}
			case 3: {
				manager.load(path + name, Sound.class);
				break;
			}
			case 4: {
				manager.load(path + name, Music.class);
				break;
			}
			case 5: {
				manager.load((path + name).substring(0, (path + name).length() - 5), ShaderProgram.class);
				break;
			}
			}
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

	public static void dispose() {
		final AssetManager manager = Constant.MANAGER;
		manager.dispose();
	}
}
