package com.gem.component.ui;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup;
import com.badlogic.gdx.utils.XmlReader.Element;
import com.badlogic.gdx.utils.XmlWriter;
import com.gem.component.ComponentBase;
import com.gem.component.ComponentFactory;
import com.gem.component.ComponentPoint;
import com.gem.engine.Gem;
import com.gem.entity.ComponentSpokesman;
import com.gem.entity.Entity;

public class ComponentUITable extends ComponentUILayout {
	private static final Vector3 ONE = new Vector3(1, 1, 1);
	private String _BackgroundAsset = "";
	private boolean _Saved = false, _Packed = true;
	private Table _Table;

	public ComponentUITable(Gem ng, Entity ent, ComponentFactory factory, ComponentSpokesman _ComponentSpokesman) {
		super(ng, ent, factory, _ComponentSpokesman);
		_Table = new Table();
		regenerate();
	}

	public String getBackgroundAsset() {
		return _BackgroundAsset;
	}

	public boolean isPacked() {
		return _Packed;
	}

	@Override
	public void notifyWithComponent(ComponentPoint point) {
		super.notifyWithComponent(point);
		if (point.getScale().equals(ONE) && point.getRotation().isZero()) {
			_Table.setTransform(false);
		} else {
			_Table.setTransform(true);
		}
	}

	@Override
	public ComponentUITable remove() {
		getOwner().removeComponent(ComponentUILayout.class);
		Owner.removeComponent(this.getClass(), Id);
		regenerate();
		return this;
	}

	public void setBackgroundAsset(String _BackgroundAsset) {
		// _Ng.Loader.getAsset<Texture>(_BackgroundAsset);
		// _Table.setBackground();
		// this._BackgroundAsset = _BackgroundAsset;
	}

	public void setPacked(boolean _Packed) {
		this._Packed = _Packed;
	}

	private void regenerate() {
		_Table.setWidth(_Width);
		_Table.setHeight(_Height);
		// _Table.setBackground();
		if (_Packed)
			_Table.pack();
	}

	@Override
	protected void add(ComponentUIBase comp) {
		_Table.add(comp.getActor());
		regenerate();
	}

	@Override
	protected void del(ComponentUIBase comp) {
		_Table.removeActor(comp.getActor());
		regenerate();
	}

	@Override
	protected WidgetGroup get() {
		return _Table;
	}

	@Override
	protected ComponentBase Load(Element element) throws Exception {
		return this;
	}

	@Override
	protected void Save(XmlWriter element) throws Exception {
		element.element("Component").attribute("Type", this.getClass().getName()).pop();
	}

	@Override
	protected void swap(ComponentUIBase a, ComponentUIBase b) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void visitComponent(ComponentBase component, ComponentFactory factory) {
		factory.callComponentNotify(this, component);
	}
}
