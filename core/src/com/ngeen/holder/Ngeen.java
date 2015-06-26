package com.ngeen.holder;

import com.artemis.BaseSystem;
import com.artemis.Entity;
import com.artemis.World;
import com.artemis.managers.GroupManager;
import com.artemis.managers.TagManager;
import com.artemis.utils.ImmutableBag;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.ngeen.factories.CollidableFactory;
import com.ngeen.helper.EntityHelper;
import com.ngeen.helper.InputHelper;
import com.ngeen.helper.RenderHelper;
import com.ngeen.helper.SaveHelper;
import com.ngeen.load.Loader;
import com.ngeen.systems.AnimateSystem;
import com.ngeen.systems.ButtonSystem;
import com.ngeen.systems.PhysicsSystem;
import com.ngeen.systems.RenderSystem;
import com.ngeen.systems.SceneSystem;
import com.ngeen.systems.TransformSystem;

public class Ngeen {
	public World engine;
	public BaseSystem animateSystem, buttonSystem, physicSystem, renderSystem, sceneSystem, transformSystem;

	public EntityHelper entityHelper;
	public InputHelper inputHelper;
	public SaveHelper saveHelper;
	public RenderHelper renderHelper;

	public Loader load;

	public Entity getByTag(String tag) {
		return engine.getManager(TagManager.class).getEntity(tag);
	}
	
	public Entity getById(int id){
		return engine.getEntity(id);
	}
	
	public ImmutableBag<Entity> getByGroup(String group){
		return engine.getManager(GroupManager.class).getEntities(group);
	}
	
	public void clear() {
	}

	public void init() {
		load = new Loader();
		entityHelper = new EntityHelper();
		inputHelper = new InputHelper();
		saveHelper = new SaveHelper();
		renderHelper = new RenderHelper();
		
		engine = new World();		
		engine.setManager(new TagManager());
		engine.setManager(new GroupManager());
		
		animateSystem = new AnimateSystem();
		buttonSystem = new ButtonSystem();
		physicSystem = new PhysicsSystem();
		renderSystem = new RenderSystem(renderHelper.batch);
		sceneSystem = new SceneSystem();
		transformSystem = new TransformSystem();
		
		engine.setSystem(animateSystem);
		engine.setSystem(buttonSystem);
		engine.setSystem(physicSystem);
		engine.setSystem(renderSystem);
		engine.setSystem(sceneSystem);
		engine.setSystem(transformSystem);
	}

	public void update(float delta) {
	}

	public Ngeen() {
		init();
	}

	public void restart() {		
	}

	public static void afterLoad() {
		// TODO Auto-generated method stub
		
	}
	
	/*
	public void checkClick(int x, int y, final String s) {
		Vector3 testPoint = new Vector3(x, y, 0);
		inUse.unproject(testPoint);

		move.world.QueryAABB(new QueryCallback() {
			@Override
			public boolean reportFixture(Fixture fixture) {
				ClickState state = (ClickState) fixture.getBody().getUserData();
				if (s.compareTo("ClickDown") == 0) {
					state.down = true;
					state.up = false;
				}
				if (s.compareTo("ClickUp") == 0) {
					state.up = true;
					state.down = false;
				}
				return true;
			}
		}, testPoint.x - 0.1f, testPoint.y - 0.1f, testPoint.x + 0.1f,
				testPoint.y + 0.1f);
	}

	@Override
	public boolean keyDown(int keycode) {
		if (keycode == Keys.ENTER) {
			DEBUG = !DEBUG;
		}
		if (keycode == Keys.SPACE) {
			try {
				save();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return false;
	}
	*/
}
