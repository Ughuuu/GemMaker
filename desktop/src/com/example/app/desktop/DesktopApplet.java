package com.example.app.desktop;

import java.awt.Dimension;

import com.badlogic.gdx.Files.FileType;
import com.badlogic.gdx.backends.lwjgl.LwjglApplet;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.example.app.Main;

public class DesktopApplet extends LwjglApplet
{
    private static final long serialVersionUID = 1L;
    
    static Main main = new Main();
    
    public DesktopApplet()
    {
        super(main);
    }

    @Override
    public void resize(int width, int height) {
    	//main.resize(width, height);
    }
}
