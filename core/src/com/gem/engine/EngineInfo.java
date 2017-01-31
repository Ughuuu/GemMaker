package com.gem.engine;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.gem.component.*;
import com.gem.component.ui.*;
import com.gem.entity.Entity;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Dragos
 * @hidden Information data about engine.
 */
public class EngineInfo {

    public static final int CommandsSize = 1000000;
    public final static int ComponentCache = 1000;
    public final static Map<Class<?>, Integer> ComponentIndexMap = new HashMap<Class<?>, Integer>() {
        {
            int i = 0;
            put(ComponentCamera.class, i++);
            put(ComponentMaterial.class, i++);
            put(ComponentMesh.class, i++);
            put(ComponentPoint.class, i++);
            put(ComponentRigid.class, i++);
            put(ComponentScript.class, i++);
            put(ComponentSprite.class, i++);
            put(ComponentVariable.class, i++);

            put(ComponentUIStage.class, i++);
            put(ComponentUILayout.class, i++);
            put(ComponentUIWidget.class, i++);
            put(ComponentUIContainer.class, i++);
            put(ComponentUIHorizontalGroup.class, i++);
            put(ComponentUIScrollPane.class, i++);
            put(ComponentUISplitPane.class, i++);
            put(ComponentUIStack.class, i++);
            put(ComponentUITable.class, i++);
            put(ComponentUITree.class, i++);
            put(ComponentUIVerticalGroup.class, i++);

            put(ComponentUIBase.class, i++);
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
    /**
     * If set to true, program will output a lot of stuff and have debug renders
     * set up.
     */
    public static final boolean Debug = true;
    public final static int EntitiesCache = 500;
    /**
     * Mathematical constant. Very small float number.
     */
    public static final float Epsilon = 1e-10f;
    /**
     * Frames per second.
     */
    public static final float Fps = 120;
    public final static Map<Integer, Class<?>> IndexComponentMap = new HashMap<Integer, Class<?>>() {
        {
            for (Map.Entry<Class<?>, Integer> entry : ComponentIndexMap.entrySet()) {
                put(entry.getValue(), entry.getKey());
            }
        }
    };
    /**
     * MeterPerPixel. Used for Box2d. 1/PixelPerMeter.
     */
    public static final float MeterPerPixel = 1.f / 100;
    /**
     * Milliseconds passed for each iteration.
     */
    public static final float Ms = 1.f / Fps;
    /**
     * PixelPerMeter. Used for Box2D.
     */
    public static final float PixelPerMeter = 100;
    public final static int TotalComponents = ComponentIndexMap.size();
    public static boolean Android = false;
    public static boolean Applet = false;
    public static Color BackgroundColor = new Color(.6f, .2f, .1f, 1);
    /**
     * Game Speed.
     */
    public static float GameSpeed = 1;
    /**
     * Gravity. To be used in Box2D.
     */
    public static Vector2 Gravity = new Vector2(0, -1f);
    /**
     * Value goes from 0 to 1, where 0 is mute and 1 is normal.
     */
    public static float Volume = 1;

    protected static void makeOptionalEntities(Gem ng) {
        if (Debug) {
            ng.loader.enqueFolder("engine/");
            ng.loader.addFolder("engine/");
            ng.loader.finish();
        }
    }
}
