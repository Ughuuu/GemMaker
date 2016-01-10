package com.ngeen.engine;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.ngeen.component.ComponentCamera;
import com.ngeen.entity.Entity;

/**
 * Information data about engine.
 * 
 * @author Dragos
 *
 */
public class EngineInfo {
	public final static int EntitiesCache = 1;
	
	public final static int ComponentCache = 100;

	public static Color BackgroundColor = new Color(.6f, .2f, .1f, 1);
	/**
	 * Screen Width.
	 */
	public static float Width = 1024;

	/**
	 * Screen Height.
	 */
	public static float Height = 600;

	public static float ScreenWidth;
	
	public static float ScreenHeight;
	
	/**
	 * Mathematical constant. Very small float number.
	 */
	public static final float Epsilon = 1e-10f;

	/**
	 * Value goes from 0 to 1, where 0 is mute and 1 is normal.
	 */
	public static float Volume = 1;

	/**
	 * If set to true, program will output a lot of stuff and have debug renders
	 * set up.
	 */
	public static final boolean Debug = true;

	/**
	 * Gravity. To be used in Box2D.
	 */
	public static Vector2 Gravity = new Vector2(0, -1f);

	/**
	 * Game Speed.
	 */
	public static float GameSpeed = 1;

	/**
	 * Frames per second.
	 */
	public static final float Fps = 60;

	/**
	 * Milliseconds passed for each iteration.
	 */
	public static final float Ms = 1.f / Fps;

	/**
	 * PixelPerMeter. Used for Box2D.
	 */
	public static final float PixelPerMeter = 100;

	/**
	 * MeterPerPixel. Used for Box1d.
	 */
	public static final float MeterPerPixel = 1.f / PixelPerMeter;
	
	protected static void makeBasicEntities(Ngeen ng){
		ScreenWidth = Gdx.graphics.getWidth();
		ScreenHeight = Gdx.graphics.getHeight();
		
		Entity camera = ng.EntityBuilder.makeEntity("~CAMERA");
		Entity uiCamera = ng.EntityBuilder.makeEntity("~UICAMERA");
		
		ComponentCamera cam = camera.addComponent(ComponentCamera.class);
		cam.createCamera(Width, Height);
		
		ComponentCamera uiCam = uiCamera.addComponent(ComponentCamera.class);
		uiCam.createCamera(ScreenWidth, ScreenHeight);
		uiCam.Camera.translate(ScreenWidth/2, ScreenHeight/2, 0);
		uiCam.Camera.update();
	}
}
