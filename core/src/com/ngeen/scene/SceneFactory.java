package com.ngeen.scene;

import com.ngeen.debug.Debugger;
import com.ngeen.engine.Ngeen;
import com.ngeen.systems.SystemScene;

/**
 * @author Dragos
 * @composed 1 - 1 Scene
 */
public class SceneFactory {
    private final Ngeen _Ng;
    private final SystemScene _SceneSystem;

    public SceneFactory(Ngeen ng, SystemScene _SceneSystem) {
        _Ng = ng;
        this._SceneSystem = _SceneSystem;
    }

    public void changeScene(String name) {
        _SceneSystem.setScene(makeScene(name));
    }

    public Scene makeScene(String name) {
        try {
            Class<?> scene = Class.forName(name);
            Scene scn = (Scene) scene.newInstance();
            scn.addNgeen(_Ng);
            scn.addSceneFactory(this);
            return scn;
        } catch (Exception e) {
            Debugger.log(e.toString());
        }
        return null;
    }
}
