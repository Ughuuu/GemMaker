package com.ngeen.component;

import com.badlogic.gdx.utils.XmlWriter;
import com.badlogic.gdx.utils.XmlReader.Element;
import com.ngeen.engine.Ngeen;

public class ComponentScript extends ComponentBase{
	protected Script Program;
	private String _ProgramName;
	
	public ComponentScript(Ngeen ng) {
		super(ng);
		Program = null;
	}
	
	public void setScript(Class<?> script) throws Exception{
		_ProgramName = script.getName();
		Program = (Script) script.newInstance();
		Program.ng = _Ng;
		Program.onInit();
	}
	
	public Script getScipt(){
		return Program;
	}

	@Override
	protected void Save(XmlWriter element) throws Exception {
		element.element("Component")
				.attribute("_Type", "ComponentScript")
				.attribute("_ProgramName", _ProgramName)
		       .pop();
	}

	@Override
	protected void Load(Element element) throws Exception {
		_ProgramName = element.get("_ProgramName");
		Class<?> act = Class.forName(_ProgramName);
		setScript(act);
	}
}
