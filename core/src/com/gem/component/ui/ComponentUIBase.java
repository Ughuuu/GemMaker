package com.gem.component.ui;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Value;
import com.badlogic.gdx.utils.Align;
import com.gem.component.ComponentBase;
import com.gem.component.ComponentFactory;
import com.gem.component.ComponentPoint;
import com.gem.engine.Gem;
import com.gem.entity.ComponentSpokesman;
import com.gem.entity.Entity;

import lombok.val;

public abstract class ComponentUIBase extends ComponentBase {

	protected int _Depth = 0, _Align;
	protected Entity uiParent;
	protected boolean click = false;
	Value width;

	public ComponentUIBase(Gem ng, Entity ent, ComponentFactory factory, ComponentSpokesman _ComponentSpokesman) {
		super(ng, ent, factory, _ComponentSpokesman);
	}

	public Entity getUIParent() {
		return uiParent;
	}

	@Override
	public void notifyWithComponent(ComponentPoint point) {
		Actor actor = getActor();
		actor.setOrigin(Align.center);
		actor.setPosition(point.getPosition().x, point.getPosition().y);
		actor.setZIndex(_Depth);
		if (point.getScale().isZero() && point.getRotation().isZero()) {
		} else {
			actor.setScale(point.getScale().x, point.getScale().y);
			actor.setRotation(point.getRotation().z);
		}
	}

	protected void add(ComponentUIBase comp) {
	}

	protected void del(ComponentUIBase comp) {
	}

	protected abstract Actor getActor();

	@Override
	protected void reinit() {
		// _ComponentFactory.addSuperComponent(this);
	}

	protected void swap(ComponentUIBase a, ComponentUIBase b) {
	}
}
