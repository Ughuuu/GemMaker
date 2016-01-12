package com.ngeen.systems;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.Vector;

import org.omg.CosNaming._NamingContextExtStub;
import org.w3c.dom.css.Rect;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.input.GestureDetector.GestureListener;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.ngeen.component.*;
import com.ngeen.debug.Debugger;
import com.ngeen.engine.EngineInfo;
import com.ngeen.engine.Ngeen;
import com.ngeen.entity.Entity;

public class SystemOverlay extends SystemBase implements GestureListener, InputProcessor {
	private ShapeRenderer _ShapeRenderer;
	public static float _X1;
	public static float _Y1;
	public static float _X2;
	public static float _Y2;
	private BoundingBox _Selection = new BoundingBox();
	private Matrix4 _Comb;
	private boolean _Moving = false, _DeleteSelected = false;
	private Set<Entity> _Selected = new TreeSet<Entity>(new Comparator<Entity>() {

		@Override
		public int compare(Entity o1, Entity o2) {			
			return Integer.signum(o1.hashCode()-o2.hashCode());
		}
	});
	private boolean _Selecting = false;
	private OverlaySelector _OverlaySelector;

	@SuppressWarnings("unchecked")
	public SystemOverlay(Ngeen ng, SystemConfiguration conf) {
		super(ng, conf);
		_ShapeRenderer = new ShapeRenderer();
		_OverlaySelector = new OverlaySelector(this, _ShapeRenderer, _Ng);
	}

	private void computeSelection(){
		Entity ent = _Ng.EntityBuilder.getByName("~UICAMERA");
		ComponentCamera cam = ent.getComponent(ComponentCamera.class);
		
		Entity ent2 = _Ng.EntityBuilder.getByName("~CAMERA");
		ComponentCamera cam2 = ent2.getComponent(ComponentCamera.class);
		
		Matrix4 comb = cam.Camera.combined;
		_Selection = new BoundingBox(new Vector3(_X1, EngineInfo.ScreenHeight -_Y1, -10), new Vector3 (_X2, EngineInfo.ScreenHeight -_Y2, 10));
		_Selection.mul(new Matrix4().translate(new Vector3(-cam.Camera.position.x, -cam.Camera.position.y,0)));
		_Selection.mul(new Matrix4(new Vector3(), new Quaternion(), new Vector3(EngineInfo.Width/EngineInfo.ScreenWidth, EngineInfo.Height/EngineInfo.ScreenHeight, 1)));
	}
	
	private void drawSelection() {
		Entity ent = _Ng.EntityBuilder.getByName("~UICAMERA");
		ComponentCamera cam = ent.getComponent(ComponentCamera.class);
		Matrix4 comb = cam.Camera.combined;
		_ShapeRenderer.setProjectionMatrix(comb);
		_ShapeRenderer.rect(_X1, EngineInfo.ScreenHeight - _Y1, _X2 - _X1, -_Y2 + _Y1);
	}
	
	private void MoveAll(){
		for(Entity ent : _Selected){
			Vector3 pos = ent.getComponent(ComponentPoint.class).getPosition();
			pos.x+=(_Selection.max.x - _Selection.min.x)*Math.signum(_X2 - _X1);
			pos.y+=(_Selection.max.y - _Selection.min.y)*Math.signum(_Y1 - _Y2);
			ent.getComponent(ComponentPoint.class).setPosition(pos);
		}
		_X1 = _X2;
		_Y1 = _Y2;
	}
	
	@Override
	public void onBeforeUpdate() {
		if(_DeleteSelected){
			for(Entity ent : _Selected){
				ent.remove();
			}
			_Selected.clear();
			_DeleteSelected = false;
		}
		_Comb = _Ng.EntityBuilder.getByName("~CAMERA").getComponent(ComponentCamera.class).Camera.combined;
		_ShapeRenderer.begin(ShapeType.Line);
		computeSelection();
		if(!_Moving)
		for(Entity ent : _Selected){
			_Moving = _OverlaySelector.OnEntity(_Selection.min, ent);
			if(_Moving == true){
				break;
			}
		}
		if(_Moving){
			MoveAll();
			return;
		}
		if(_Selecting)
		_Selected.clear();
		drawSelection();
	}
	
	@Override
	public void onUpdate(Entity ent) {
		if(_Moving){
			
		}else{
		if(_Selecting)
		_OverlaySelector.Select(ent, _Selected, _Selection);
		}
		_OverlaySelector.Overlay(ent, _Comb, _Selected.contains(ent));
	}

	@Override
	public void onAfterUpdate() {
		_ShapeRenderer.end();
	}

	@Override
	public boolean keyDown(int keycode) {
		if(Input.Keys.FORWARD_DEL == keycode){
			_DeleteSelected = true;
		}
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

		for(Entity ent : _Selected){
			_Moving = _OverlaySelector.OnEntity(_Selection.min, ent);
			if(_Moving == true){
				break;
			}
		}
		_Selecting=true;
		_X1 = screenX;
		_Y1 = screenY;
		_X2 = _X1;
		_Y2 = _Y1;
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		_Selecting=false;
		_X1 = 0;
		_Y1 = 0;
		_X2 = 0;
		_Y2 = 0;
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		_Selecting=true;
		_X2 = screenX;
		_Y2 = screenY;
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
