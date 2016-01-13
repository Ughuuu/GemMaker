package com.ngeen.engine;

import java.util.Set;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.input.GestureDetector.GestureListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.ngeen.asset.Asset;
import com.ngeen.asset.AssetFactory;
import com.ngeen.asset.MeshFactory;
import com.ngeen.component.*;
import com.ngeen.component.ui.widget.*;
import com.ngeen.debug.Debugger;
import com.ngeen.entity.Entity;
import com.ngeen.entity.EntityFactory;
import com.ngeen.entity.XmlEntity;
import com.ngeen.asset.AssetFactory;
import com.ngeen.scene.Scene;
import com.ngeen.scene.SceneFactory;
import com.ngeen.systems.*;

/**
 * Main engine class. Links all elements and holds entities.
 * 
 * @author Dragos
 *
 */
public class Ngeen extends ApplicationAdapter {
	public AssetFactory Loader;
	public EntityFactory EntityBuilder;
	public MeshFactory _MeshBuilder;
	public XmlEntity XmlSave;
	public UIFactory UIBuilder;

	protected ComponentFactory ComponentBuilder;

	protected SceneFactory SceneBuilder;
	protected SystemFactory _SystemBuilder;

	private XmlComponent _XmlComponent;

	public void init() {
		_SystemBuilder = new SystemFactory(this);
		_SystemBuilder.createConfigurations();
		Loader = new AssetFactory(this);
		ComponentBuilder = new ComponentFactory(this);
		EntityBuilder = new EntityFactory(this, ComponentBuilder, _SystemBuilder);
		_MeshBuilder = new MeshFactory(this);
		UIBuilder = new UIFactory(this);

		_SystemBuilder.createMainSystems(UIBuilder._SpriteBatch);		

		SceneBuilder = new SceneFactory(this, _SystemBuilder._SceneSystem);
		SceneBuilder.changeScene("LoadScene");
		
		EngineInfo.makeBasicEntities(this);
		EngineInfo.makeOptionalEntities(this);
		
		_SystemBuilder.createUISystems();
		
		UIBuilder.createMultiplexer();

		_SystemBuilder.sendConfigurations(EntityBuilder);

		_XmlComponent = new XmlComponent();
		XmlSave = new XmlEntity(this, _XmlComponent);
	}

	public void update(float delta) {
		Loader.done();
		_SystemBuilder.updateSystems();
	}

	public void restart() {
		remove();
	}

	@Override
	public void create() {
		init();
		Loader.scoutFiles();
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
		UIBuilder.resize(w,h);
	}

	public void remove() {
		EntityBuilder.clear();
		ComponentBuilder.clear();
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
}
