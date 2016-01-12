package com.ngeen.game.desktop;

import com.badlogic.gdx.Files.FileType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.ngeen.engine.Ngeen;

public class DesktopLauncher {
	public static void main(String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		// System.setProperty("org.lwjgl.opengl.Window.undecorated", "true");
		//config.width = LwjglApplicationConfiguration.getDesktopDisplayMode().width;
		//config.height = LwjglApplicationConfiguration.getDesktopDisplayMode().height;
		config.width = 1366;
		config.height = 760;
		config.addIcon("icon64.png", FileType.Internal);
		config.addIcon("icon16.png", FileType.Internal);
		// config.fullscreen = true;
		config.vSyncEnabled = true;
		new LwjglApplication(new Ngeen(), config);
	}
}
