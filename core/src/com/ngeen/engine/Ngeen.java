package com.ngeen.engine;

import java.util.Set;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.input.GestureDetector.GestureListener;
import com.badlogic.gdx.utils.TimeUtils;
import com.ngeen.asset.AssetFactory;
import com.ngeen.asset.MeshFactory;
import com.ngeen.component.*;
import com.ngeen.debug.Debugger;
import com.ngeen.entity.Entity;
import com.ngeen.entity.EntityFactory;
import com.ngeen.entity.XmlEntity;
import com.ngeen.asset.AssetFactory;
import com.ngeen.scene.Scene;
import com.ngeen.scene.SceneFactory;
import com.ngeen.systems.SystemOverlay;
import com.ngeen.systems.SystemPhysics;
import com.ngeen.systems.SystemScene;
import com.ngeen.systems.SystemSprite;
import com.ngeen.systems.SystemBase;
import com.ngeen.systems.SystemCamera;
import com.ngeen.systems.SystemConfiguration;
import com.ngeen.systems.SystemDraw;

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

	protected SceneFactory SceneBuilder;
	private GestureListener _EditorInput;
	protected ComponentFactory ComponentBuilder;

	private SystemCamera _SystemCamera;
	private SystemScene _SceneSystem;
	private SystemOverlay _OverlaySystem;
	private SystemPhysics _PhysicsSystem;
	private SystemDraw _DrawingSystem;
	private SystemSprite _SpriteSystem;
	private XmlComponent _XmlComponent;

	private SystemConfiguration _PointConfiguration, _RigidConfiguration, _ScriptConfiguration, _DrawingConfiguration,
			_SpriteConfiguration, _CameraConfiguration;

	private void createConfigurations() {
		_PointConfiguration = new SystemConfiguration().all(ComponentPoint.class);
		_RigidConfiguration = new SystemConfiguration().all(ComponentPoint.class, ComponentRigid.class);
		_ScriptConfiguration = new SystemConfiguration().all(ComponentScript.class);
		_DrawingConfiguration = new SystemConfiguration().all(ComponentPoint.class, ComponentMesh.class,
				ComponentMaterial.class, ComponentVariable.class);
		_SpriteConfiguration = new SystemConfiguration().all(ComponentPoint.class, ComponentSprite.class,
				ComponentVariable.class);
		_CameraConfiguration = new SystemConfiguration().all(ComponentPoint.class, ComponentCamera.class);
	}

	private void sendConfigurations() {
		EntityBuilder.addSystem(_SceneSystem);
		EntityBuilder.addSystem(_OverlaySystem);
		EntityBuilder.addSystem(_PhysicsSystem);
		EntityBuilder.addSystem(_DrawingSystem);
		EntityBuilder.addSystem(_SpriteSystem);
		EntityBuilder.addSystem(_SystemCamera);
	}

	public void init() {
		createConfigurations();
		_EditorInput = new Editor(this);
		Loader = new AssetFactory(this);
		ComponentBuilder = new ComponentFactory(this);
		EntityBuilder = new EntityFactory(this, ComponentBuilder);
		_MeshBuilder = new MeshFactory(this);

		_PhysicsSystem = new SystemPhysics(this, _RigidConfiguration);
		_OverlaySystem = new SystemOverlay(this, _PointConfiguration);
		_SceneSystem = new SystemScene(this, _ScriptConfiguration);
		_DrawingSystem = new SystemDraw(this, _DrawingConfiguration);
		_SpriteSystem = new SystemSprite(this, _SpriteConfiguration);
		_SystemCamera = new SystemCamera(this, _CameraConfiguration);

		SceneBuilder = new SceneFactory(this, _SceneSystem);
		SceneBuilder.changeScene("LoadScene");
		final InputMultiplexer multiplexer = new InputMultiplexer();
		multiplexer.addProcessor((InputProcessor) _EditorInput);
		multiplexer.addProcessor(new GestureDetector(_SceneSystem));
		multiplexer.addProcessor((InputProcessor) _OverlaySystem);
		Gdx.input.setInputProcessor(multiplexer);

		sendConfigurations();
		EngineInfo.makeBasicEntities(this);
		_XmlComponent = new XmlComponent();
		XmlSave = new XmlEntity(this, _XmlComponent);
	}

	private void updateSystem(SystemBase system) {
		float time = TimeUtils.millis();
		system.onBeforeUpdate();
		Set<Entity> entities = EntityBuilder.getEntitiesForSystem(system);

		for (Entity entity : entities) {
			system.onUpdate(entity);
		}

		system.onAfterUpdate();
		system.deltaTime = TimeUtils.millis() - time;
	}

	public void update(float delta) {
		Loader.done();
		updateSystem(_SceneSystem);
		updateSystem(_PhysicsSystem);
		updateSystem(_DrawingSystem);
		updateSystem(_SpriteSystem);
		updateSystem(_OverlaySystem);
		updateSystem(_SystemCamera);
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
	}

	public void remove() {
		EntityBuilder.clear();
		ComponentBuilder.clear();
	}

	public Class<?> getCurrentScene() {
		return _SceneSystem.getScene();
	}
}
