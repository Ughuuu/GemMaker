package com.gem.component;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlWriter;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
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

	private Camera camera;
	private float fov = -1;
	private float width = 0;
	private float height = 0;
	private Viewport viewport;
	private static enum CameraType{
		Stretch, Fit, Fill, Screen, Extend
	}
	private CameraType cameraType = CameraType.Screen;

	public ComponentCamera(Gem ng, Entity ent, ComponentFactory factory, ComponentSpokesman ComponentSpokesman) {
		super(ng, ent, factory, ComponentSpokesman);
	}
	
	public Viewport getViewport(){
		return viewport;
	}
	
	private void makeViewport(){
		switch(cameraType){
		case Stretch:
			viewport = new StretchViewport(width, height);
			break;
		case Fit:
			viewport = new FillViewport(width, height);
			break;
		case Fill:
			viewport = new FillViewport(width, height);
			break;
		case Screen:
			viewport = new ScreenViewport();
			break;
		case Extend:
			viewport = new ExtendViewport(width, height);
			break;
		}
	}
	
	public void setCameraType(CameraType type){
		cameraType = type;
		makeViewport();
	}
	
	public CameraType getCameraType(CameraType type){
		return cameraType;
	}
	
	public Vector3 getPosition(){
		return camera.position;
	}
	
	public ComponentCamera setPosition(Vector3 pos){
		camera.position.set(pos);
		camera.update();
		return this;
	}
	
	public Matrix4 getView(){
		return camera.view;
	}
	
	public Matrix4 getCombined(){
		return camera.combined;
	}
	
	public ComponentCamera setViewportWidth(float width){
		this.width = width;
		camera.viewportWidth = width;
		camera.update();
		return this;
	}
	
	public ComponentCamera setViewportHeight(float height){
		this.height = height;
		camera.viewportHeight = height;
		camera.update();
		return this;
	}
	
	public float getViewportWidth(){
		return camera.viewportWidth;
	}
	
	public float getViewportHeight(){
		return camera.viewportHeight;
	}
	
	public void setToCamera(Viewport view){
		view.setCamera(camera);
	}

	/**
	 * Creates an OrtographicCamera with give width and height.
	 *
	 * @param width
	 * @param height
	 */
	public ComponentCamera createOrthographicCamera(float width, float height) {
		camera = new OrthographicCamera(width, height);
		this.width = width;
		this.height = height;
		makeViewport();
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
	public ComponentCamera createPerspectiveCamera(float fov, float width, float height) {
		this.fov = fov;
		camera = new PerspectiveCamera(fov, width, height);
		this.width = width;
		this.height = height;
		makeViewport();
		return this;
	}

	@Override
	public void notifyWithComponent(ComponentPoint point) {
		setPosition(point.getPosition());
		camera.up.set(point.getUp());
		camera.viewportWidth = width * (point.getScale().x * point.getScale().z);
		camera.viewportHeight = height * (point.getScale().y * point.getScale().z);
		camera.update();
	}

	@Override
	protected ComponentBase Load(XmlReader.Element element) throws Exception {
		float Width = element.getChildByName("Viewport").getFloat("Width");
		float Height = element.getChildByName("Viewport").getFloat("Height");
		try{
			cameraType = CameraType.values()[element.getChildByName("Viewport").getInt("CameraType")];
		}catch(Exception e){
		}
		try{
			fov = element.getChildByName("Viewport").getFloat("Fov");
			if (fov < 0) {
				createOrthographicCamera(Width, Height);
			} else {
				createPerspectiveCamera(Width, Height, fov);
			}
		}catch(Exception e){
			createOrthographicCamera(Width, Height);			
		}
		return this;
	}

	@Override
	protected void Save(XmlWriter element) throws Exception {
		element.element("Component").attribute("Type", Type.getName()).element("Viewport");
		if(fov>0){
			element.attribute("Fov", fov);
		}
		element.attribute("Width", width).attribute("Height", height).attribute("Scaling", cameraType).pop().pop();
	}

	@Override
	protected void visitComponent(ComponentBase component, ComponentFactory factory) {
		component.notifyWithComponent(this);
	}
}
