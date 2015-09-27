package com.ngeen.ui.creators;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

public class Creator {
	public static Table animation, button, camera, group, music, particle,
			physics, resource, script, sound, tag, text, texture, transform;

	public static Skin skin;
	public static TextureAtlas buttons;

	public static float w;
	public static float h;

	public void dispose() {
		skin.dispose();
		buttons.dispose();
	}
}
