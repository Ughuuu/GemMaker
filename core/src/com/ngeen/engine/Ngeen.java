package com.ngeen.engine;

import java.util.List;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.input.GestureDetector.GestureListener;
import com.badlogic.gdx.scenes.scene2d.ui.TextArea;
import com.ngeen.component.CameraComponent;
import com.ngeen.entity.Entity;
import com.ngeen.factories.CollidableFactory;
import com.ngeen.factories.InputFactory;
import com.ngeen.factories.LoaderFactory;
import com.ngeen.scene.LoadScene;
import com.ngeen.scene.Scene;
import com.ngeen.systems.DebugSystem;
import com.ngeen.systems.OverlaySystem;
import com.ngeen.systems.PhysicsSystem;
import com.ngeen.systems.RenderSystem;
import com.ngeen.systems.SceneSystem;
import com.ngeen.systems.TransformSystem;

/**
 * Main engine class. Links all elements and holds entities.
 * @author Dragos
 *
 */
public class Ngeen extends ApplicationAdapter {
	private GestureListener _SceneSystem, _EditorInput;
	private LoaderFactory _Loader;
	private Debugger _Debug;
	public static SpriteBatch BATCH;

	/**
	 * Get entity by name.
	 * @param tag The name of the object.
	 * @return
	 */
	public Entity getByName(String tag) {
		return null;
	}

	/**
	 * Get entity by it's id.
	 * @param id
	 * @return
	 */
	public Entity getById(int id) {
		return null;
	}

	/**
	 * Remove entity by id.
	 * @param id
	 */
	public void removeEntity(int id) {
	}

	private void addDummyEntities() {
		/*
		 * Entity cameraEntity = entityHelper.createPositional("~CAMERA",
		 * "~ENGINE"); CameraComponent camera =
		 * cameraEntity.edit().create(CameraComponent.class); camera.camera =
		 * new OrthographicCamera(Constant.W, Constant.H); Constant.CAMERA =
		 * cameraEntity;
		 * 
		 * cameraEntity = entityHelper.createPositional("~UI_CAMERA",
		 * "~ENGINE"); camera =
		 * cameraEntity.edit().create(CameraComponent.class); camera.camera =
		 * new OrthographicCamera(Constant.W, Constant.H);
		 * camera.camera.translate(Constant.W / 2, Constant.H / 2, 0);
		 * camera.camera.update(); Constant.UI_CAMERA = cameraEntity;
		 */
	}

	public void zoom() {
		/*
		 * if (Constant.ZOOM < 0.1f) Constant.ZOOM = 0.1f; if (Constant.ZOOM >
		 * 2) Constant.ZOOM = 2;
		 * Constant.CAMERA.getComponent(CameraComponent.class).camera.
		 * viewportHeight = Constant.H * Constant.ZOOM;
		 * Constant.CAMERA.getComponent(CameraComponent.class).camera.
		 * viewportWidth = Constant.W * Constant.ZOOM;
		 * Constant.CAMERA.getComponent(CameraComponent.class).camera.update();
		 */
	}

	public void load(String name) {
		LoaderFactory.preLoadAll(name);
	}

	public void init() {
		Scene.ng = this;
		Constant.DEBUG_FONT = new BitmapFont(Gdx.files.internal("data/font/AmaticSC-Regular.fnt"));

		Constant.MANAGER = new AssetManager();

		final InputMultiplexer multiplexer = new InputMultiplexer();
		if (Constant.DEBUG) {
			Integer ii = new Integer(17);
			debug = new Debugger(ii, this);
			multiplexer.addProcessor(debug);
		}
		multiplexer.addProcessor((InputProcessor) editorInput);
		multiplexer.addProcessor((InputProcessor) this);
		multiplexer.addProcessor(new GestureDetector(sceneSystem));
		Gdx.input.setInputProcessor(multiplexer);

		addDummyEntities();
	}

	public void setScene(Scene sc) {
		((SceneSystem) sceneSystem).setScene(sc);
	}

	public void afterLoad(String name) {
		Debugger.println("Started loading: \"" + name + "\" folder");
	}

	public void update(float delta) {
		LoaderFactory.done();
	}

	public void restart() {
	}

	@Override
	public void create() {
		init();
	}

	@Override
	public void render() {
		Gdx.gl.glClearColor(Constant.BACKGROUND_COLOR.r, Constant.BACKGROUND_COLOR.g, Constant.BACKGROUND_COLOR.b,
				Constant.BACKGROUND_COLOR.a);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		update(Gdx.graphics.getDeltaTime());
	}

	@Override
	public void resize(int w, int h) {
	}

	public void dispose() {
		Constant.BATCH.dispose();
	}
}
