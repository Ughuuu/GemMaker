package com.ngeen.holder;

import java.util.List;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

public class Constant {
	public static int DEBUG_LEVEL = 0;
	public static int W = 1024;
	public static int H = 576;
	
	public static boolean GAME_STATE = false;
	public static boolean VOLUME = true;
	public static boolean DEBUG = true;
	public static boolean RELEASE = false;

	public static Vector2 GRAVITY = new Vector2(0, -1f);
	public static Camera CAMERA;
	public static SpriteBatch BATCH;
	public static BitmapFont DEBUG_FONT;
	public static AssetManager MANAGER;
	
	public static float GAME_SPEED = 1;
	public static float ZOOM = 1;
	public static float FPS = 60;
	public static float MS = 1.f / FPS;
	public static float PIXEL_TO_METER = 100;
	public static float INV_PIXEL_TO_METER = 1.f/PIXEL_TO_METER;
	
	public static Color BACKGROUND_COLOR = new Color(.6f, .2f, .1f, 1);

}

