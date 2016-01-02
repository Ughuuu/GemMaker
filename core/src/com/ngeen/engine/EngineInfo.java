package com.ngeen.engine;

import java.util.List;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

/**
 * Information data about engine.
 * @author Dragos
 *
 */
public class EngineInfo {
	/**
	 * Screen Width.
	 */
	public static float Width;
	
	/**
	 * Screen Height.
	 */
	public static float Height;

	/**
	 * Mathematical constant. Very small float number.
	 */
	public static final float Epsilon = 1e-10f;
	
	/**
	 * Value goes from 0 to 1, where 0 is mute and 1 is normal.
	 */
	public static float Volume = 1;
	
	/**
	 * If set to true, program will output a lot of stuff
	 * and have debug renders set up.
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
}
