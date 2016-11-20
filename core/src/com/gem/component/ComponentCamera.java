package com.gem.component;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlWriter;
import com.gem.engine.EngineInfo;
import com.gem.engine.Gem;
import com.gem.entity.ComponentSpokesman;
import com.gem.entity.Entity;

/**
 * Camera component. By default it is global. You need to link it to work.
 *
 * @author Dragos
 */
public class ComponentCamera extends ComponentBase {

	private final ComponentSpokesman _ComponentSpokesman;
	public Camera Camera;
	private float _Fov = -1;

	public ComponentCamera(Gem ng, Entity ent, ComponentFactory factory, ComponentSpokesman _ComponentSpokesman) {
		super(ng, ent, factory, _ComponentSpokesman);
		this._ComponentSpokesman = _ComponentSpokesman;
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
	public void notifyWithComponent(ComponentPoint point) {
		Camera.position.set(point.getPosition());
		Camera.update();
	}

	@Override
	protected ComponentBase Load(XmlReader.Element element) throws Exception {
		_Fov = element.getChildByName("Viewport").getFloat("Fov");
		float Width = element.getChildByName("Viewport").getFloat("Width");
		float Height = element.getChildByName("Viewport").getFloat("Height");
		if (_Fov < 0) {
			createCamera(Width, Height);
		} else {
			createCamera(Width, Height, _Fov);
		}
		return this;
	}

	@Override
	protected void Save(XmlWriter element) throws Exception {
		element.element("Component").attribute("Type", Type.getName()).element("Viewport").attribute("Fov", _Fov)
				.attribute("Width", Camera.viewportWidth).attribute("Height", Camera.viewportHeight).pop().pop();
	}

	@Override
	protected void visitComponent(ComponentBase component, ComponentFactory factory) {
		component.notifyWithComponent(this);
	}
}
