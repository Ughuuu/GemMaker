package com.ngeen.holder;

import java.util.List;

import com.artemis.BaseSystem;
import com.artemis.Entity;
import com.artemis.World;
import com.artemis.managers.GroupManager;
import com.artemis.managers.TagManager;
import com.artemis.utils.ImmutableBag;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.input.GestureDetector.GestureListener;
import com.badlogic.gdx.scenes.scene2d.ui.TextArea;
import com.ngeen.components.CameraComponent;
import com.ngeen.factories.CollidableFactory;
import com.ngeen.helper.EntityHelper;
import com.ngeen.helper.InputHelper;
import com.ngeen.helper.SaveHelper;
import com.ngeen.load.Loader;
import com.ngeen.manager.EntityLogger;
import com.ngeen.scene.LoadScene;
import com.ngeen.scene.Scene;
import com.ngeen.systems.AnimateSystem;
import com.ngeen.systems.ButtonSystem;
import com.ngeen.systems.LogSystem;
import com.ngeen.systems.OverlaySystem;
import com.ngeen.systems.PhysicsSystem;
import com.ngeen.systems.RenderSystem;
import com.ngeen.systems.SceneSystem;
import com.ngeen.systems.TransformSystem;

public class Ngeen {
	private World engine;
	private BaseSystem animateSystem, buttonSystem, physicSystem, renderSystem,
			sceneSystem, transformSystem, logSystem, overlaySystem;

	public EntityHelper entityHelper;
	public InputHelper inputHelper;
	public SaveHelper saveHelper;

	private Loader load;

	private List<Entity> selected;

	public Entity getByTag(String tag) {
		return engine.getManager(TagManager.class).getEntity(tag);
	}

	public Entity getById(int id) {
		return engine.getEntity(id);
	}

	public ImmutableBag<Entity> getByGroup(String group) {
		return engine.getManager(GroupManager.class).getEntities(group);
	}

	public void clear() {
	}

	void addDummyEntities() {
		Entity cameraEntity = entityHelper.createPositional("~CAMERA",
				"~ENGINE");
		CameraComponent camera = cameraEntity.edit().create(
				CameraComponent.class);
		camera.camera = new OrthographicCamera(Constant.W, Constant.H);
		Constant.CAMERA = camera.camera;
	}

	public void zoom() {
		if (Constant.ZOOM < 0.1f)
			Constant.ZOOM = 0.1f;
		if (Constant.ZOOM > 2)
			Constant.ZOOM = 2;
		Constant.CAMERA.viewportHeight = Constant.H * Constant.ZOOM;
		Constant.CAMERA.viewportWidth = Constant.W * Constant.ZOOM;
		Constant.CAMERA.update();
	}

	public GestureListener getGestureListener() {
		return (GestureListener) sceneSystem;
	}

	public InputProcessor getInputProcessor() {
		return (InputProcessor) sceneSystem;
	}

	public void init() {
		Scene.ng = this;
		Constant.DEBUG_FONT = new BitmapFont(
				Gdx.files.internal("font/AmaticSC-Regular.fnt"));

		inputHelper = new InputHelper();
		saveHelper = new SaveHelper();

		engine = new World();
		engine.setManager(new TagManager());
		engine.setManager(new GroupManager());
		if (Constant.DEBUG) {
			engine.setManager(new EntityLogger());
		}

		animateSystem = new AnimateSystem();
		buttonSystem = new ButtonSystem();
		physicSystem = new PhysicsSystem();
		renderSystem = new RenderSystem();
		sceneSystem = new SceneSystem();
		transformSystem = new TransformSystem();

		engine.setSystem(animateSystem);
		engine.setSystem(buttonSystem);
		engine.setSystem(physicSystem);
		engine.setSystem(renderSystem);
		engine.setSystem(sceneSystem);
		engine.setSystem(transformSystem);

		if (Constant.DEBUG) {
			logSystem = new LogSystem();
			overlaySystem = new OverlaySystem();
			engine.setSystem(logSystem);
			engine.setSystem(overlaySystem);
		}

		engine.initialize();
		entityHelper = new EntityHelper(engine);
		load = new Loader(entityHelper);

		addDummyEntities();
	}

	public void update(float delta) {
		engine.setDelta(delta);
		engine.process();
	}

	public Ngeen() {
		init();
	}

	public void restart() {
	}

	public void dispose() {
		Constant.BATCH.dispose();
		Constant.DEBUG_FONT.dispose();
	}

	/*
	 * public void checkClick(int x, int y, final String s) { Vector3 testPoint
	 * = new Vector3(x, y, 0); inUse.unproject(testPoint);
	 * 
	 * move.world.QueryAABB(new QueryCallback() {
	 * 
	 * @Override public boolean reportFixture(Fixture fixture) { ClickState
	 * state = (ClickState) fixture.getBody().getUserData(); if
	 * (s.compareTo("ClickDown") == 0) { state.down = true; state.up = false; }
	 * if (s.compareTo("ClickUp") == 0) { state.up = true; state.down = false; }
	 * return true; } }, testPoint.x - 0.1f, testPoint.y - 0.1f, testPoint.x +
	 * 0.1f, testPoint.y + 0.1f); }
	 * 
	 * @Override public boolean keyDown(int keycode) { if (keycode ==
	 * Keys.ENTER) { DEBUG = !DEBUG; } if (keycode == Keys.SPACE) { try {
	 * save(); } catch (IOException e) { // TODO Auto-generated catch block
	 * e.printStackTrace(); } } return false; }
	 */
}
