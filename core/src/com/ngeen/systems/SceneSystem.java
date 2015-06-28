package com.ngeen.systems;

import com.artemis.annotations.Wire;
import com.artemis.systems.VoidEntitySystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.input.GestureDetector.GestureListener;
import com.badlogic.gdx.math.Vector2;
import com.ngeen.scene.LoadScene;
import com.ngeen.scene.Scene;

@Wire
public class SceneSystem extends VoidEntitySystem implements GestureListener,
		InputProcessor {
	private Scene scene;

	public SceneSystem() {
		this.scene = new LoadScene();
	}

	public Scene getScene() {
		return scene;
	}

	public void setScene(Scene scene) {
		this.scene = scene;
	}

	@Override
	protected void processSystem() {
		scene.onUpdate(Gdx.graphics.getDeltaTime());
	}

	@Override
	public boolean touchDown(float x, float y, int pointer, int button) {
		scene.onTouchDown(x, y, pointer);
		return false;
	}

	@Override
	public boolean tap(float x, float y, int count, int button) {
		scene.onTap(x, y, count);
		return false;
	}

	@Override
	public boolean longPress(float x, float y) {
		scene.onLongPress(x, y);
		return false;
	}

	@Override
	public boolean fling(float velocityX, float velocityY, int button) {
		scene.onFling(velocityX, velocityY);
		return false;
	}

	@Override
	public boolean pan(float x, float y, float deltaX, float deltaY) {
		scene.onPan(x, y, deltaX, deltaY);
		return false;
	}

	@Override
	public boolean panStop(float x, float y, int pointer, int button) {
		scene.onPanStop(x, y);
		return false;
	}

	@Override
	public boolean zoom(float initialDistance, float distance) {
		scene.onZoom(initialDistance, distance);
		return false;
	}

	@Override
	public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2,
			Vector2 pointer1, Vector2 pointer2) {
		scene.onPinch(initialPointer1, initialPointer2, pointer1, pointer2);
		return false;
	}

	@Override
	public boolean keyDown(int keycode) {
		scene.onKeyDown(keycode);
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		scene.onKeyUp(keycode);
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		// UNUSED
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		// UNUSED
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		scene.onTouchUp(screenX, screenY, pointer);
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		scene.onTouchDrag(screenX, screenY, pointer);
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		// UNUSED
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		// UNUSED
		return false;
	}

}
