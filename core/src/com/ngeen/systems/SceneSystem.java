package com.ngeen.systems;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.input.GestureDetector.GestureListener;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.TimeUtils;
import com.ngeen.engine.Constant;
import com.ngeen.engine.Ngeen;
import com.ngeen.entity.Entity;
import com.ngeen.scene.Scene;
import com.ngeen.scene.SceneFactory;

public class SceneSystem extends SystemBase implements GestureListener, InputProcessor {
	private Scene scene;
	private Ngeen _Ng;
	
	public SceneSystem(Ngeen ng, String sc) {
		_Ng = ng;
		setScene(_Ng.SceneBuilder.makeScene(sc));
	}
	
	public void setScene(Scene sc){
		if(sc!=null){
			sc.onInit();
			this.scene = sc;
		}
	}
	
	@Override
	public void onBeforeUpdate(){
		if (scene != null)
			scene.onUpdate(deltaTime);
	}
	
	@Override
	public boolean touchDown(float x, float y, int pointer, int button) {
		x = x / (float) Gdx.graphics.getWidth() * Constant.W;
		y = Constant.H - y / (float) Gdx.graphics.getHeight() * Constant.H;
		if (Constant.DEBUG) {
			OverlaySystem.x1 = x;
			OverlaySystem.y1 = y;
			OverlaySystem.x2 = x;
			OverlaySystem.y2 = y;
		}
		if (scene != null)
			scene.onTouchDown(x, y, pointer);
		return false;
	}

	@Override
	public boolean touchDragged(int x, int y, int pointer) {
		x = (int) (x / (float) Gdx.graphics.getWidth() * Constant.W);
		y = (int) (Constant.H - y / (float) Gdx.graphics.getHeight() * Constant.H);
		if (Constant.DEBUG) {
			OverlaySystem.x2 = x;
			OverlaySystem.y2 = y;
		}
		if (scene != null)
			scene.onTouchDrag(x, y, pointer);
		return false;
	}

	@Override
	public boolean touchUp(int x, int y, int pointer, int button) {
		x = (int) (x / (float) Gdx.graphics.getWidth() * Constant.W);
		y = (int) (Constant.H - y / (float) Gdx.graphics.getHeight() * Constant.H);
		if (Constant.DEBUG) {
			OverlaySystem.x1 = -1;
			OverlaySystem.y1 = -1;
			OverlaySystem.x2 = -1;
			OverlaySystem.y2 = -1;
		}
		if (scene != null)
			scene.onTouchUp(x, y, pointer);
		return false;
	}

	@Override
	public boolean tap(float x, float y, int count, int button) {
		x = x / (float) Gdx.graphics.getWidth() * Constant.W;
		y = Constant.H - y / (float) Gdx.graphics.getHeight() * Constant.H;
		if (scene != null)
			scene.onTap(x, y, count);
		return false;
	}

	@Override
	public boolean longPress(float x, float y) {
		x = x / (float) Gdx.graphics.getWidth() * Constant.W;
		y = Constant.H - y / (float) Gdx.graphics.getHeight() * Constant.H;
		if (scene != null)
			scene.onLongPress(x, y);
		return false;
	}

	@Override
	public boolean fling(float x, float y, int button) {
		x = x / (float) Gdx.graphics.getWidth() * Constant.W;
		y = Constant.H - y / (float) Gdx.graphics.getHeight() * Constant.H;
		if (scene != null)
			scene.onFling(x, y);
		return false;
	}

	@Override
	public boolean pan(float x, float y, float deltaX, float deltaY) {
		x = x / (float) Gdx.graphics.getWidth() * Constant.W;
		y = Constant.H - y / (float) Gdx.graphics.getHeight() * Constant.H;
		deltaX = deltaX / Gdx.graphics.getWidth() * Constant.W;
		deltaY = deltaY / Gdx.graphics.getHeight() * Constant.H;
		if (scene != null)
			scene.onPan(x, y, deltaX, deltaY);
		return false;
	}

	@Override
	public boolean panStop(float x, float y, int pointer, int button) {
		x = x / (float) Gdx.graphics.getWidth() * Constant.W;
		y = Constant.H - y / (float) Gdx.graphics.getHeight() * Constant.H;
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
