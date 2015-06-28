package com.ngeen.load;

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
import com.ngeen.helper.EntityHelper;
import com.ngeen.holder.Constant;
import com.ngeen.holder.Ngeen;

public class Loader {

	/**
	 * 1 - texture 2 - texture atlas 3 - fonts 4 - sound 5 - music 6 - particles
	 */
	private final String[][] types = { { "png", "jpg", "jpeg", "gif" },
			{ "pack", "atlas" }, { "fnt" }, { "wav", "ogg", "mp3" },
			{ "wav", "ogg", "mp3" }, { "p" } };
	private ArrayList<String> folderName = new ArrayList<String>();
	private final EntityHelper entityHelper;
	private boolean loading = false;

	public Loader(EntityHelper entityHelper) {
		this.entityHelper = entityHelper;
	}

	private boolean checkExt(String name, int i) {
		for (int j = 0; j < types[i].length; j++) {
			if (name.compareTo(types[i][j]) == 0
					|| name.compareTo(types[i][j].toUpperCase()) == 0) {
				return true;
			}
		}
		return false;
	}

	private void preLoad(String loc, int resource) {
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
				manager.load(path + name, ParticleEffect.class);
				break;
			}
			}
		}
	}

	public void preLoadAll(String folder) {
		folderName.add(folder);
		if (folderName.size() > 1 && loading) {
			return;
		}
		continueLoading();
	}

	private void continueLoading() {
		loading = true;
		String actualFolder = folderName.get(0);
		preLoad("data/texture/" + actualFolder + "/", 0);
		preLoad("data/spritesheet/" + actualFolder + "/", 1);
		preLoad("data/font/" + actualFolder + "/", 2);
		preLoad("data/sound/" + actualFolder + "/", 3);
		preLoad("data/music/" + actualFolder + "/", 4);
		preLoad("data/particle/" + actualFolder + "/", 5);
	}

	public boolean done() {
		final AssetManager manager = Constant.MANAGER;
		if (manager.update() == true && loading == true) {
			String folder = folderName.get(0);
			folderName.remove(0);
			loading = false;

			LoadAll("data/texture/" + folder + "/", 0);
			LoadAll("data/spritesheet/" + folder + "/", 1);
			LoadAll("data/font/" + folder + "/", 2);
			LoadAll("data/sound/" + folder + "/", 3);
			LoadAll("data/music/" + folder + "/", 4);
			LoadAll("data/particle/" + folder + "/", 5);

			if (folderName.isEmpty()) {
				return true;
			} else {
				continueLoading();
			}
		}
		return false;
	}

	private void LoadAll(String loc, int resource) {
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
				Texture obj = manager.get(path + name, Texture.class);
				entityHelper.createResource(obj, name, "~TEXTURE");
				break;
			}
			case 1: {
				TextureAtlas obj = manager.get(path + name, TextureAtlas.class);
				entityHelper.createResource(obj, name, "~TEXTURE_ATLAS");
				break;
			}
			case 2: {
				BitmapFont obj = manager.get(path + name, BitmapFont.class);
				entityHelper.createResource(obj, name, "~BITMAP_FONT");
				break;
			}
			case 3: {
				Sound obj = manager.get(path + name, Sound.class);
				entityHelper.createResource(obj, name, "~SOUND");
				break;
			}
			case 4: {
				Music obj = manager.get(path + name, Music.class);
				entityHelper.createResource(obj, name, "~MUSIC");
				break;
			}
			case 5: {
				ParticleEffect obj = manager.get(path + name,
						ParticleEffect.class);
				entityHelper.createResource(obj, name, "~PARTICLE_EFFECT");
				break;
			}
			}
		}
	}

	public void dispose() {
		final AssetManager manager = Constant.MANAGER;
		manager.dispose();
	}
}
