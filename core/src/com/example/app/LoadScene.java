package com.example.app;

import com.gem.scene.Scene;

public class LoadScene extends Scene {
    @Override
    public void onInit() {
        gem.loader.scoutFiles();
        gem.loader.finish();// blocking load
        gem.changeScene("com.example.app.Main");
    }
}
