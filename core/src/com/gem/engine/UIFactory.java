package com.gem.engine;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.gem.component.ComponentCamera;
import com.gem.component.ui.ComponentUIStage;

/**
 * @author Dragos
 * @hidden
 */
public class UIFactory {
	protected final InputMultiplexer _InputMultiplexer = new InputMultiplexer();
	protected final SpriteBatch _SpriteBatch;
	private final Gem gem;
	private Viewport viewport;

	public UIFactory(Gem _Ng) {
		this.gem = _Ng;
		_SpriteBatch = new SpriteBatch();
	}

	public void createStage(ComponentUIStage stage) {
		viewport = new ScreenViewport();
		gem.getEntity("~UICAMERA").getComponent(ComponentCamera.class).setToCamera(viewport);
		stage.setStage(viewport, _SpriteBatch, _InputMultiplexer);
	}

	protected void createMultiplexer() {
		Gdx.input.setInputProcessor(_InputMultiplexer);
		_InputMultiplexer.addProcessor(new GestureDetector(gem.systemBuilder.sceneSystem));
		if (gem.systemBuilder.overlaySystem != null)
			_InputMultiplexer.addProcessor(gem.systemBuilder.overlaySystem);
	}

	protected void resize(int w, int h) {
		if (viewport != null) {
			viewport.update(w, h);
			gem.getEntity("~UICAMERA").getComponent(ComponentCamera.class).setToCamera(viewport);
		}
	}
}
