package com.ngeen.scene;

import com.badlogic.gdx.math.Vector2;
import com.ngeen.debug.Debugger;
import com.ngeen.engine.Ngeen;
import com.ngeen.entity.Entity;

/**
 * Extend this class to have Scene functionality. A scene is called every frame.
 * 
 * @author Dragos
 *
 */
public class Scene {
	protected Ngeen ng;
	private SceneFactory _SceneFactory;
	
	protected Entity CreateObject(String name){
		return ng.EntityBuilder.makeEntity(name);
	}

	public Scene() {
	}
	
	protected void addNgeen(final Ngeen ng){
		this.ng = ng;
	}
	
	protected void addSceneFactory(final SceneFactory _SceneFactory){
		this._SceneFactory = _SceneFactory;
	}

	/**
	 * Called before onEnter so you can assist the loading part of objects.
	 */
	public void onInit() {
		Debugger.println("onInit()");
	}

	public void onExit() {
		Debugger.println("onExit()");
	}

	public void onUpdate(float delta) {
		Debugger.println("onUpdate(): [delta=" + delta + "]");
	}

	public void onKeyUp(int keycode) {
		Debugger.println("onKeyUp(): [keycode=" + keycode + "]");
	}

	public void onKeyDown(int keycode) {
		Debugger.println("onKeyDown(): [keycode=" + keycode + "]");
	}

	public void onTouchDrag(float x, float y, int index) {
		Debugger.println("onTouchDrag(): [x=" + x + ",y=" + y + ",index=" + index + "]");
	}

	public void onTouchDown(float x, float y, int index) {
		Debugger.println("onTouchDown(): [x=" + x + ",y=" + y + ",index=" + index + "]");
	}

	public void onTouchUp(float x, float y, int index) {
		Debugger.println("onTouchUp(): [x=" + x + ",y=" + y + ",index=" + index + "]");
	}

	public void onTap(float x, float y, int count) {
		Debugger.println("onTap(): [x=" + x + ",y=" + y + ",count=" + count + "]");
	}

	public void onLongPress(float x, float y) {
		Debugger.println("onLongPress(): [x=" + x + ",y=" + y + "]");
	}

	public void onFling(float x, float y) {
		Debugger.println("onFling(): [x=" + x + ",y=" + y + "]");
	}

	public void onPan(float x, float y, float deltax, float deltay) {
		Debugger.println("onPan(): [x=" + x + ",y=" + y + ",deltax=" + deltax + ",deltay=" + deltay + "]");
	}

	public void onPanStop(float x, float y) {
		Debugger.println("onPanStop(): [x=" + x + ",y=" + y + "]");
	}

	public void onZoom(float start, float distance) {
		Debugger.println("onZoom(): [start=" + start + ",distance=" + distance + "]");
	}

	public void onPinch(Vector2 start1, Vector2 start2, Vector2 final1, Vector2 final2) {
		Debugger.println(
				"onZoom(): [start1=" + start1 + ",start2=" + start2 + ",final1=" + final1 + ",final2=" + final2 + "]");
	}

	public void changeScene(String newScene) {
		_SceneFactory.changeScene(newScene);
	}
}
