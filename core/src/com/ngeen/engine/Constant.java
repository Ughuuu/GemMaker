package com.ngeen.engine;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;

public class Constant {
	public final static int ENTITIES_CACHE = 500;
	public final static int COMPONENT_CACHE = 100;
	public final static int DEBUG_LEVEL = 1;
	public static final float EPSILON = 1e-10f;
	public static final boolean DEBUG = true;

	public static Vector2 GRAVITY = new Vector2(0, -1f);

	public static float W = 1024;
	public static float H = 576;
	public static float GAME_SPEED = 1;
	public static float FPS = 60;
	public static float MS = 1.f / FPS;
	public static float PIXEL_TO_METER = 100;
	public static float INV_PIXEL_TO_METER = 1.f / PIXEL_TO_METER;

	public static Color BACKGROUND_COLOR = new Color(.6f, .2f, .1f, 1);

}
