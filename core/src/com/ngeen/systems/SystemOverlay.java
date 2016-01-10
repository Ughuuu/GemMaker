package com.ngeen.systems;

import org.omg.CosNaming._NamingContextExtStub;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.input.GestureDetector.GestureListener;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.ngeen.component.ComponentCamera;
import com.ngeen.component.ComponentDrawble;
import com.ngeen.engine.EngineInfo;
import com.ngeen.engine.Ngeen;
import com.ngeen.entity.Entity;

public class SystemOverlay extends SystemBase implements GestureListener, InputProcessor{
	private ShapeRenderer shapeRenderer;
	public static float x1, y1, x2, y2;

	@SuppressWarnings("unchecked")
	public SystemOverlay(Ngeen ng, SystemConfiguration conf) {
		super(ng, conf);
		shapeRenderer = new ShapeRenderer();
	}

	private void drawSelection() {
		Entity ent = _Ng.EntityBuilder.getByName("~UICAMERA");
		ComponentCamera cam = ent.getComponent(ComponentCamera.class);
		Matrix4 comb = cam.Camera.combined;
		shapeRenderer.setProjectionMatrix(comb);
		shapeRenderer.rect(x1, EngineInfo.ScreenHeight - y1, x2 - x1, - y2 + y1);
	}

	@Override
	public void onBeforeUpdate() {
		shapeRenderer.begin(ShapeType.Line);
		drawSelection();
	}

	@Override
	public void onUpdate(Entity ent) {
		/*
		 * TransformComponent transform = transformMapper.get(e);
		 * MaterialComponent texture = textureMapper.get(e); float w = 1, h = 1;
		 * if (texture != null) { w = texture.tex.getWidth(); h =
		 * texture.tex.getHeight(); }
		 * 
		 * Matrix4 comb =
		 * Constant.CAMERA.getComponent(CameraComponent.class).camera.combined;
		 * Matrix4 pos = ComputeHelper.getCombined(Constant.CAMERA.getComponent(
		 * TransformComponent.class)); Matrix4 pos2 =
		 * ComputeHelper.getCombined(transform);
		 * shapeRenderer.setProjectionMatrix(pos2.mul(pos).mul(comb));
		 * 
		 * shapeRenderer.rect(-w / 2, -h / 2, w, h);
		 */
	}

	@Override
	public void onAfterUpdate() {
		shapeRenderer.end();
	}

	@Override
	public boolean keyDown(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		x1 = screenX;
		y1 = screenY;
		x2 = x1;
		y2 = y1;
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		x1 = 0;
		y1 = 0;
		x2 = 0;
		y2 = 0;
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		x2 = screenX;
		y2 = screenY;
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
}
