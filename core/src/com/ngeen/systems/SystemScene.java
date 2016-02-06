package com.ngeen.systems;

import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.input.GestureDetector.GestureListener;
import com.badlogic.gdx.math.Vector2;
import com.ngeen.component.ComponentScript;
import com.ngeen.debug.Debugger;
import com.ngeen.engine.EngineInfo;
import com.ngeen.engine.Ngeen;
import com.ngeen.entity.Entity;
import com.ngeen.scene.Scene;

/**
 * @composed 1 - 1 Scene
 * @author Dragos
 *
 */
public class SystemScene extends SystemBase implements GestureListener, InputProcessor {
	private Scene _RequestChange;
	private Scene _Scene;

	public SystemScene(Ngeen ng, SystemConfiguration conf) {
		super(ng, conf);
	}

	@Override
	public boolean fling(float x, float y, int button) {
		x = x / Gdx.graphics.getWidth() * EngineInfo.Width;
		y = EngineInfo.Height - y / Gdx.graphics.getHeight() * EngineInfo.Height;
		if (_Scene != null)
			_Scene.onFling(x, y);
		return false;
	}

	public Class<?> getScene() {
		return _Scene.getClass();
	}

	@Override
	public boolean keyDown(int keycode) {
		if (_Scene != null)
			_Scene.onKeyDown(keycode);
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		// UNUSED
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		if (_Scene != null)
			_Scene.onKeyUp(keycode);
		return false;
	}

	@Override
	public boolean longPress(float x, float y) {
		x = x / Gdx.graphics.getWidth() * EngineInfo.Width;
		y = EngineInfo.Height - y / Gdx.graphics.getHeight() * EngineInfo.Height;
		if (_Scene != null)
			_Scene.onLongPress(x, y);
		return false;
	}

	@Override
	public boolean mouseMoved(int x, int y) {
		// UNUSED
		return false;
	}

	@Override
	public void onBeforeUpdate() {
		if (_Scene != null && EngineInfo.Debug) {
			_Ng.XmlSave.checkDate();
		}
		if (_RequestChange != null) {
			if (_Scene != null)
				_Scene.onExit();
			_Scene = _RequestChange;
			_Ng.XmlSave.Load();
			_RequestChange = null;
			Debugger.log("Loaded scene " + _Scene);
			_Scene.onInit();
		}
		if (_Scene != null)
			_Scene.onUpdate(deltaTime);
	}

	@Override
	public void onUpdate(Entity ent) {
		List<ComponentScript> scripts = ent.getComponents(ComponentScript.class);

		for (ComponentScript script : scripts) {
			if (script.isValid()) {
				script.getScript().onUpdate(deltaTime);
			}
		}
	}

	@Override
	public boolean pan(float x, float y, float deltaX, float deltaY) {
		x = x / Gdx.graphics.getWidth() * EngineInfo.Width;
		y = EngineInfo.Height - y / Gdx.graphics.getHeight() * EngineInfo.Height;
		deltaX = deltaX / Gdx.graphics.getWidth() * EngineInfo.Width;
		deltaY = deltaY / Gdx.graphics.getHeight() * EngineInfo.Height;
		if (_Scene != null)
			_Scene.onPan(x, y, deltaX, deltaY);
		return false;
	}

	@Override
	public boolean panStop(float x, float y, int pointer, int button) {
		x = x / Gdx.graphics.getWidth() * EngineInfo.Width;
		y = EngineInfo.Height - y / Gdx.graphics.getHeight() * EngineInfo.Height;
		if (_Scene != null)
			_Scene.onPanStop(x, y);
		return false;
	}

	@Override
	public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		// UNUSED
		return false;
	}

	public void setScene(Scene sc) {
		if (sc != null) {
			this._RequestChange = sc;
		}
	}

	@Override
	public boolean tap(float x, float y, int count, int button) {
		x = x / Gdx.graphics.getWidth() * EngineInfo.Width;
		y = EngineInfo.Height - y / Gdx.graphics.getHeight() * EngineInfo.Height;
		if (_Scene != null)
			_Scene.onTap(x, y, count);
		return false;
	}

	@Override
	public boolean touchDown(float x, float y, int pointer, int button) {
		x = x / Gdx.graphics.getWidth() * EngineInfo.Width;
		y = EngineInfo.Height - y / Gdx.graphics.getHeight() * EngineInfo.Height;
		if (EngineInfo.Debug) {
			SystemOverlay._X1 = x;
			SystemOverlay._Y1 = y;
			SystemOverlay._X2 = x;
			SystemOverlay._Y2 = y;
		}
		if (_Scene != null)
			_Scene.onTouchDown(x, y, pointer);
		return false;
	}

	@Override
	public boolean touchDown(int x, int y, int pointer, int button) {
		// UNUSED
		return false;
	}

	@Override
	public boolean touchDragged(int x, int y, int pointer) {
		x = (int) (x / (float) Gdx.graphics.getWidth() * EngineInfo.Width);
		y = (int) (EngineInfo.Height - y / (float) Gdx.graphics.getHeight() * EngineInfo.Height);
		if (EngineInfo.Debug) {
			SystemOverlay._X2 = x;
			SystemOverlay._Y2 = y;
		}
		if (_Scene != null)
			_Scene.onTouchDrag(x, y, pointer);
		return false;
	}

	@Override
	public boolean touchUp(int x, int y, int pointer, int button) {
		x = (int) (x / (float) Gdx.graphics.getWidth() * EngineInfo.Width);
		y = (int) (EngineInfo.Height - y / (float) Gdx.graphics.getHeight() * EngineInfo.Height);
		if (EngineInfo.Debug) {
			SystemOverlay._X1 = -1;
			SystemOverlay._Y1 = -1;
			SystemOverlay._X2 = -1;
			SystemOverlay._Y2 = -1;
		}
		if (_Scene != null)
			_Scene.onTouchUp(x, y, pointer);
		return false;
	}

	@Override
	public boolean zoom(float initialDistance, float distance) {
		if (_Scene != null)
			_Scene.onZoom(initialDistance, distance);
		return false;
	}
}
