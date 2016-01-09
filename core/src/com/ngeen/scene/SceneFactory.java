package com.ngeen.scene;

import com.ngeen.debug.Debugger;
import com.ngeen.engine.Ngeen;

public class SceneFactory {
	private final Ngeen _Ng;

	public SceneFactory(Ngeen ng) {
		_Ng = ng;
	}

	public Scene makeScene(String name) {
		try {
			Class<?> scene = Class.forName("com.ngeen.scene.implemented." + name);
			return (Scene) scene.getDeclaredConstructor(Ngeen.class).newInstance(_Ng);
		} catch (Exception e) {
			Debugger.log(e.toString());
		}
		return null;
	}
}
