package com.example.app.client;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.gwt.GwtApplication;
import com.badlogic.gdx.backends.gwt.GwtApplicationConfiguration;
import com.example.app.Main;

public class HtmlLauncher extends GwtApplication {

	Main main;
	
    @Override
    public GwtApplicationConfiguration getConfig() {
        return new GwtApplicationConfiguration(480, 320);
    }

    @Override
    public ApplicationListener getApplicationListener() {
        return main;
    }

	@Override
	public ApplicationListener createApplicationListener() {
		main = new Main();
		return main;
	}
}