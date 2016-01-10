package com.ngeen.scene;

import com.ngeen.debug.Debugger;
import com.ngeen.engine.Ngeen;
import com.ngeen.systems.SceneSystem;

public class SceneFactory {
	private final Ngeen _Ng;
	private final SceneSystem _SceneSystem;

	public SceneFactory(Ngeen ng, SceneSystem _SceneSystem) {
		_Ng = ng;
		this._SceneSystem = _SceneSystem;
	}

	public Scene makeScene(String name) {
		try {
			Class<?> scene = Class.forName("com.ngeen.scene." + name);
			Scene scn = (Scene) scene.newInstance();
			scn.addNgeen(_Ng);
			scn.addSceneFactory(this);
			return scn;
		} catch (Exception e) {
			Debugger.log(e.toString());
		}
		return null;
	}
	
	public void changeScene(String name){
		_SceneSystem.setScene(makeScene(name));
	}
}
