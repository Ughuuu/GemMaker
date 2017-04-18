package com.example.app.desktop;

import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Cursor;
import org.lwjgl.input.Mouse;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.example.app.Main;
<<<<<<< HEAD

public class DesktopLauncher {
	public static void main(String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		// System.setProperty("org.lwjgl.opengl.Window.undecorated", "true");
		//config.width = LwjglApplicationConfiguration.getDesktopDisplayMode().width;
		//config.height = LwjglApplicationConfiguration.getDesktopDisplayMode().height;
		config.width = 640;
		config.height = 480;
		config.addIcon("icon64.png", FileType.Internal);
		config.addIcon("icon16.png", FileType.Internal);
		// config.fullscreen = true;
		config.vSyncEnabled = true;
		new LwjglApplication(new Main(), config);
	}
=======
import com.gem.engine.EngineInfo;

public class DesktopLauncher {
    public static void main(String[] arg) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        //System.setProperty("org.lwjgl.opengl.Window.undecorated", "true");	
        config.title = "Gem Maker";
        config.width = 1366;
        config.height = 760;
    	
        config.vSyncEnabled = true;
        config.addIcon("data/engine/gem-ico128.png", Files.FileType.Internal);
        config.addIcon("data/engine/gem-ico32.png", Files.FileType.Internal);
        config.addIcon("data/engine/gem-ico16.png", Files.FileType.Internal);
        EngineInfo.Applet = false;
        EngineInfo.Android = false;
        new LwjglApplication(new Main(), config);
    }
>>>>>>> 48aceab8675624a45a311ec78fa143ebd177864d
}
