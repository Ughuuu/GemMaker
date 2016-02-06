package com.example.app;

import com.ngeen.engine.Ngeen;

public class Main extends Ngeen {

	@Override
	public Class<?> getEntry() {
		return LoadScene.class;
	}
}
