package com.ngeen.component;

import java.io.IOException;
import java.io.StringWriter;

import javax.xml.parsers.DocumentBuilderFactory;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlWriter;
import com.ngeen.engine.EngineInfo;
import com.ngeen.engine.Ngeen;
import com.ngeen.entity.Entity;

/**
 * Camera component. By default it is global. You need to link it to work.
 * 
 * @author Dragos
 *
 */
public class ComponentCamera extends ComponentBase {

	public Camera Camera;
	private float _Fov = -1;

	public ComponentCamera(Ngeen ng) {
		super(ng);
		createCamera(EngineInfo.Width,EngineInfo.Height);
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
	
	@Override
	protected void Save(XmlWriter element) throws Exception{
		element.element("Component")
				.attribute("_Type", "ComponentCamera")
				.attribute("Fov", _Fov)
		       .attribute("ViewportWidth", Camera.viewportWidth)
		       .attribute("ViewportHeight", Camera.viewportHeight)
		       .pop();
	}

	@Override
	protected void Load(XmlReader.Element element) throws Exception{
		_Fov = element.getFloat("Fov");
		float Width = element.getFloat("ViewportWidth");
		float Height = element.getFloat("ViewportHeight");
		if(_Fov < 0){
			createCamera(Width,Height);
		}else{
			createCamera(Width,Height, _Fov);			
		}
	}
}
