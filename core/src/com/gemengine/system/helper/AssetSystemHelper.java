package com.gemengine.system.helper;

import com.badlogic.gdx.assets.loaders.BitmapFontLoader;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.MusicLoader;
import com.badlogic.gdx.assets.loaders.ParticleEffectLoader;
import com.badlogic.gdx.assets.loaders.PixmapLoader;
import com.badlogic.gdx.assets.loaders.ShaderProgramLoader;
import com.badlogic.gdx.assets.loaders.SkinLoader;
import com.badlogic.gdx.assets.loaders.SoundLoader;
import com.badlogic.gdx.assets.loaders.TextureAtlasLoader;
import com.badlogic.gdx.assets.loaders.TextureLoader;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.PolygonRegion;
import com.badlogic.gdx.graphics.g2d.PolygonRegionLoader;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.loader.G3dModelLoader;
import com.badlogic.gdx.graphics.g3d.loader.ObjLoader;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.UBJsonReader;
import com.gemengine.system.AssetSystem;
import com.gemengine.system.loaders.LoaderData;
import com.gemengine.system.loaders.TextLoader;

public class AssetSystemHelper {
	public static String getExtension(String path) {
		int extensionStart = path.lastIndexOf('.');
		if (extensionStart == -1)
			return "";
		return path.substring(extensionStart);
	}

	public static String getWithoutExtension(String path) {
		int extensionStart = path.lastIndexOf('.');
		if (extensionStart == -1)
			return "";
		return path.substring(0, extensionStart);
	}

	public static void setDefaultLoaders(AssetSystem assetSystem) {
		FileHandleResolver resolver = assetSystem.getFileHandleResolver();
		assetSystem.addLoaderDefault(new LoaderData(BitmapFont.class), new BitmapFontLoader(resolver), ".fnt");
		assetSystem.addLoaderDefault(new LoaderData(Music.class), new MusicLoader(resolver), ".wav", ".mp3", ".ogg");
		assetSystem.addLoaderDefault(new LoaderData(Sound.class), new SoundLoader(resolver), ".wav", ".mp3", ".ogg");
		assetSystem.addTypeFolder(new LoaderData(Sound.class), "sound/");
		assetSystem.addTypeFolder(new LoaderData(Music.class), "music/");
		assetSystem.addLoaderDefault(new LoaderData(Pixmap.class), new PixmapLoader(resolver), ".png", ".jpg", ".jpeg",
				".bmp");
		assetSystem.addLoaderDefault(new LoaderData(TextureAtlas.class), new TextureAtlasLoader(resolver), ".png",
				".jpg", ".jpeg", ".bmp");
		assetSystem.addLoaderDefault(new LoaderData(Texture.class), new TextureLoader(resolver), ".png", ".jpg",
				".jpeg", ".bmp");
		assetSystem.addTypeFolder(new LoaderData(Texture.class), "img/");
		assetSystem.addTypeFolder(new LoaderData(TextureAtlas.class), "atlas/");
		assetSystem.addTypeFolder(new LoaderData(Pixmap.class), "pixmap/");
		assetSystem.addLoaderDefault(new LoaderData(Skin.class), new SkinLoader(resolver), ".json");

		assetSystem.addLoaderDefault(new LoaderData(ParticleEffect.class), new ParticleEffectLoader(resolver),
				".2dparticle");
		assetSystem.addLoaderDefault(new LoaderData(com.badlogic.gdx.graphics.g3d.particles.ParticleEffect.class),
				new com.badlogic.gdx.graphics.g3d.particles.ParticleEffectLoader(resolver), ".3dparticle");
		assetSystem.addLoaderDefault(new LoaderData(PolygonRegion.class), new PolygonRegionLoader(resolver), ".psh");
		assetSystem.addLoaderOverride(new LoaderData(Model.class), new G3dModelLoader(new JsonReader(), resolver),
				".g3dj");
		assetSystem.addLoaderOverride(new LoaderData(Model.class), new G3dModelLoader(new UBJsonReader(), resolver),
				".g3db");
		assetSystem.addLoaderOverride(new LoaderData(Model.class), new ObjLoader(resolver), ".obj");
		assetSystem.addLoaderDefault(new LoaderData(ShaderProgram.class), new ShaderProgramLoader(resolver), ".vert");

		assetSystem.addLoaderDefault(new LoaderData(String.class), new TextLoader(resolver), ".txt");
	}
}
