package com.ngeen.manager;

import com.artemis.Entity;
import com.artemis.Manager;
import com.badlogic.gdx.Gdx;
import com.ngeen.holder.Ngeen;
import com.ngeen.ui.Interface;

public class EntityLogger extends Manager {
	private final Ngeen ng;

	public EntityLogger(Ngeen ng) {
		super();
		this.ng = ng;
	}

	@Override
	public void added(Entity e) {
		Interface.addEntity(e);
		//ng.undoRedo.doNext(e);
		//ng.undoRedo.increaseTime();
	}

	@Override
	public void changed(Entity e) {
		Interface.changeEntity(e);
		//ng.undoRedo.doNext(e);
		//ng.undoRedo.increaseTime();
	}

	@Override
	public void deleted(Entity e) {
		Interface.deleteEntity(e);
		//ng.undoRedo.delNext(e);
		//ng.undoRedo.increaseTime();
	}

	@Override
	public void disabled(Entity e) {
	}

	@Override
	public void enabled(Entity e) {
	}
}
