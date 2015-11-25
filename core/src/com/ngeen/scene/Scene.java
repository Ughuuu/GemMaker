package com.ngeen.scene;

import com.artemis.World;
import com.badlogic.gdx.math.Vector2;
import com.ngeen.debug.Debugger;
import com.ngeen.engine.Constant;
import com.ngeen.engine.Ngeen;

public class Scene {
	public static Ngeen ng;

	/**
	 * Called before onEnter so you can assist the loading part of objects.
	 */
	public void onInit() {
		if (Constant.DEBUG_LEVEL == 0)
			Debugger.println("onInit()");
	}

	public void onLeave() {
		if (Constant.DEBUG_LEVEL == 0)
			Debugger.println("onLeave()");
	}

	public void onEnter() {
		if (Constant.DEBUG_LEVEL == 0)
			Debugger.println("onEnter()");
	}

	public void onUpdate(float delta) {
		if (Constant.DEBUG_LEVEL == 0)
			Debugger.println("onUpdate(): [delta=" + delta + "]");
	}

	public void onKeyUp(int keycode) {
		if (Constant.DEBUG_LEVEL == 0)
			Debugger.println("onKeyUp(): [keycode=" + keycode + "]");
	}

	public void onKeyDown(int keycode) {
		if (Constant.DEBUG_LEVEL == 0)
			Debugger.println("onKeyDown(): [keycode=" + keycode + "]");
	}

	public void onTouchDrag(float x, float y, int index) {
		if (Constant.DEBUG_LEVEL == 0)
			Debugger.println("onTouchDrag(): [x=" + x + ",y=" + y + ",index="
					+ index + "]");
	}

	public void onTouchDown(float x, float y, int index) {
		if (Constant.DEBUG_LEVEL == 0)
			Debugger.println("onTouchDown(): [x=" + x + ",y=" + y + ",index="
					+ index + "]");
	}

	public void onTouchUp(float x, float y, int index) {
		if (Constant.DEBUG_LEVEL == 0)
			Debugger.println("onTouchUp(): [x=" + x + ",y=" + y + ",index="
					+ index + "]");
	}

	public void onTap(float x, float y, int count) {
		if (Constant.DEBUG_LEVEL == 0)
			Debugger.println("onTap(): [x=" + x + ",y=" + y + ",count="
					+ count + "]");
	}

	public void onLongPress(float x, float y) {
		if (Constant.DEBUG_LEVEL == 0)
			Debugger.println("onLongPress(): [x=" + x + ",y=" + y + "]");
	}

	public void onFling(float x, float y) {
		if (Constant.DEBUG_LEVEL == 0)
			Debugger.println("onFling(): [x=" + x + ",y=" + y + "]");
	}

	public void onPan(float x, float y, float deltax, float deltay) {
		if (Constant.DEBUG_LEVEL == 0)
			Debugger.println("onPan(): [x=" + x + ",y=" + y + ",deltax="
					+ deltax + ",deltay=" + deltay + "]");
	}

	public void onPanStop(float x, float y) {
		if (Constant.DEBUG_LEVEL == 0)
			Debugger.println("onPanStop(): [x=" + x + ",y=" + y + "]");
	}

	public void onZoom(float start, float distance) {
		if (Constant.DEBUG_LEVEL == 0)
			Debugger.println("onZoom(): [start=" + start + ",distance="
					+ distance + "]");
	}

	public void onPinch(Vector2 start1, Vector2 start2, Vector2 final1,
			Vector2 final2) {
		if (Constant.DEBUG_LEVEL == 0)
			Debugger.println("onZoom(): [start1=" + start1 + ",start2="
					+ start2 + ",final1=" + final1 + ",final2=" + final2 + "]");
	}
}
