package com.ngeen.scene;

import java.io.File;

import com.ngeen.component.DynaCode;
import com.ngeen.debug.Debugger;
import com.ngeen.engine.EngineInfo;
import com.ngeen.engine.Ngeen;
import com.ngeen.systems.SystemScene;

/**
 * @composed 1 - 1 Scene
 * @author Dragos
 *
 */
public class SceneFactory {
	private final Ngeen _Ng;
	private final SystemScene _SceneSystem;

	public SceneFactory(Ngeen ng, SystemScene _SceneSystem) {
		_Ng = ng;
		this._SceneSystem = _SceneSystem;
	}

	public Scene makeScene(String name) {
		try {
			Class<?> scene = Class.forName(name);
			Scene scn = (Scene) scene.newInstance();
			((Scene) scn).addNgeen(_Ng);
			((Scene) scn).addSceneFactory(this);
			return scn;
		} catch (Exception e) {
			Debugger.log(e.toString());
		}
		return null;
	}

	public void changeScene(String name) {
		_SceneSystem.setScene(makeScene(name));
	}
}
