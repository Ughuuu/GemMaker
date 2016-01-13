package com.ngeen.component;

import com.badlogic.gdx.utils.XmlWriter;
import com.badlogic.gdx.utils.XmlReader.Element;
import com.ngeen.engine.Ngeen;
import com.ngeen.entity.Entity;

public class ComponentScript extends ComponentBase {
	protected Script Program;
	private String _ProgramName;

	public ComponentScript(Ngeen ng, Entity ent) {
		super(ng, ent);
		Program = null;
	}

	public ComponentScript setScript(Class<?> script) {
		try {
			_ProgramName = script.getName();
			Program = (Script) script.newInstance();
			Program.ng = _Ng;
			Program.holder = getOwner();
			Program.onInit();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return this;
	}

	public Script getScipt() {
		return Program;
	}

	@Override
	protected void Save(XmlWriter element) throws Exception {
		element.element("Component").attribute("_Type", this.getClass().getName()).attribute("_ProgramName", _ProgramName)
				.pop();
	}

	public ComponentScript setEnabled(boolean Enable) {
		this.Enable = Enable;
		Program.onInit();
		return this;
	}
	
	@Override
	protected void Load(Element element) throws Exception {
		_ProgramName = element.get("_ProgramName");
		Class<?> act = Class.forName(_ProgramName);
		try {
			_ProgramName = act.getName();
			Program = (Script) act.newInstance();
			Program.ng = _Ng;
			Program.holder = getOwner();
			Enable = false;
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
