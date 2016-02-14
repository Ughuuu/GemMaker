package com.ngeen.engine;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.ngeen.asset.Asset;
import com.ngeen.asset.AssetFactory;
import com.ngeen.asset.MeshFactory;
import com.ngeen.component.ComponentFactory;
import com.ngeen.component.XmlComponent;
import com.ngeen.entity.CollidableFactory;
import com.ngeen.entity.Entity;
import com.ngeen.entity.EntityFactory;
import com.ngeen.entity.XmlEntity;
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
	private MeshFactory _MeshBuilder;
	private CollidableFactory CollidableBuilder;
	private EntityFactory EntityBuilder;
	private AssetFactory Loader;
	private UIFactory UIBuilder;
	private XmlEntity XmlSave;

	private XmlComponent _XmlComponent;

	protected ComponentFactory _ComponentBuilder;
	protected SystemFactory _SystemBuilder;

	protected SceneFactory SceneBuilder;

	public void changeScene(String newScene) {
		SceneBuilder.changeScene(newScene);
	}

	@Override
	public void create() {
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
	}

	@Override
	public void resize(int w, int h) {
		EngineInfo.makeBasicEntities(this);
		UIBuilder.resize(w, h);
	}

	public void restart() {
		EntityBuilder.clear();
		EngineInfo.makeBasicEntities(this);
	}

	public void update(float delta) {
		if (EngineInfo.Debug && EntityBuilder.getByName("~CAMERA") == null) {
			EngineInfo.makeBasicEntities(this);
		}
		_SystemBuilder.updateSystems();
	}
}
