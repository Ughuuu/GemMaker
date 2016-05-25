package com.ngeen.debug;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.ngeen.asset.Asset;
import com.ngeen.engine.Ngeen;

import java.util.Set;

public class TestAsset {
    public TestAsset(Ngeen ng) {
        Asset<BitmapFont> font = ng.Loader.getAsset("LoadScene/fonts/AmaticSC-Regular.fnt");
        BitmapFont font2 = font.getData();
        assert (font2 != null);
        Asset<Texture> pic = ng.Loader.getAsset("LoadScene/fonts/AmaticSC-Regular.png");
        Texture txt = pic.getData();
        assert (txt != null);
        font.dispose();
        font = ng.Loader.getAsset("LoadScene/fonts/AmaticSC-Regular.fnt");
        assert (font == null);

        Set<String> folderNames = ng.Loader.getFolderNames();

        assert (folderNames.contains("LoadScene/"));

        ng.Loader.disposeFolder("LoadScene/");
        ng.Loader.disposeFolder("LoadScene/");

        font = ng.Loader.getAsset("LoadScene/fonts/AmaticSC-Regular.fnt");
        pic = ng.Loader.getAsset("LoadScene/fonts/AmaticSC-Regular.png");
        assert (font == null);
        assert (pic == null);

        ng.Loader.enqueFolder("LoadScene/");
        ng.Loader.enqueFolder("LoadScene/");

        ng.Loader.finish();

        font = ng.Loader.getAsset("LoadScene/fonts/AmaticSC-Regular.fnt");
        pic = ng.Loader.getAsset("LoadScene/fonts/AmaticSC-Regular.png");
        assert (font != null);
        assert (pic != null);

        Debugger.log("TestAsset()---PASS");
    }
}
