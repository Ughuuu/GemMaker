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
import com.gemengine.system.loaders.TextLoader;

public class AssetSystemHelper {
	public static void setDefaultLoaders(AssetSystem assetSystem) {
		FileHandleResolver resolver = assetSystem.getFileHandleResolver();
		assetSystem.addLoaderDefault(new AssetSystem.LoaderData(BitmapFont.class), new BitmapFontLoader(resolver),
				".fnt");
		assetSystem.addLoaderDefault(new AssetSystem.LoaderData(Music.class), new MusicLoader(resolver), ".wav", ".mp3",
				".ogg");
		assetSystem.addLoaderDefault(new AssetSystem.LoaderData(Sound.class), new SoundLoader(resolver), ".wav", ".mp3",
				".ogg");
		assetSystem.addTypeFolder(new AssetSystem.LoaderData(Sound.class), "sound/");
		assetSystem.addTypeFolder(new AssetSystem.LoaderData(Music.class), "music/");
		assetSystem.addLoaderDefault(new AssetSystem.LoaderData(Pixmap.class), new PixmapLoader(resolver), ".png",
				".jpg", ".jpeg", ".bmp");
		assetSystem.addLoaderDefault(new AssetSystem.LoaderData(TextureAtlas.class), new TextureAtlasLoader(resolver),
				".png", ".jpg", ".jpeg", ".bmp");
		assetSystem.addLoaderDefault(new AssetSystem.LoaderData(Texture.class), new TextureLoader(resolver), ".png",
				".jpg", ".jpeg", ".bmp");
		assetSystem.addTypeFolder(new AssetSystem.LoaderData(Texture.class), "img/");
		assetSystem.addTypeFolder(new AssetSystem.LoaderData(TextureAtlas.class), "atlas/");
		assetSystem.addTypeFolder(new AssetSystem.LoaderData(Pixmap.class), "pixmap/");
		assetSystem.addLoaderDefault(new AssetSystem.LoaderData(Skin.class), new SkinLoader(resolver), ".json");

		assetSystem.addLoaderDefault(new AssetSystem.LoaderData(ParticleEffect.class),
				new ParticleEffectLoader(resolver), ".2dparticle");
		assetSystem.addLoaderDefault(
				new AssetSystem.LoaderData(com.badlogic.gdx.graphics.g3d.particles.ParticleEffect.class),
				new com.badlogic.gdx.graphics.g3d.particles.ParticleEffectLoader(resolver), ".3dparticle");
		assetSystem.addLoaderDefault(new AssetSystem.LoaderData(PolygonRegion.class), new PolygonRegionLoader(resolver),
				".psh");
		assetSystem.addLoaderOverride(new AssetSystem.LoaderData(Model.class),
				new G3dModelLoader(new JsonReader(), resolver), ".g3dj");
		assetSystem.addLoaderOverride(new AssetSystem.LoaderData(Model.class),
				new G3dModelLoader(new UBJsonReader(), resolver), ".g3db");
		assetSystem.addLoaderOverride(new AssetSystem.LoaderData(Model.class), new ObjLoader(resolver), ".obj");
		assetSystem.addLoaderDefault(new AssetSystem.LoaderData(ShaderProgram.class), new ShaderProgramLoader(resolver),
				".vert");

		assetSystem.addLoaderDefault(new AssetSystem.LoaderData(String.class), new TextLoader(resolver), ".txt");
	}
}
