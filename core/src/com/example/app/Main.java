package com.example.app;

import com.ngeen.engine.Ngeen;

public class Main extends Ngeen {

    public static void main(String[] arg) {
        new Main().create();
    }

    @Override
    public Class<?> getEntry() {
        return LoadScene.class;
    }
}
