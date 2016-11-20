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
	private final Gem _Ng;
	private Viewport _Viewport;

	public UIFactory(Gem _Ng) {
		this._Ng = _Ng;
		_SpriteBatch = new SpriteBatch();
	}

	public void createStage(ComponentUIStage stage) {
		Camera cam = _Ng.getEntity("~UICAMERA").getComponent(ComponentCamera.class).Camera;
		if (_Viewport == null) {
			_Viewport = new ScreenViewport(cam);
		}
		stage.setStage(_Viewport, _SpriteBatch, _InputMultiplexer);
	}

	protected void createMultiplexer() {
		Gdx.input.setInputProcessor(_InputMultiplexer);
		_InputMultiplexer.addProcessor(new GestureDetector(_Ng._SystemBuilder._SceneSystem));
		if (_Ng._SystemBuilder._OverlaySystem != null)
			_InputMultiplexer.addProcessor(_Ng._SystemBuilder._OverlaySystem);
	}

	protected void resize(int w, int h) {
		if (_Viewport != null) {
			_Viewport.update(w, h);
			Camera cam = _Ng.getEntity("~UICAMERA").getComponent(ComponentCamera.class).Camera;
			_Viewport.setCamera(cam);
		}
	}
}
