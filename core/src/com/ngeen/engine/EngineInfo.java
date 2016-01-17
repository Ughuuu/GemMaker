package com.ngeen.engine;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.SplitPane;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Tree;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.ngeen.component.*;
import com.ngeen.component.ui.layout.ComponentUIContainer;
import com.ngeen.component.ui.layout.ComponentUIHorizontalGroup;
import com.ngeen.component.ui.layout.ComponentUIScrollPane;
import com.ngeen.component.ui.layout.ComponentUISplitPane;
import com.ngeen.component.ui.layout.ComponentUIStack;
import com.ngeen.component.ui.layout.ComponentUITable;
import com.ngeen.component.ui.layout.ComponentUITree;
import com.ngeen.component.ui.layout.ComponentUIVerticalGroup;
import com.ngeen.component.ui.widget.*;
import com.ngeen.entity.Entity;

/**
 * Information data about engine.
 * 
 * @author Dragos
 *
 */
public class EngineInfo {

	public final static Map<Class<?>, Integer> ComponentIndexMap = new HashMap<Class<?>, Integer>() {
		{
			int i=0;
			put(ComponentCamera.class, i++);
			put(ComponentMaterial.class, i++);
			put(ComponentMesh.class, i++);
			put(ComponentPoint.class, i++);
			put(ComponentRigid.class, i++);
			put(ComponentScript.class, i++);
			put(ComponentSprite.class, i++);
			put(ComponentUILayout.class, i++);
			put(ComponentUIStage.class, i++);
			put(ComponentUIWidget.class, i++);
			put(ComponentVariable.class, i++);
			
			put(ComponentUIContainer.class, i++);
			put(ComponentUIHorizontalGroup.class, i++);
			put(ComponentUIScrollPane.class, i++);
			put(ComponentUISplitPane.class, i++);
			put(ComponentUIStack.class, i++);
			put(ComponentUITable.class, i++);
			put(ComponentUITree.class, i++);
			put(ComponentUIVerticalGroup.class, i++);

			put(ComponentUIButton.class, i++);
			put(ComponentUIButtonGroup.class, i++);
			put(ComponentUICheckBox.class, i++);
			put(ComponentUIDialog.class, i++);
			put(ComponentUIImage.class, i++);
			put(ComponentUIImageButton.class, i++);
			put(ComponentUILabel.class, i++);
			put(ComponentUIList.class, i++);
			put(ComponentUIProgressBar.class, i++);
			put(ComponentUISelectBox.class, i++);
			put(ComponentUISlider.class, i++);
			put(ComponentUITextArea.class, i++);
			put(ComponentUITextButton.class, i++);
			put(ComponentUITextField.class, i++);
			put(ComponentUITouchpad.class, i++);
			put(ComponentUIWindow.class, i++);
			
		}
	};

	public final static Map<Integer, Class<?>> IndexComponentMap = new HashMap<Integer, Class<?>>() {
		{
			for (Map.Entry<Class<?>, Integer> entry : ComponentIndexMap.entrySet()) {
				put(entry.getValue(), entry.getKey());
			}
		}
	};

	public final static int TotalComponents = ComponentIndexMap.size();

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

	protected static void makeBasicEntities(Ngeen ng) {
		ScreenWidth = Gdx.graphics.getWidth();
		ScreenHeight = Gdx.graphics.getHeight();

		Width = Height / ScreenHeight * ScreenWidth;

		Entity camera = ng.EntityBuilder.makeEntity("~CAMERA");
		Entity uiCamera = ng.EntityBuilder.makeEntity("~UICAMERA");

		ComponentCamera cam = camera.addComponent(ComponentCamera.class);
		cam.createCamera(Width, Height);
		ComponentPoint pos = camera.addComponent(ComponentPoint.class);

		ComponentCamera uiCam = uiCamera.addComponent(ComponentCamera.class);
		uiCam.createCamera(ScreenWidth, ScreenHeight);

		ComponentPoint uiPos = uiCamera.addComponent(ComponentPoint.class);
		uiPos.setPosition(new Vector3(ScreenWidth / 2, ScreenHeight / 2, 0));
	}

	protected static void makeOptionalEntities(Ngeen ng) {
		if(Debug){
			ng.Loader.enqueFolder("engine/");
			ng.Loader.addFolder("engine/");
			ng.Loader.finish();
		}
	}
}
