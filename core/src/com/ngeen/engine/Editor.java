package com.ngeen.engine;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.input.GestureDetector.GestureListener;
import com.badlogic.gdx.math.Vector2;

public class Editor implements GestureListener, InputProcessor {
	private final Ngeen _Ng;

	public Editor(Ngeen ng) {
		_Ng = ng;
	}

	boolean control = false, shift = false, alt = false;

	@Override
	public boolean keyDown(int keycode) {
		switch (keycode) {
		case Keys.CONTROL_LEFT:
			control = true;
			break;
		case Keys.CONTROL_RIGHT:
			control = true;
			break;
		case Keys.SHIFT_LEFT:
			shift = true;
			break;
		case Keys.SHIFT_RIGHT:
			shift = true;
			break;
		case Keys.ALT_LEFT:
			alt = true;
			break;
		case Keys.ALT_RIGHT:
			alt = true;
			break;
		case Keys.Z:
			if (control) {// ctrl + z(undo)
			}
			break;
		case Keys.Y:
			if (control) {// ctrl + y(redo)
			}
			break;
		}
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		switch (keycode) {
		case Keys.CONTROL_LEFT:
			control = false;
			break;
		case Keys.CONTROL_RIGHT:
			control = false;
			break;
		case Keys.SHIFT_LEFT:
			shift = false;
			break;
		case Keys.SHIFT_RIGHT:
			shift = false;
			break;
		case Keys.ALT_LEFT:
			alt = false;
			break;
		case Keys.ALT_RIGHT:
			alt = false;
			break;
		}
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDown(float x, float y, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean tap(float x, float y, int count, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean longPress(float x, float y) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean fling(float velocityX, float velocityY, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean pan(float x, float y, float deltaX, float deltaY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean panStop(float x, float y, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean zoom(float initialDistance, float distance) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2) {
		// TODO Auto-generated method stub
		return false;
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
