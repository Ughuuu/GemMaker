package com.ngeen.engine;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.ngeen.asset.Asset;
import com.ngeen.asset.AssetFactory;
import com.ngeen.asset.MeshFactory;
import com.ngeen.component.ComponentFactory;
import com.ngeen.component.XmlComponent;
import com.ngeen.debug.Debugger;
import com.ngeen.entity.CollidableFactory;
import com.ngeen.entity.Entity;
import com.ngeen.entity.EntityFactory;
import com.ngeen.entity.XmlEntity;
import com.ngeen.scene.SceneFactory;

/**
 * Main engine class. Links all elements and holds entities.
 * <img src="img/Ngeen.png"/>
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
public abstract class Ngeen extends ApplicationAdapter{
	public AssetFactory Loader;
	public EntityFactory EntityBuilder;
	public MeshFactory _MeshBuilder;
	public XmlEntity XmlSave;
	public UIFactory UIBuilder;
	public CollidableFactory CollidableBuilder;

	protected ComponentFactory _ComponentBuilder;

	protected SceneFactory SceneBuilder;
	protected SystemFactory _SystemBuilder;

	private XmlComponent _XmlComponent;

	public void init() {
		Loader = new AssetFactory(this);
		_ComponentBuilder = new ComponentFactory(this);
		_SystemBuilder = new SystemFactory(this, _ComponentBuilder);
		_SystemBuilder.createConfigurations();
		EntityBuilder = new EntityFactory(this, _ComponentBuilder, _SystemBuilder);
		_MeshBuilder = new MeshFactory(this);
		UIBuilder = new UIFactory(this);

		_SystemBuilder.createMainSystems(UIBuilder._SpriteBatch);

		SceneBuilder = new SceneFactory(this, _SystemBuilder._SceneSystem);
		SceneBuilder.changeScene(getEntry().getName());

		EngineInfo.makeBasicEntities(this);
		EngineInfo.makeOptionalEntities(this);

		_SystemBuilder.createUISystems();

		UIBuilder.createMultiplexer();

		_SystemBuilder.sendConfigurations(EntityBuilder);

		_XmlComponent = new XmlComponent();
		XmlSave = new XmlEntity(this, _XmlComponent);
	}

	public void update(float delta) {
		if (EngineInfo.Debug && EntityBuilder.getByName("~CAMERA") == null) {
			EngineInfo.makeBasicEntities(this);
		}
		_SystemBuilder.updateSystems();
	}

	public void restart() {
		EntityBuilder.clear();
		EngineInfo.makeBasicEntities(this);
	}

	@Override
	public void create() {
		init();
	}

	@Override
	public void render() {
		Gdx.gl.glClearColor(EngineInfo.BackgroundColor.r, EngineInfo.BackgroundColor.g, EngineInfo.BackgroundColor.b,
				EngineInfo.BackgroundColor.a);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		update(Gdx.graphics.getDeltaTime());
	}

	@Override
	public void resize(int w, int h) {
		EngineInfo.makeBasicEntities(this);
		UIBuilder.resize(w, h);
	}

	public void remove() {
		EntityBuilder.clear();
		_ComponentBuilder.clear();
	}

	public Class<?> getCurrentScene() {
		return _SystemBuilder._SceneSystem.getScene();
	}

	public void changeScene(String newScene) {
		SceneBuilder.changeScene(newScene);
	}

	public Entity getEntity(String name) {
		return EntityBuilder.getByName(name);
	}

	public Entity getEntity(int id) {
		return EntityBuilder.getById(id);
	}

	public <T> Asset<T> getAsset(String name) {
		return Loader.getAsset(name);
	}
	
	public abstract Class<?> getEntry();
}
