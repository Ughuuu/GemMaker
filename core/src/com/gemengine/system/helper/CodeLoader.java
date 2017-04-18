/*******************************************************************************
 * Copyright 2011 See AUTHORS file.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/

package com.gemengine.system.helper;

import org.jsync.sync.Sync;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.AsynchronousAssetLoader;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;

public class CodeLoader<T> extends AsynchronousAssetLoader<Sync, CodeLoader.CodeParameter> {
	static public class CodeParameter extends AssetLoaderParameters<Sync> {
		private final ClassLoader classLoader;
		private final String destinationFolder;
		private final String sourceFolder;

		public CodeParameter(ClassLoader classLoader, String sourceFolder, String destinationFolder) {
			this.classLoader = classLoader;
			this.sourceFolder = sourceFolder;
			this.destinationFolder = destinationFolder;
		}
	}

	private Sync<T> sync;

	public CodeLoader(FileHandleResolver resolver) {
		super(resolver);
	}

	@Override
	public Array<AssetDescriptor> getDependencies(String fileName, FileHandle file, CodeParameter parameter) {
		return null;
	}

	@Override
	public void loadAsync(AssetManager manager, String fileName, FileHandle file, CodeParameter parameter) {
		String path = file.path();
		this.sync = new Sync<T>(parameter.classLoader, path, parameter.sourceFolder,
				parameter.destinationFolder);
	}

	@Override
	public Sync<T> loadSync(AssetManager manager, String fileName, FileHandle file, CodeParameter parameter) {
		String path = file.path();
		this.sync = new Sync<T>(parameter.classLoader, path, parameter.sourceFolder,
				parameter.destinationFolder);
		return sync;
	}

	protected T getNewInstance() {
		return sync.newInstance();
	}
}
