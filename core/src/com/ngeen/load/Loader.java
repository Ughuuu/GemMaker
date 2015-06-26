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
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g3d.Model;
import com.ngeen.holder.Ngeen;

public class Loader {
	private Map<String, Texture> texture = new LinkedHashMap<String, Texture>();
	private Map<String, TextureAtlas> atlas = new LinkedHashMap<String, TextureAtlas>();
	private Map<String, BitmapFont> fonts = new LinkedHashMap<String, BitmapFont>();

	private final String[][] types = { { "png", "jpg", "jpeg", "gif" },
			{ "pack"} , {"fnt"} };
	private boolean done = false;
	private AssetManager manager = new AssetManager();

	int checkExt(String name) {
		for (int i = 0; i < types.length; i++) {
			boolean ok = false;
			for (int j = 0; j < types[i].length; j++) {
				if (name.compareTo(types[i][j]) == 0
						|| name.compareTo(types[i][j].toUpperCase()) == 0) {
					ok = true;
				}
			}
			if (ok == true) {
				return i;
			}
		}
		return -1;
	}

	public void preLoad(String loc, int resource) {
		done = true;
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
			int type = checkExt(entry.extension());
			if(type == resource)
			switch (type) {
			case 0:
				manager.load(path + name, Texture.class);
				break;
			case 1:
				manager.load(path + name, TextureAtlas.class);
				break;
			case 2:
				manager.load(path + name, BitmapFont.class);
				break;
			}
		}
	}
	
	public void preLoadAll(){
		preLoad("data/texture/",0);
		preLoad("data/spritesheet/",1);
		preLoad("data/font/",2);
	}

	public boolean done() {
		if (manager.update() == true && done == true) {
			done = false;
			LoadAll("data/texture/",0);
			LoadAll("data/spritesheet/",1);
			LoadAll("data/font/",2);
			Ngeen.afterLoad();
		}
		return manager.update();
	}

	public Object get(String name, Object type) {
		if (type.equals(Texture.class))
			return texture.get(name);
		if (type.equals(TextureAtlas.class))
			return atlas.get(name);
		if (type.equals(BitmapFont.class))
			return fonts.get(name);
		return null;
	}

	public Object get(int ind, Object type) {
		int ok = 0;
		if (type.equals(Texture.class))
			for (Entry<String, Texture> entry : texture.entrySet()) {
				String key = entry.getKey();
				Texture value = entry.getValue();
				if (ok == ind) {
					return value;
				}
				ok++;
			}
		if (type.equals(TextureAtlas.class))
			for (Entry<String, TextureAtlas> entry : atlas.entrySet()) {
				String key = entry.getKey();
				TextureAtlas value = entry.getValue();
				if (ok == ind) {
					return value;
				}
				ok++;
			}
		if (type.equals(BitmapFont.class))
			for (Entry<String, BitmapFont> entry : fonts.entrySet()) {
				String key = entry.getKey();
				BitmapFont value = entry.getValue();
				if (ok == ind) {
					return value;
				}
				ok++;
			}
		return null;
	}

	public void LoadAll(String loc, int resource) {
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
			int type = checkExt(entry.extension());
			if(type == resource)
			switch (type) {
			case 0:
				Texture tex = manager.get(path + name, Texture.class);
				texture.put(name, tex);
				break;
			case 1:
				TextureAtlas texatl = manager.get(path + name,
						TextureAtlas.class);
				atlas.put(name, texatl);
				break;
			case 2:
				BitmapFont font = manager.get(path + name,
						BitmapFont.class);
				fonts.put(name, font);
				break;
			}
		}
	}

	public void dispose() {
		manager.dispose();
	}
}
