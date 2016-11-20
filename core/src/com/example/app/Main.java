package com.example.app;

import com.gem.engine.Gem;

public class Main extends Gem {

	public static void main(String[] arg) {
		new Main().create();
	}

	@Override
	public Class<?> getEntry() {
		return LoadScene.class;
	}
}
