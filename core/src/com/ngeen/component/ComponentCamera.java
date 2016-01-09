package com.ngeen.component;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.ngeen.engine.Constant;
import com.ngeen.engine.Ngeen;

/**
 * Camera component. By default it is global. You need to link it to work.
 * 
 * @author Dragos
 *
 */
public class ComponentCamera extends ComponentBase {

	public Camera Camera;
	private float _Fov;

	public ComponentCamera(Ngeen ng) {
		super(ng);
		createCamera(Constant.W,Constant.H);
	}

	/**
	 * Creates an OrtographicCamera with give width and height.
	 * 
	 * @param width
	 * @param height
	 */
	public void createCamera(float width, float height) {
		Camera = new OrthographicCamera(width, height);
	}

	/**
	 * Creates a PerspectiveCamera with given fov and ratio. Ratio is computed
	 * from width and height.
	 * 
	 * @param fov
	 * @param width
	 * @param height
	 */
	public void createCamera(float fov, float width, float height) {
		this._Fov = fov;
		Camera = new PerspectiveCamera(fov, width, height);
	}
}
