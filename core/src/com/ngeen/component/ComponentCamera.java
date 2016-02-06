package com.ngeen.component;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.PerspectiveCamera;
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

	private float _Fov = -1;
	public Camera Camera;

	public ComponentCamera(Ngeen ng, Entity ent) {
		super(ng, ent);
		createCamera(EngineInfo.Width, EngineInfo.Height);
	}

	/**
	 * Creates an OrtographicCamera with give width and height.
	 * 
	 * @param width
	 * @param height
	 */
	public ComponentCamera createCamera(float width, float height) {
		Camera = new OrthographicCamera(width, height);
		return this;
	}

	/**
	 * Creates a PerspectiveCamera with given fov and ratio. Ratio is computed
	 * from width and height.
	 * 
	 * @param fov
	 * @param width
	 * @param height
	 */
	public ComponentCamera createCamera(float fov, float width, float height) {
		this._Fov = fov;
		Camera = new PerspectiveCamera(fov, width, height);
		return this;
	}

	@Override
	protected void Load(XmlReader.Element element) throws Exception {
		_Fov = element.getChildByName("Fov").getFloat("Float");
		float Width = element.getChildByName("ViewportWidth").getFloat("Float");
		float Height = element.getChildByName("ViewportHeight").getFloat("Float");
		if (_Fov < 0) {
			createCamera(Width, Height);
		} else {
			createCamera(Width, Height, _Fov);
		}
	}

	@Override
	protected void Save(XmlWriter element) throws Exception {
		element.element("Component").attribute("_Type", _Type.getName()).element("Fov").attribute("Float", _Fov).pop()
				.element("ViewportWidth").attribute("Float", Camera.viewportWidth).pop().element("ViewportHeight")
				.attribute("Float", Camera.viewportHeight).pop().pop();
	}
}
