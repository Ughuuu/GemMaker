package com.ngeen.debug;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.input.GestureDetector.GestureListener;
import com.ngeen.asset.Asset;
import com.ngeen.asset.AssetFactory;
import com.ngeen.component.ComponentFactory;
import com.ngeen.engine.Ngeen;
import com.ngeen.entity.EntityFactory;
import com.ngeen.scene.SceneFactory;
import com.ngeen.systems.SystemScene;

public class TestAsset {
	public TestAsset(Ngeen ng) {
		Asset<BitmapFont> font = ng.Loader.getAsset("LoadScene/fonts/AmaticSC-Regular.fnt");
		BitmapFont font2 = font.getData();
		assert(font2!=null);
		Asset<Texture> pic = ng.Loader.getAsset("LoadScene/fonts/AmaticSC-Regular.png");
		Texture txt = pic.getData();
		assert(txt!=null);
		font.dispose();
		font = ng.Loader.getAsset("LoadScene/fonts/AmaticSC-Regular.fnt");
		assert(font==null);

		Set<String> folderNames = ng.Loader.getFolderNames();
		
		assert(folderNames.contains("LoadScene/"));
		
		ng.Loader.disposeFolder("LoadScene/");
		ng.Loader.disposeFolder("LoadScene/");

		font = ng.Loader.getAsset("LoadScene/fonts/AmaticSC-Regular.fnt");
		pic = ng.Loader.getAsset("LoadScene/fonts/AmaticSC-Regular.png");
		assert(font==null);
		assert(pic==null);
		
		ng.Loader.enqueFolder("LoadScene/");
		ng.Loader.enqueFolder("LoadScene/");

		ng.Loader.finish();
		
		font = ng.Loader.getAsset("LoadScene/fonts/AmaticSC-Regular.fnt");
		pic = ng.Loader.getAsset("LoadScene/fonts/AmaticSC-Regular.png");
		assert(font!=null);
		assert(pic!=null);
		
		Debugger.log("TestAsset()---PASS");
	}
}
