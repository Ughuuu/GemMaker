package com.gem.engine;

import java.util.List;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.gem.action.CommandFactory;
import com.gem.asset.Asset;
import com.gem.asset.AssetFactory;
import com.gem.asset.MeshFactory;
import com.gem.component.ComponentBase;
import com.gem.component.ComponentFactory;
import com.gem.component.ui.ComponentUILayout;
import com.gem.component.ui.ComponentUIWidget;
import com.gem.debug.Debugger;
import com.gem.entity.CollidableFactory;
import com.gem.entity.ComponentSpokesman;
import com.gem.entity.Entity;
import com.gem.entity.EntityFactory;
import com.gem.entity.XmlEntity;
import com.gem.scene.Scene;
import com.gem.scene.SceneFactory;

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
public abstract class Gem extends ApplicationAdapter {
	public MeshFactory meshBuilder;
	public CollidableFactory collidableBuilder;
	public EntityFactory entityBuilder;
	public AssetFactory loader;
	protected ComponentFactory componentFactory;
	protected SystemFactory systemBuilder;
	protected SceneFactory SceneBuilder;
	private ComponentSpokesman componentSpokesman;
	private UIFactory UIBuilder;
	private XmlEntity XmlSave;

	public void changeScene(String newScene) {
		SceneBuilder.changeScene(newScene);
	}

	@Override
	public void create() {
		if (EngineInfo.Debug)
			CommandFactory.factory.setGem(this);
		init();
	}

	public <T> Asset<T> getAsset(String name) {
		return loader.getAsset(name);
	}

	public Scene getCurrentScene() {
		return systemBuilder.sceneSystem.getScene();
	}

	public Entity getEntity(int id) {
		return entityBuilder.getById(id);
	}

	public Entity getEntity(String name) {
		return entityBuilder.getByName(name);
	}

	public abstract Class<?> getEntry();

	public void init() {
		if (EngineInfo.Debug)
			Debugger.setGem(this);
		Gdx.graphics.setVSync(false);
		loader = new AssetFactory(this);

		UIBuilder = new UIFactory(this);
		EngineInfo.makeBasicEntities(this, UIBuilder);
		componentSpokesman = new ComponentSpokesman(UIBuilder);

		componentFactory = new ComponentFactory(this, componentSpokesman);

		XmlSave = new XmlEntity(this);

		systemBuilder = new SystemFactory(this, componentFactory, XmlSave);

		entityBuilder = new EntityFactory(componentFactory, XmlSave, componentSpokesman);
		meshBuilder = new MeshFactory(this);

		systemBuilder.createMainSystems(UIBuilder._SpriteBatch);

		SceneBuilder = new SceneFactory(this, systemBuilder.sceneSystem);
		SceneBuilder.changeScene(getEntry().getName());

		EngineInfo.makeOptionalEntities(this);

		systemBuilder.createUISystems();

		UIBuilder.createMultiplexer();

		systemBuilder.sendConfigurations(entityBuilder);
	}

	public void remove() {
		entityBuilder.clear();
		componentFactory.clear();
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
		systemBuilder.resize(w, h);
		EngineInfo.makeBasicEntities(this, UIBuilder);
	}

	public void restart() {
		entityBuilder.clear();
		systemBuilder.restart();
		EngineInfo.makeBasicEntities(this, UIBuilder);
	}

	protected void update(float delta) {
		systemBuilder.updateSystems();
		try {
			long time = (long) (1000 / EngineInfo.Fps - Gdx.graphics.getDeltaTime());
			Thread.sleep(time < 0 ? 0 : time);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Goes upwards
	 *
	 * @param ent
	 * @param cls
	 * @return
	 */
	public static <T extends ComponentBase> T goFindUpWithComponent(Entity ent, Class<T> cls) {
		if (ent == null) {
			return null;
		}
		if (!ent.hasComponent(cls)) {
			return goUpForComponent(ent, cls);
		}
		return ent.getComponent(cls);
	}

	/**
	 * Goes upwards
	 *
	 * @param ent
	 * @param cls
	 * @return
	 */
	public static <T extends ComponentBase> T goUpForComponent(Entity ent, Class<T> cls) {
		Entity parent = ent.getParent();
		if (parent == null) {
			return null;
		}
		if (parent != null && !parent.hasComponent(cls)) {
			return goUpForComponent(parent, cls);
		}
		return parent.getComponent(cls);
	}

	/**
	 * Goes downwards on leafs.
	 *
	 * @param ent
	 * @param cls
	 * @return
	 */
	public static List<Entity> goDownFind(Entity ent, Class<?> cls) {
		List<Entity> children = ent.getChildren();
		for (Entity child : children) {
			if (!child.hasComponent(ComponentUILayout.class) && !child.hasComponent(ComponentUIWidget.class)) {
				children.remove(child);
				List<Entity> subChildren = child.getChildren();
				for (Entity subChild : subChildren) {
					children.addAll(goDownFind(subChild, cls));
				}
				children.addAll(subChildren);
			}
		}
		return children;
	}
}
