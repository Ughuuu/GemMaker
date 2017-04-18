package com.gem.systems;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.input.GestureDetector.GestureListener;
import com.badlogic.gdx.math.Vector2;
import com.gem.component.ComponentScript;
import com.gem.component.Script;
import com.gem.debug.Debugger;
import com.gem.engine.EngineInfo;
import com.gem.engine.Gem;
import com.gem.entity.Entity;
import com.gem.entity.XmlEntity;
import com.gem.scene.Scene;

import java.awt.Component;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Dragos
 * @composed 1 - 1 Scene
 */
public class SystemScene extends SystemBase implements GestureListener, InputProcessor {
	private final XmlEntity xmlFactory;
	private Scene requestChange;
	private Scene scene;
	private Set<ComponentScript> toUpdateScripts = new HashSet<ComponentScript>();
	private Set<ComponentScript> otherScripts = new HashSet<ComponentScript>();

	public SystemScene(Gem ng, SystemConfiguration conf, XmlEntity xml) {
		super(ng, conf);
		xmlFactory = xml;
	}

	@Override
	public boolean fling(float x, float y, int button) {
		if (scene != null)
			scene.onFling(x, y);
		return false;
	}

	public Scene getScene() {
		return scene;
	}

	public void setScene(Scene sc) {
		if (sc != null) {
			this.requestChange = sc;
		}
	}

	@Override
	public boolean keyDown(int keycode) {
		if (scene != null)
			scene.onKeyDown(keycode);
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		// UNUSED
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		if (scene != null)
			scene.onKeyUp(keycode);
		return false;
	}

	@Override
	public boolean longPress(float x, float y) {
		if (scene != null)
			scene.onLongPress(x, y);
		return false;
	}

	@Override
	public boolean mouseMoved(int x, int y) {
		// UNUSED
		return false;
	}

	@Override
	public void onBeforeUpdate() {
		if (scene != null && EngineInfo.Debug) {
			xmlFactory.update();
		}
		if (requestChange != null) {
			if (scene != null)
				scene.onExit();
			scene = requestChange;
			xmlFactory.Load();
			requestChange = null;
			scene.onInit();
		}
		if (scene != null)
			scene.onUpdate(deltaTime);
	}

	@Override
	public void onUpdate(Set<Entity> entities) {
		if (EngineInfo.Debug) {
			toUpdateScripts.clear();
			otherScripts.clear();
			for (Entity ent : entities) {
				List<ComponentScript> entScripts = ent.getComponents(ComponentScript.class);
				// find if scripts need to be changed
				for (ComponentScript scriptComponent : entScripts) {
					if (scriptComponent.shouldUpdate()) {
						toUpdateScripts.add(scriptComponent);
					} else {
						otherScripts.add(scriptComponent);
					}
				}
			}
		}
		if (EngineInfo.Debug && !toUpdateScripts.isEmpty()) {
			// reload all scrips that changed
			ComponentScript currentScript = toUpdateScripts.iterator().next();
			try {
				toUpdateScripts.remove(currentScript);
				String err = currentScript.update(toUpdateScripts);
				if (!err.equals("")) {
					System.out.println(err);
				}
				currentScript.reloadOthers(otherScripts);
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				currentScript.runInit();
			} catch (Exception e) {
				e.printStackTrace();
			}
			for (ComponentScript script : toUpdateScripts) {
				try {
					script.runInit();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			for (ComponentScript script : otherScripts) {
				try {
					script.runInit();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		for (Entity ent : entities) {
			List<ComponentScript> entScripts = ent.getComponents(ComponentScript.class);
			for (ComponentScript scriptComponent : entScripts) {
				if (scriptComponent.isValid()) {
					Script script = scriptComponent.getScript();
					try {
						script.onBeforeUpdate();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
		for (Entity ent : entities) {
			List<ComponentScript> entScripts = ent.getComponents(ComponentScript.class);
			for (ComponentScript scriptComponent : entScripts) {
				if (scriptComponent.isValid()) {
					Script script = scriptComponent.getScript();
					try {
						script.onUpdate(deltaTime);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
		for (Entity ent : entities) {
			List<ComponentScript> entScripts = ent.getComponents(ComponentScript.class);
			for (ComponentScript scriptComponent : entScripts) {
				if (scriptComponent.isValid()) {
					Script script = scriptComponent.getScript();
					try {
						script.onAfterUpdate();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

	@Override
	public boolean pan(float x, float y, float deltaX, float deltaY) {
		if (scene != null)
			scene.onPan(x, y, deltaX, deltaY);
		return false;
	}

	@Override
	public boolean panStop(float x, float y, int pointer, int button) {
		if (scene != null)
			scene.onPanStop(x, y);
		return false;
	}

	@Override
	public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void pinchStop() {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean scrolled(int amount) {
		// UNUSED
		return false;
	}

	@Override
	public boolean tap(float x, float y, int count, int button) {
		if (scene != null)
			scene.onTap(x, y, count);
		return false;
	}

	@Override
	public boolean touchDown(float x, float y, int pointer, int button) {
		if (scene != null)
			scene.onTouchDown(x, y, pointer);
		return false;
	}

	@Override
	public boolean touchDown(int x, int y, int pointer, int button) {
		// UNUSED
		return false;
	}

	@Override
	public boolean touchDragged(int x, int y, int pointer) {
		if (scene != null)
			scene.onTouchDrag(x, y, pointer);
		return false;
	}

	@Override
	public boolean touchUp(int x, int y, int pointer, int button) {
		if (scene != null)
			scene.onTouchUp(x, y, pointer);
		return false;
	}

	@Override
	public boolean zoom(float initialDistance, float distance) {
		if (scene != null)
			scene.onZoom(initialDistance, distance);
		return false;
	}
}
