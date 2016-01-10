package com.ngeen.systems;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.input.GestureDetector.GestureListener;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.TimeUtils;
import com.ngeen.engine.EngineInfo;
import com.ngeen.engine.Ngeen;
import com.ngeen.entity.Entity;
import com.ngeen.scene.Scene;

public class SceneSystem extends SystemBase implements GestureListener, InputProcessor {
	private Scene scene;
	
	public SceneSystem(Ngeen ng) {
		super(ng);
	}
	
	public void setScene(Scene sc){
		if(sc!=null){
			if(scene!=null)
			scene.onExit();
			this.scene = sc;
			scene.onInit();
		}
	}
	
	@Override
	public void onBeforeUpdate(){
		if (scene != null)
			scene.onUpdate(deltaTime);
	}
	
	@Override
	public boolean touchDown(float x, float y, int pointer, int button) {
		x = x / (float) Gdx.graphics.getWidth() * EngineInfo.Width;
		y = EngineInfo.Height - y / (float) Gdx.graphics.getHeight() * EngineInfo.Height;
		if (EngineInfo.Debug) {
			SystemOverlay.x1 = x;
			SystemOverlay.y1 = y;
			SystemOverlay.x2 = x;
			SystemOverlay.y2 = y;
		}
		if (scene != null)
			scene.onTouchDown(x, y, pointer);
		return false;
	}

	@Override
	public boolean touchDragged(int x, int y, int pointer) {
		x = (int) (x / (float) Gdx.graphics.getWidth() * EngineInfo.Width);
		y = (int) (EngineInfo.Height - y / (float) Gdx.graphics.getHeight() * EngineInfo.Height);
		if (EngineInfo.Debug) {
			SystemOverlay.x2 = x;
			SystemOverlay.y2 = y;
		}
		if (scene != null)
			scene.onTouchDrag(x, y, pointer);
		return false;
	}

	@Override
	public boolean touchUp(int x, int y, int pointer, int button) {
		x = (int) (x / (float) Gdx.graphics.getWidth() * EngineInfo.Width);
		y = (int) (EngineInfo.Height - y / (float) Gdx.graphics.getHeight() * EngineInfo.Height);
		if (EngineInfo.Debug) {
			SystemOverlay.x1 = -1;
			SystemOverlay.y1 = -1;
			SystemOverlay.x2 = -1;
			SystemOverlay.y2 = -1;
		}
		if (scene != null)
			scene.onTouchUp(x, y, pointer);
		return false;
	}

	@Override
	public boolean tap(float x, float y, int count, int button) {
		x = x / (float) Gdx.graphics.getWidth() * EngineInfo.Width;
		y = EngineInfo.Height - y / (float) Gdx.graphics.getHeight() * EngineInfo.Height;
		if (scene != null)
			scene.onTap(x, y, count);
		return false;
	}

	@Override
	public boolean longPress(float x, float y) {
		x = x / (float) Gdx.graphics.getWidth() * EngineInfo.Width;
		y = EngineInfo.Height - y / (float) Gdx.graphics.getHeight() * EngineInfo.Height;
		if (scene != null)
			scene.onLongPress(x, y);
		return false;
	}

	@Override
	public boolean fling(float x, float y, int button) {
		x = x / (float) Gdx.graphics.getWidth() * EngineInfo.Width;
		y = EngineInfo.Height - y / (float) Gdx.graphics.getHeight() * EngineInfo.Height;
		if (scene != null)
			scene.onFling(x, y);
		return false;
	}

	@Override
	public boolean pan(float x, float y, float deltaX, float deltaY) {
		x = x / (float) Gdx.graphics.getWidth() * EngineInfo.Width;
		y = EngineInfo.Height - y / (float) Gdx.graphics.getHeight() * EngineInfo.Height;
		deltaX = deltaX / Gdx.graphics.getWidth() * EngineInfo.Width;
		deltaY = deltaY / Gdx.graphics.getHeight() * EngineInfo.Height;
		if (scene != null)
			scene.onPan(x, y, deltaX, deltaY);
		return false;
	}

	@Override
	public boolean panStop(float x, float y, int pointer, int button) {
		x = x / (float) Gdx.graphics.getWidth() * EngineInfo.Width;
		y = EngineInfo.Height - y / (float) Gdx.graphics.getHeight() * EngineInfo.Height;
		if (scene != null)
			scene.onPanStop(x, y);
		return false;
	}

	@Override
	public boolean zoom(float initialDistance, float distance) {
		if (scene != null)
			scene.onZoom(initialDistance, distance);
		return false;
	}

	@Override
	public boolean keyDown(int keycode) {
		if (scene != null)
			scene.onKeyDown(keycode);
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		if (scene != null)
			scene.onKeyUp(keycode);
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		// UNUSED
		return false;
	}

	@Override
	public boolean touchDown(int x, int y, int pointer, int button) {
		// UNUSED
		return false;
	}

	@Override
	public boolean mouseMoved(int x, int y) {
		// UNUSED
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		// UNUSED
		return false;
	}

	@Override
	public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2) {
		// TODO Auto-generated method stub
		return false;
	}
}
