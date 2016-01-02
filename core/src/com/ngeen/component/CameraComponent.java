package com.ngeen.component;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.ngeen.asset.AssetVariable;

/**
 * Camera component. By default it is global. You need to link it to work.
 * 
 * @author Dragos
 *
 */
public class CameraComponent extends BaseComponent {
	
	public AssetVariable<Camera> Camera;
	private float _Fov;

	/**
	 * Creates an OrtographicCamera with give width and height.
	 * 
	 * @param width
	 * @param height
	 */
	public CameraComponent(float width, float height) {
		super();
		Camera.setElement(new OrthographicCamera(width, height));
	}

	/**
	 * Creates a PerspectiveCamera with given fov and ratio. Ratio is computed
	 * from width and height.
	 * 
	 * @param fov
	 * @param width
	 * @param height
	 */
	public CameraComponent(float fov, float width, float height) {
		super();
		this._Fov = fov;
		Camera.setElement(new PerspectiveCamera(fov, width, height));
	}
}
