package com.ngeen.components;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;

/**
 * Contains a public Camera from libgdx library.
 * 
 * @author Dragos
 *
 */
public class CameraComponent extends BaseComponent {
	
	public Camera camera;
	private float fov;

	/**
	 * Creates an OrtographicCamera with give width and height.
	 * 
	 * @param width
	 * @param height
	 */
	public CameraComponent(float width, float height) {
		super();
		camera = new OrthographicCamera(width, height);
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
		this.fov = fov;
		camera = new PerspectiveCamera(fov, width, height);
	}
}
