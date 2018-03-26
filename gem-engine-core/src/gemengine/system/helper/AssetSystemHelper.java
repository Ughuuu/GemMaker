package gemengine.system.helper;

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
import gemengine.system.AssetSystem;
import gemengine.system.loaders.LoaderData;
import gemengine.system.loaders.TextLoader;

/**
 * Helper class used in asset system.
 * 
 * @author Dragos
 *
 */
public class AssetSystemHelper {
	/**
	 * Get the extension of a path.
	 * 
	 * @param path
	 *            Path to get extension from
	 * @return The extension of the path, or empty string.
	 */
	public static String getExtension(String path) {
		int extensionStart = path.lastIndexOf('.');
		if (extensionStart == -1)
			return "";
		return path.substring(extensionStart);
	}

	/**
	 * Get the path without the extension
	 * 
	 * @param path
	 *            Path to get path without extension from
	 * @return The path without extension or same path if no extension is found.
	 */
	public static String getWithoutExtension(String path) {
		int extensionStart = path.lastIndexOf('.');
		if (extensionStart == -1)
			return "";
		return path.substring(0, extensionStart);
	}

	/**
	 * Sets the default loaders to the asset systems. This is done by default if
	 * it is set in the {@link com.gemengine.engine.GemConfiguration} when
	 * constructing the {@link com.gemengine.engine.Gem}.
	 * 
	 * @param assetSystem
	 */
	public static void setDefaultLoaders(AssetSystem assetSystem) {
		FileHandleResolver resolver = assetSystem.getFileHandleResolver();
		assetSystem.addLoaderDefault(new LoaderData(BitmapFont.class), null, new BitmapFontLoader(resolver), ".fnt");
		assetSystem.addLoaderDefault(new LoaderData(Music.class), Messages.getString("AssetSystemHelper.MusicFolder"),
				new MusicLoader(resolver), ".wav", ".mp3", ".ogg"); //$NON-NLS-1$
		assetSystem.addLoaderDefault(new LoaderData(Sound.class), Messages.getString("AssetSystemHelper.SoundFolder"),
				new SoundLoader(resolver), ".wav", ".mp3", ".ogg"); //$NON-NLS-1$
		assetSystem.addLoaderDefault(new LoaderData(Pixmap.class), Messages.getString("AssetSystemHelper.PixmapFolder"),
				new PixmapLoader(resolver), ".png", ".jpg", ".jpeg", ".bmp"); //$NON-NLS-4$
		assetSystem.addLoaderDefault(new LoaderData(TextureAtlas.class),
				Messages.getString("AssetSystemHelper.AtlasFolder"), new TextureAtlasLoader(resolver), ".atlas");// $NON-NLS-3$

		assetSystem.addLoaderDefault(new LoaderData(Texture.class), Messages.getString("AssetSystemHelper.ImageFolder"),
				new TextureLoader(resolver), ".png", ".jpg", ".jpeg", ".bmp"); //$NON-NLS-3$
		assetSystem.addLoaderDefault(new LoaderData(Skin.class), "uiskin/", new SkinLoader(resolver), ".json");
		assetSystem.addLoaderDefault(new LoaderData(ParticleEffect.class), null, new ParticleEffectLoader(resolver),
				".2dparticle");
		assetSystem.addLoaderDefault(new LoaderData(com.badlogic.gdx.graphics.g3d.particles.ParticleEffect.class), null,
				new com.badlogic.gdx.graphics.g3d.particles.ParticleEffectLoader(resolver), ".3dparticle");
		assetSystem.addLoaderDefault(new LoaderData(PolygonRegion.class), null, new PolygonRegionLoader(resolver),
				".psh");
		assetSystem.addLoaderOverride(new LoaderData(Model.class), null, new G3dModelLoader(new JsonReader(), resolver),
				".g3dj");
		assetSystem.addLoaderOverride(new LoaderData(Model.class), null,
				new G3dModelLoader(new UBJsonReader(), resolver), ".g3db");
		assetSystem.addLoaderOverride(new LoaderData(Model.class), null, new ObjLoader(resolver), ".obj");
		assetSystem.addLoaderDefault(new LoaderData(ShaderProgram.class), null, new ShaderProgramLoader(resolver),
				".vert");

		assetSystem.addLoaderDefault(new LoaderData(String.class), null, new TextLoader(resolver), ".txt", ".json");
	}
}
