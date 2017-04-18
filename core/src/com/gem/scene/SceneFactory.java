package com.gem.scene;

import com.badlogic.gdx.Gdx;
import com.gem.engine.Gem;
import com.gem.systems.SystemScene;

/**
 * @author Dragos
 * @composed 1 - 1 Scene
 */
public class SceneFactory {
    private final Gem gem;
    private final SystemScene sceneSystem;

    public SceneFactory(Gem gem, SystemScene sceneSystem) {
        this.gem = gem;
        this.sceneSystem = sceneSystem;
    }

    public void changeScene(String name) {
        Gdx.graphics.setTitle(name);
        Scene scene = makeScene(name);
        scene.name = name;
        sceneSystem.setScene(scene);
    }

    public Scene makeScene(String name) {
    	Scene scn;
        try {
            Class<?> scene = Class.forName(name);
            scn = (Scene) scene.newInstance();
        } catch (Exception e) {
            // You don't have to have a scene class for the scene xml, it's
            // optional
            // e.printStackTrace();
            // Debugger.log(e.t);
            scn = new Scene();
            scn.name = name;
        }
        scn.addGem(gem);
        scn.addSceneFactory(this);
        return scn;
    }
}
