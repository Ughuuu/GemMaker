package com.ngeen.misc;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.SynchronousAssetLoader;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.utils.Array;

public class ShaderLoader extends SynchronousAssetLoader<ShaderProgram, ShaderLoader.ShaderParameter> {

	private static final String VERTEX_SHADER_EXTENSION = ".vert";
	private static final String FRAGMENT_SHADER_EXTENSION = ".frag";

	public ShaderLoader(final FileHandleResolver resolver) {
		super(resolver);
	}

	public ShaderProgram load(AssetManager assetManager, String fileName, FileHandle file, ShaderParameter parameter) {
		final ShaderProgram shader = new ShaderProgram(resolve(fileName + VERTEX_SHADER_EXTENSION),
				resolve(fileName + FRAGMENT_SHADER_EXTENSION));
		if (shader.isCompiled() == false) {
			throw new IllegalStateException(shader.getLog());
		}
		return shader;
	}
	
	static public class ShaderParameter extends AssetLoaderParameters<ShaderProgram> {
		public ShaderProgram prog;
	}

	@Override
	public Array<AssetDescriptor> getDependencies(String fileName, FileHandle file, ShaderParameter parameter) {
		// TODO Auto-generated method stub
		return null;
	}
}