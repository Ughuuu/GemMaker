package com.ngeen.engine;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.ngeen.component.ComponentCamera;
import com.ngeen.component.ComponentUIStage;

public class UIFactory {
	private final Ngeen _Ng;
	private Viewport _Viewport;
	protected final SpriteBatch _SpriteBatch;
	protected final InputMultiplexer _InputMultiplexer = new InputMultiplexer();

	public UIFactory(Ngeen _Ng) {
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

	protected void resize(int w, int h) {
		if(_Viewport!=null)
		_Viewport.update(w, h);
	}

	protected void createMultiplexer() {
		Gdx.input.setInputProcessor(_InputMultiplexer);
		_InputMultiplexer.addProcessor(new GestureDetector(_Ng._SystemBuilder._SceneSystem));
		if(_Ng._SystemBuilder._OverlaySystem != null)
		_InputMultiplexer.addProcessor((InputProcessor) _Ng._SystemBuilder._OverlaySystem);
	}
}
