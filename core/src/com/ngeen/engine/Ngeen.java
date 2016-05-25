package com.ngeen.engine;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.ngeen.action.CommandFactory;
import com.ngeen.asset.Asset;
import com.ngeen.asset.AssetFactory;
import com.ngeen.asset.MeshFactory;
import com.ngeen.component.ComponentFactory;
import com.ngeen.entity.*;
import com.ngeen.scene.SceneFactory;

/**
 * Main engine class. Links all elements and holds entities. <img src=
 * "https://raw.githubusercontent.com/Ughuuu/ngeen/online/core/doc/img/Ngeen.png"/>
 *
 * @author Dragos
 * @opt hide com.badlogic.*
 * @opt shape node
 * @composed 1 has * AssetFactory
 * @composed 1 has * EntityFactory
 * @composed 1 has * MeshFactory
 * @composed 1 has * ComponentFactory
 * @composed 1 has * SceneFactory
 * @composed 1 has * SystemFactory
 * @composed 1 has * XmlComponent
 */
public abstract class Ngeen extends ApplicationAdapter {
    public MeshFactory _MeshBuilder;
    public CollidableFactory CollidableBuilder;
    public EntityFactory EntityBuilder;
    public AssetFactory Loader;
    protected ComponentFactory _ComponentBuilder;
    protected SystemFactory _SystemBuilder;
    protected SceneFactory SceneBuilder;
    private ComponentSpokesman _ComponentSpokesman;
    private UIFactory UIBuilder;
    private XmlEntity XmlSave;

    public void changeScene(String newScene) {
        SceneBuilder.changeScene(newScene);
    }

    @Override
    public void create() {
        if (EngineInfo.Debug)
            CommandFactory.factory._Ng = this;
        init();
    }

    public <T> Asset<T> getAsset(String name) {
        return Loader.getAsset(name);
    }

    public Class<?> getCurrentScene() {
        return _SystemBuilder._SceneSystem.getScene();
    }

    public Entity getEntity(int id) {
        return EntityBuilder.getById(id);
    }

    public Entity getEntity(String name) {
        return EntityBuilder.getByName(name);
    }

    public abstract Class<?> getEntry();

    public void init() {
        Gdx.graphics.setVSync(false);
        Loader = new AssetFactory(this);

        UIBuilder = new UIFactory(this);
        _ComponentSpokesman = new ComponentSpokesman(UIBuilder);

        _ComponentBuilder = new ComponentFactory(this, _ComponentSpokesman);

        XmlSave = new XmlEntity(this);

        _SystemBuilder = new SystemFactory(this, _ComponentBuilder, XmlSave);
        _SystemBuilder.createConfigurations();

        EntityBuilder = new EntityFactory(_ComponentBuilder, XmlSave, _ComponentSpokesman);
        _MeshBuilder = new MeshFactory(this);

        _SystemBuilder.createMainSystems(UIBuilder._SpriteBatch);

        SceneBuilder = new SceneFactory(this, _SystemBuilder._SceneSystem);
        SceneBuilder.changeScene(getEntry().getName());

        EngineInfo.makeBasicEntities(this, UIBuilder);
        EngineInfo.makeOptionalEntities(this);

        _SystemBuilder.createUISystems();

        UIBuilder.createMultiplexer();

        _SystemBuilder.sendConfigurations(EntityBuilder);
    }

    public void remove() {
        EntityBuilder.clear();
        _ComponentBuilder.clear();
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(EngineInfo.BackgroundColor.r, EngineInfo.BackgroundColor.g, EngineInfo.BackgroundColor.b,
                EngineInfo.BackgroundColor.a);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        update(Gdx.graphics.getDeltaTime());
        UIBuilder.resize((int) EngineInfo.ScreenWidth, (int) EngineInfo.ScreenHeight);
    }

    @Override
    public void resize(int w, int h) {
        EngineInfo.makeBasicEntities(this, UIBuilder);
    }

    public void restart() {
        EntityBuilder.clear();
        EngineInfo.makeBasicEntities(this, UIBuilder);
    }

    protected void update(float delta) {
        if (EngineInfo.Debug && EntityBuilder.getByName("~CAMERA") == null) {
            EngineInfo.makeBasicEntities(this, UIBuilder);
        }
        _SystemBuilder.updateSystems();
        try {
            long time = (long) (1000 / EngineInfo.Fps - Gdx.graphics.getDeltaTime());
            Thread.sleep(time < 0 ? 0 : time);
        } catch (InterruptedException e) {
        }
    }
}
