package com.ngeen.asset;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.SynchronousAssetLoader;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.utils.Array;

/**
 * This is a loader for glsl shaders.
 *
 * @opt hide com.badlogic.*
 * @hidden
 */
public class ShaderLoader extends SynchronousAssetLoader<ShaderProgram, ShaderLoader.ShaderParameter> {

    private static final String FRAGMENT_SHADER_EXTENSION = ".frag";
    private static final String VERTEX_SHADER_EXTENSION = ".vert";

    public ShaderLoader(final FileHandleResolver resolver) {
        super(resolver);
    }

    @Override
    public Array<AssetDescriptor> getDependencies(String fileName, FileHandle file, ShaderParameter parameter) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ShaderProgram load(AssetManager assetManager, String fileName, FileHandle file, ShaderParameter parameter) {
        String shaderName = fileName = (fileName).substring(0, (fileName).length() - 5);
        final ShaderProgram shader = new ShaderProgram(resolve(shaderName + VERTEX_SHADER_EXTENSION),
                resolve(shaderName + FRAGMENT_SHADER_EXTENSION));
        if (shader.isCompiled() == false) {
            throw new IllegalStateException(shader.getLog());
        }
        return shader;
    }

    /**
     * @opt hide com.badlogic.*
     * @hidden
     */
    static public class ShaderParameter extends AssetLoaderParameters<ShaderProgram> {
        public ShaderProgram prog;
    }
}