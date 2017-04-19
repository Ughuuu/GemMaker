package com.gemengine.system.helper;

import com.badlogic.gdx.assets.loaders.BitmapFontLoader;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.MusicLoader;
import com.badlogic.gdx.assets.loaders.ParticleEffectLoader;
import com.badlogic.gdx.assets.loaders.ShaderProgramLoader;
import com.badlogic.gdx.assets.loaders.TextureLoader;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.PolygonRegion;
import com.badlogic.gdx.graphics.g2d.PolygonRegionLoader;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.loader.G3dModelLoader;
import com.badlogic.gdx.graphics.g3d.loader.ObjLoader;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.UBJsonReader;
import com.gemengine.system.AssetSystem;

public class AssetSystemHelper {
	public static void setDefaultLoaders(AssetSystem assetSystem) {
		FileHandleResolver resolver = assetSystem.getFileHandleResolver();
		assetSystem.addLoaderDefault(BitmapFont.class, new BitmapFontLoader(resolver), ".fnt");

		assetSystem.addLoaderDefault(Music.class, new MusicLoader(resolver), ".wav");
		assetSystem.addExtensionMapping(MusicLoader.class, ".mp3");
		assetSystem.addExtensionMapping(MusicLoader.class, ".ogg");
		// assetSystem.addLoaderDefault(Sound.class, new SoundLoader(resolver));
		// assetSystem.addLoaderDefault(Pixmap.class, new
		// PixmapLoader(resolver));
		// assetSystem.addLoaderDefault(TextureAtlas.class, new
		// TextureAtlasLoader(resolver));
		assetSystem.addLoaderDefault(Texture.class, new TextureLoader(resolver), ".png");
		assetSystem.addExtensionMapping(TextureLoader.class, ".jpg");
		assetSystem.addExtensionMapping(TextureLoader.class, ".jpeg");
		assetSystem.addExtensionMapping(TextureLoader.class, ".bmp");
		// assetSystem.addLoaderDefault(Skin.class, new SkinLoader(resolver));
		assetSystem.addLoaderDefault(ParticleEffect.class, new ParticleEffectLoader(resolver), ".2dparticle");
		assetSystem.addLoaderDefault(com.badlogic.gdx.graphics.g3d.particles.ParticleEffect.class,
				new com.badlogic.gdx.graphics.g3d.particles.ParticleEffectLoader(resolver), ".3dparticle");
		assetSystem.addLoaderDefault(PolygonRegion.class, new PolygonRegionLoader(resolver), ".psh");
		assetSystem.addLoaderOverride(Model.class, new G3dModelLoader(new JsonReader(), resolver), ".g3dj");
		assetSystem.addLoaderOverride(Model.class, new G3dModelLoader(new UBJsonReader(), resolver), ".g3db");
		assetSystem.addLoaderOverride(Model.class, new ObjLoader(resolver), ".obj");
		assetSystem.addLoaderDefault(ShaderProgram.class, new ShaderProgramLoader(resolver), ".vert");

		assetSystem.addLoaderDefault(String.class, new TextLoader(resolver), ".txt");
	}
}
