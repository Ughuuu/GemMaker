package com.gem.debug;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.gem.asset.Asset;
import com.gem.engine.Gem;

import java.util.Set;

public class TestAsset {
    public TestAsset(Gem ng) {
        Asset<BitmapFont> font = ng.loader.getAsset("LoadScene/fonts/AmaticSC-Regular.fnt");
        BitmapFont font2 = font.getAsset();
        assert (font2 != null);
        Asset<Texture> pic = ng.loader.getAsset("LoadScene/fonts/AmaticSC-Regular.png");
        Texture txt = pic.getAsset();
        assert (txt != null);
        font.dispose();
        font = ng.loader.getAsset("LoadScene/fonts/AmaticSC-Regular.fnt");
        assert (font == null);

        Set<String> folderNames = ng.loader.getFolderNames();

        assert (folderNames.contains("LoadScene/"));

        ng.loader.disposeFolder("LoadScene/");
        ng.loader.disposeFolder("LoadScene/");

        font = ng.loader.getAsset("LoadScene/fonts/AmaticSC-Regular.fnt");
        pic = ng.loader.getAsset("LoadScene/fonts/AmaticSC-Regular.png");
        assert (font == null);
        assert (pic == null);

        ng.loader.enqueFolder("LoadScene/");
        ng.loader.enqueFolder("LoadScene/");

        ng.loader.finish();

        font = ng.loader.getAsset("LoadScene/fonts/AmaticSC-Regular.fnt");
        pic = ng.loader.getAsset("LoadScene/fonts/AmaticSC-Regular.png");
        assert (font != null);
        assert (pic != null);

        Debugger.log("TestAsset()---PASS");
    }
}
