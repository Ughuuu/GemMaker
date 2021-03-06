package com.example.app.desktop;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.gemengine.engine.core.GemEngine;

public class DesktopLauncher {
	public static void main(String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		// System.setProperty("org.lwjgl.opengl.Window.undecorated", "true");
		config.title = "Gem Maker";
		config.width = 640;
		config.height = 480;

		config.vSyncEnabled = true;
		config.addIcon("assets/img/gem-ico128.png", Files.FileType.Internal);
		config.addIcon("assets/img/gem-ico16.png", Files.FileType.Internal);
		config.addIcon("assets/img/gem-ico32.png", Files.FileType.Internal);
		new LwjglApplication(new GemEngine(), config);
	}
}
